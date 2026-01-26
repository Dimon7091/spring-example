package io.hexlet.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateDTO(
        @NotBlank(message = "Заголовок обязателен")
        @Size(min = 3, max = 100, message = "Заголовок от 3 до 100 символов")
        String title,

        @NotBlank(message = "Контент обязателен")
        @Size(min = 10, max = 1000, message = "Контент от 10 до 1000 символов")
        String content,

        @NotNull
        Boolean published
) {}
