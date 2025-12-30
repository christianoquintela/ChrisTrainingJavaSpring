package com.example.training.controllers;

import com.example.training.models.Task;
import com.example.training.services.TaskService;
import com.example.training.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/task")
@Validated
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;


    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable("id") Long id) {
        Task task = this.taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    //Busca tudo mermo
    @GetMapping("/all")
    public ResponseEntity<List<?>> findAll() {
        return ResponseEntity.ok(taskService.findAllTasks());

    }

//    Busca tudo pelo id do usuário
    @GetMapping("/user")
    public ResponseEntity<List<Task>> findAllByUser() {

        final var allByUserId = taskService.findAllByUser();
        return ResponseEntity.ok().body(allByUserId);
    }

    @PostMapping("/save")
    @Validated
    public ResponseEntity<Void> save(@RequestBody @Valid Task task) {
        this.taskService.create(task);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand((task.getId()))
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> update(@Valid @RequestBody Task task, @PathVariable Long id) {
        task.setId(id);
        this.taskService.update(task);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
