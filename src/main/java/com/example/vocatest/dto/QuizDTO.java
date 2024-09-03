package com.example.vocatest.dto;

import com.example.vocatest.entity.QuizEntity;
import com.example.vocatest.entity.VocaListEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "quiz DTO")
@Getter
@Setter
public class QuizDTO {
    @Schema(description = "맞춘 퀴즈 개수", example = "8")
    private int solvedQuizCount;

    private VocaListEntity vocaListEntity;

    public QuizEntity toEntity(VocaListEntity vocaListEntity) {
        return new QuizEntity(solvedQuizCount, vocaListEntity);
    }
}
