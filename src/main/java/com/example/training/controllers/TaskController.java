package com.example.training.controllers;

import com.example.training.models.Task;
import com.example.training.services.TaskService;
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

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
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
    @GetMapping("/all/{id}")
    public ResponseEntity<List<Task>> findAlll(@PathVariable Long id) {
        final var allByUserId = taskService.findAllByUserId(id);
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
