package com.example.vocatest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int quizScore;

    @Schema(description = "단어장 참조값")
    @ManyToOne
    @JoinColumn(name = "vocalist_id")
    private VocaListEntity vocaListEntity;

    public QuizEntity(int quizScore, VocaListEntity vocaListEntity) {
        this.quizScore = quizScore;
        this.vocaListEntity = vocaListEntity;
    }

}
