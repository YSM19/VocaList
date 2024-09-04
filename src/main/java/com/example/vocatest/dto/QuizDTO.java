package com.example.vocatest.dto;

import com.example.vocatest.entity.QuizEntity;
import com.example.vocatest.entity.VocaListEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Schema(name = "quiz DTO")
@Getter
@Setter
public class QuizDTO {

    @Schema(description = "맞춘 퀴즈 개수", example = "8")
    private int score;

    @Schema(description = "퀴즈를 푼 날짜", example = "2024-09-04")
    private Date date;

    public QuizEntity toEntity(String email, VocaListEntity vocaListEntity) {
        return new QuizEntity(email, score, date, vocaListEntity);
    }
}
