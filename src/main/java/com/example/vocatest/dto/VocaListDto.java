package com.example.vocatest.dto;

import com.example.vocatest.entity.VocaListEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "단어장 DTO")
@Getter
@Setter
public class VocaListDto {
    @Schema(description = "단어장 제목", example = "English")
    private String title;

    public static VocaListEntity createVocaListToEntity(VocaListEntity originalVocaListEntity, String email){
        return new VocaListEntity(email, originalVocaListEntity.getTitle(), 0, 0L);
    }

    public VocaListEntity toEntity(String email){
        return new VocaListEntity(email, title, 0, 0L);
    }

}
