package io.hexlet.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.model.Post;
import io.hexlet.model.User;
import io.hexlet.repository.PostRepository;
import io.hexlet.repository.UserRepository;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    void createPostPositive() throws Exception {
        var post = Map.of("title", "New title",
                "content", "New content",
                "published", true);

        var request = post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(post));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void getPostsPositive() throws Exception {
        var post1 = createPostInDb();
        var post2 = createPostInDb();

        var request = get("/api/posts");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(post1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(post2.getId()))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void  getPostByIdPositive() throws Exception {
        var post1 = createPostInDb();
        var post2 = createPostInDb();
        var post3 = createPostInDb();

        var request1 = get("/api/posts/" + post1.getId());
        var request2 = get("/api/posts/" + post2.getId());
        var request3 = get("/api/posts/" + post3.getId());

        mockMvc.perform(request1)
                .andExpect(status().isOk());
        mockMvc.perform(request2)
                .andExpect(status().isOk());
        mockMvc.perform(request3)
                .andExpect(status().isOk());
    }

    @Test
    void updatePostPositive() throws Exception {
        var post = createPostInDb();

        var updateUserData = Map.of("title", "New title",
                "content", "New content",
                "published", "true");

        var request = put("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(updateUserData));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(updateUserData.get("content")));
    }

    @Test
    void deletePostPositive() throws Exception {
        var post = createPostInDb();

        var request = delete("/api/posts/" + post.getId());

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    private Post createPostInDb() {
        Post post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getUpdatedAt))
                .ignore(Select.field(Post::getCreatedAt))
                .create();
        return postRepository.save(post);
    }

}
