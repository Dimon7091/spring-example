package io.hexlet.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreatedDTO(
        @NotBlank(message = "Email обязателен")
        @Size(min = 6, max = 100, message = "Email от 6 до 100 символов")
        String email,

        @NotBlank(message = "Имя обязателено")
        @Size(min = 2, max = 100, message = "Имя от 2 до 100 символов")
        String firstName,

        @NotBlank(message = "Фамилия обязателена")
        @Size(min = 2, max = 100, message = "Фамилия от 2 до 100 символов")
        String lastName
) {}
