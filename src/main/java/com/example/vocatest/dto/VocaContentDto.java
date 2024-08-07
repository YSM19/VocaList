package com.example.vocatest.dto;

import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VocaContentDto {
    private String text;
    private String transtext;
    private VocaListEntity vocaListEntity;

    public VocaContentEntity toEntity(VocaListEntity vocaListEntity){
        return new VocaContentEntity(text, transtext, vocaListEntity);
    }
}
