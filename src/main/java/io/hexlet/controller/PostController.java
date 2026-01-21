package io.hexlet.controller;

import io.hexlet.model.Post;
import io.hexlet.util.SlugUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
    private final List<Post> posts;

    public PostController(List<Post> posts) {
        this.posts = posts;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "10") Integer limit) {
        var result = posts.stream().limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Posts", String.valueOf(posts.size()))
                .body(result);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> create(@RequestBody Post post) {
        post.setCreatedAt(LocalDateTime.now());
        post.setSlug(SlugUtils.generateSlug(post.getTitle()));
        posts.add(post);
        return ResponseEntity.created(URI.create("/posts/" + post.getSlug()))
                .body(post);
    }

    @GetMapping("/posts/{slug}")
    public ResponseEntity<Post> show(@PathVariable("slug") String slug) {
        var post = posts.stream()
                .filter(p -> p.getSlug().equals(slug))
                .findFirst();
        return post.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/posts/{slug}") // Обновление страницы
    public ResponseEntity<Post> update(@PathVariable String slug, @RequestBody Post data) {
        var maybePost = posts.stream()
                .filter(p -> p.getSlug().equals(slug))
                .findFirst();
        if (maybePost.isPresent()) {
            var post = maybePost.get();
            post.setTitle(data.getTitle());
            post.setContent(data.getContent());
            post.setAuthor(data.getAuthor());
            return ResponseEntity.ok()
                    .body(post);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/posts/{slug}")
    public ResponseEntity<Void> delete(@PathVariable("slug") String slug) {
        boolean remove = posts.removeIf(p -> p.getSlug().equals(slug));

        return (remove)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
