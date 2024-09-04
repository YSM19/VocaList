package com.example.vocatest.dto;

import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "단어 DTO")
@Getter
@Setter
public class VocaContentDto {
    @Schema(description = "단어", example = "apple")
    private String text;
    @Schema(description = "단어 뜻", example = "사과")
    private String transtext;
    @Schema(description = "예문", example = "I eat apple")
    private String sampleSentence;

    public VocaContentEntity toEntity(VocaListEntity vocaListEntity){
        return new VocaContentEntity(text, transtext, sampleSentence, vocaListEntity);
    }
}
