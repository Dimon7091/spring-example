package io.hexlet.model;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class User {
    private Long id;
    private String name;
    private String email;
}
