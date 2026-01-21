package io.hexlet.controller;

import io.hexlet.model.Post;
import io.hexlet.model.User;
import io.hexlet.util.SlugUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class UserController {
    private final List<User> users;

    public UserController(List<User> users) {
        this.users = users;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> index(@RequestParam(defaultValue = "10") Integer limit) {
        var result = users.stream().limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Posts", String.valueOf(users.size()))
                .body(result);
    }

    @PostMapping("/users")
    public ResponseEntity<User> create(@RequestBody User user) {
        var email = user.getEmail();
        if (email.isEmpty()) {
            user.setEmail("Email не может быть пустым");
            return ResponseEntity.badRequest().body(user);
        }
        users.add(user);
        return ResponseEntity.created(URI.create("/users/" + user.getId()))
                .body(user);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> show(@PathVariable("id") Long id) {
        var user = users.stream()
                .filter(u -> Objects.equals(u.getId(), id))
                .findFirst();
        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}") // Обновление страницы
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User data) {
        var maybeUser = users.stream()
                .filter(u -> Objects.equals(u.getId(), id))
                .findFirst();
        if (maybeUser.isPresent()) {
            var user = maybeUser.get();
            user.setEmail(data.getEmail());
            user.setName(data.getName());
            return ResponseEntity.ok()
                    .body(user);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean remove = users.removeIf(u -> Objects.equals(u.getId(), id));

        return (remove)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
