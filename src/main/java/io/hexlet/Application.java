package io.hexlet;

import io.hexlet.model.Post;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
public class Application implements CommandLineRunner {

    private final ModelGenerator modelGenerator;  // ← Spring вставит!

    public Application(ModelGenerator modelGenerator) {  // ← Конструктор
        this.modelGenerator = modelGenerator;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {  // ← Выполняется при запуске!
        System.out.println("🚀 Заполняем БД тестовыми данными...");
        modelGenerator.generateUsers(10);   // 10 пользователей
        modelGenerator.generatePosts(30);   // 50 постов
        System.out.println("✅ БД заполнена!");
    }
}