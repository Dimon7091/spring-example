package io.hexlet.controller;

import io.hexlet.DTO.PostCreateDTO;
import io.hexlet.exception.ResourceNotFoundException;
import io.hexlet.model.Post;
import io.hexlet.repository.PostRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@Validated
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("")
    public ResponseEntity<Page<Post>> index(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").ascending());
        var getPage = postRepository.findAll(pageable);
        return ResponseEntity.ok()
                .header("X-Total-Posts", String.valueOf(postRepository.count()))
                .body(getPage);
    }

    @PostMapping("")
    public ResponseEntity<Post> create(@Valid @RequestBody PostCreateDTO data) {
        Post post = new Post();
        post.setTitle(data.title());
        post.setContent(data.content());
        post.setPublished(data.published());
        Post savedPost = postRepository.save(post);
        return ResponseEntity.created(URI.create("/posts/" + savedPost.getId()))
                .body(savedPost);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post show(@PathVariable("id") Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity " + id + " Not found"));
    }

    @PutMapping("{id}") // Обновление страницы
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

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity " + id + " Not found"));
        postRepository.deleteById(id);
    }
}
