package io.hexlet.controller;

import io.hexlet.DTO.PostCreateDTO;
import io.hexlet.exception.ResourceNotFoundException;
import io.hexlet.model.Post;
import io.hexlet.repository.PostRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@Validated
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "10") Integer limit) {
        var result = postRepository.findAll().stream().limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Posts", String.valueOf(postRepository.count()))
                .body(result);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> create(@Valid @RequestBody PostCreateDTO data) {
        Post post = new Post();
        post.setTitle(data.title());
        post.setContent(data.content());
        post.setPublished(data.published());
        post.setCreatedAt(LocalDateTime.now());
        Post savedPost = postRepository.save(post);
        return ResponseEntity.created(URI.create("/posts/" + savedPost.getId()))
                .body(savedPost);
    }

    @GetMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post show(@PathVariable("id") Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity " + id + " Not found"));
    }

    @PutMapping("/posts/{id}") // Обновление страницы
    @ResponseStatus(HttpStatus.OK)
    public Post update(@PathVariable Long id, @Valid @RequestBody PostCreateDTO data) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity " + id + " Not found"));

        // ✅ Обновляем ПОЛЯ
        post.setTitle(data.title());
        post.setContent(data.content());
        post.setPublished(data.published());

        // ✅ Сохраняем ПОСЛЕ изменений
        return postRepository.save(post);
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity " + id + " Not found"));
        postRepository.deleteById(id);
    }
}
