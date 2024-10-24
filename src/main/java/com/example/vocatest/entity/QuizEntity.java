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

    private String email;

    private int score;

    private Date date;

    @Schema(description = "단어장 참조값")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocalist_id")
    private VocaListEntity vocaListEntity;

    public QuizEntity(String email, int score, Date date, VocaListEntity vocaListEntity) {
        this.email = email;
        this.score = score;
        this.date = date;
        this.vocaListEntity = vocaListEntity;
    }

}
