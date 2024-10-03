package com.example.vocatest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    private String email;
    @Schema(description = "유저 참조값")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private int score;

    private Date date;

    @Schema(description = "단어장 참조값")
    @ManyToOne
    @JoinColumn(name = "vocalist_id")
    private VocaListEntity vocaListEntity;

    public QuizEntity(UserEntity userEntity, int score, Date date, VocaListEntity vocaListEntity) {
        this.userEntity = userEntity;
        this.score = score;
        this.date = date;
        this.vocaListEntity = vocaListEntity;
    }

}
