package com.example.training.services;

import com.example.training.models.Task;
import com.example.training.models.User;
import com.example.training.repositories.TaskRepository;
import com.example.training.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {

    private final UserService userService;
    private final TaskRepository taskRepository;

    public TaskService(UserService userService, TaskRepository taskRepository) {
        this.userService = userService;
        this.taskRepository = taskRepository;
    }

    public Task findById(Long id) {
        Optional<Task> taskOptional = this.taskRepository.findById(id);
        return taskOptional.orElseThrow(() -> new RuntimeException("Tarefa não encontrada! id: " + id + ", Tipo: " + Task.class.getName()));
    }

    @Transactional
    public Task create(Task obj){
        User user = this.userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj){
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        try{
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível deletar pois há entidades relacionadas. "+e.getMessage());
        }
    }
}
