package io.hexlet;

import io.hexlet.model.Post;
import io.hexlet.model.User;
import io.hexlet.repository.PostRepository;
import io.hexlet.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelGenerator {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Faker faker;

    public void generateUsers(int count) {
        for (int i = 0; i < count; i++) {
            User user = User.builder()
                    .email(faker.internet().emailAddress())
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .build();
            userRepository.save(user);
        }
    }

    public void generatePosts(int count) {
        for (int i = 0; i < count; i++) {
            Post post = Post.builder()
                    .title(faker.book().title())
                    .content(faker.lorem().characters(50, 100))
                    .published(true)
                    .build();
            postRepository.save(post);
        }
    }

}
