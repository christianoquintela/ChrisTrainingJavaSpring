package com.example.training.services;

import com.example.training.models.Task;
import com.example.training.models.User;
import com.example.training.models.enums.ProfileEnum;
import com.example.training.repositories.TaskRepository;
import com.example.training.repositories.UserRepository;
import com.example.training.security.UserSpringSecurity;
import com.example.training.services.exceptions.AuthorizationException;
import com.example.training.services.exceptions.DataBindingViolationException;
import com.example.training.services.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final UserService userService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(UserService userService, TaskRepository taskRepository, UserRepository userRepository) {
        this.userService = userService;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task findById(Long id) {
        Task task = this.taskRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Tarefa não encontrada! id: " + id + ", Tipo: " + Task.class.getName()));
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
            throw new AuthorizationException("Acesso negado!");
        return task;
    }

    public List<?> findAllTasks() {
        List<User> users = this.userRepository.findAll();
        if (users.isEmpty()) return Collections.emptyList();
        return users.stream()
                .map(User::getTasks)
                .collect(Collectors.toList());
    }

    public List<Task> findAllByUser() {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");
        List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks;
    }

    @Transactional
    public Task create(Task obj) {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        User user = this.userService.findById(userSpringSecurity.getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível deletar pois há entidades relacionadas. " + e.getMessage());
        }
    }

    public Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }
}
