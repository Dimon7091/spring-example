package io.hexlet.controller;

import io.hexlet.model.Post;
import io.hexlet.model.User;
import io.hexlet.repository.UserRepository;
import io.hexlet.util.SlugUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> index(@RequestParam(defaultValue = "10") Integer limit) {
        var users = userRepository.findAll().stream().limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Posts", String.valueOf(userRepository.count()))
                .body(users);
    }

    @PostMapping("/users")
    public ResponseEntity<User> create(@RequestBody User user) {
        var email = user.getEmail();
        if (email.isEmpty()) {
            user.setEmail("Email не может быть пустым");
            return ResponseEntity.badRequest().body(user);
        }
        userRepository.save(user);
        return ResponseEntity.created(URI.create("/users/" + user.getId()))
                .body(user);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> show(@PathVariable("id") Long id) {
        var user = userRepository.findById(id);

        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}") // Обновление страницы
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User data) {
        var maybeUser = userRepository.findById(id);

        if (maybeUser.isPresent()) {
            var user = maybeUser.get();
            userRepository.save(user);
            user.setEmail(data.getEmail());
            user.setFirstName(data.getFirstName());
            user.setLastName(data.getLastName());
            return ResponseEntity.ok()
                    .body(user);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
