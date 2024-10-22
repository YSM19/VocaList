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

    public static VocaListEntity createVocaListToEntity(VocaListEntity originalVocaListEntity, String email, String username){
        return new VocaListEntity(email, username, originalVocaListEntity.getTitle(), 0, 0L);
    }

    public VocaListEntity toEntity(String email, String username){
        return new VocaListEntity(email, username, title, 0, 0L);
    }

}
