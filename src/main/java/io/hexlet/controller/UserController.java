package io.hexlet.controller;

import io.hexlet.DTO.UserCreatedDTO;
import io.hexlet.exception.ResourceNotFoundException;
import io.hexlet.model.Post;
import io.hexlet.model.User;
import io.hexlet.repository.UserRepository;
import io.hexlet.util.SlugUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<List<User>> index(@RequestParam(defaultValue = "10") Integer limit) {
        var users = userRepository.findAll().stream().limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Posts", String.valueOf(userRepository.count()))
                .body(users);
    }

    @PostMapping()
    public ResponseEntity<User> create(@Valid @RequestBody UserCreatedDTO data) {
       User user = new User();
       user.setEmail(data.email());
       user.setFirstName(data.firstName());
       user.setLastName(data.lastName());
       User savedUser = userRepository.save(user);
       return ResponseEntity.created(URI.create("/users" + savedUser.getId()))
               .body(savedUser);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> show(@PathVariable("id") Long id) {
        var user = userRepository.findById(id);

        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}") // Обновление страницы
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody UserCreatedDTO data) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity " + id + " Not found"));

        user.setEmail(data.email());
        user.setFirstName(data.firstName());
        user.setLastName(data.lastName());

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
