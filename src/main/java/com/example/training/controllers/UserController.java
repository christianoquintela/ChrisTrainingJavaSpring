package com.example.training.controllers;

import com.example.training.models.User;
import com.example.training.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        final var byId = this.userService.findById(id);
        return ResponseEntity.ok().body(byId);
    }
    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        final var allUserIds = userService.findAllUserIds();
        return ResponseEntity.ok().body(allUserIds);
    }


    @PostMapping("/save")
    @Validated(User.CreateUser.class)
    public ResponseEntity<Void> save(@RequestBody @Valid User user) {
        this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand((user.getId()))
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated(User.UpdateUser.class)
    public ResponseEntity<Void> update(@Valid @RequestBody User user, @PathVariable Long id) {
        user.setId(id);
        this.userService.update(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
