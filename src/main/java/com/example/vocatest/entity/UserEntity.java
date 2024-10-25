package com.example.vocatest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Schema(description = "유저 이름", example = "google")
    private String username;

    @Schema(description = "유저 이메일", example = "xxxx@gmail.com")
    private String email;

    @Schema(description = "역할", example = "USER")
    private String role;

    @Schema(description = "유저 Total 스코어", example = "10")
    private int totalScore;

}
