package io.hexlet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;


@Getter
@Setter

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String title;
    private String content;
    private Boolean published;
    private LocalDateTime createdAt;
}
