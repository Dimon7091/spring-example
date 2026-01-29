package io.hexlet.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.ModelGenerator;
import io.hexlet.model.User;
import io.hexlet.repository.UserRepository;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.instancio.Instancio;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUserPositive() throws Exception {
        var createData = Map.of(
                "firstName", faker.name().firstName(),
                "lastName", faker.name().lastName(),
                "email", faker.internet().emailAddress()
        );

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createData));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    void getUsersPositive() throws Exception {
        var user1 = createUserInDb();
        var user2 = createUserInDb();

        var request = get("/api/users");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void  getUserByIdPositive() throws Exception {
        var user1 = createUserInDb();
        var user2 = createUserInDb();
        var user3 = createUserInDb();

        var requestUser1 = get("/api/users/" + user1.getId());
        var requestUser2 = get("/api/users/" + user2.getId());
        var requestUser3 = get("/api/users/" + user3.getId());

        mockMvc.perform(requestUser1)
                .andExpect(status().isOk());
        mockMvc.perform(requestUser2)
                .andExpect(status().isOk());
        mockMvc.perform(requestUser3)
                .andExpect(status().isOk());
    }

    @Test
    void updateUserPositive() throws Exception {
        var user = createUserInDb();

        var updateUserData = Map.of("email", "Dmitry",
                "firstName", "Dmitry",
                "lastName", "Gorbunov");

        var request = put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(updateUserData));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updateUserData.get("firstName")));
    }

    @Test
    void deleteUserPositive() throws Exception {
        var user = createUserInDb();

        var request = delete("/api/users/" + user.getId());

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    private User createUserInDb() {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getUpdatedAt))
                .ignore(Select.field(User::getCreatedAt))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        return userRepository.save(user);
    }
}
