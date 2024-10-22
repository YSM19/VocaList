package com.example.vocatest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class VocaContentEntity { //단어 내용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "단어", example = "apple")
    private String text; // 원본

    @Schema(description = "단어 해석", example = "사과")
    private String transtext; //해석

    @Schema(description = "예시 문장", example = "I eat apple")
    private String sampleSentence; // 예문

    @Schema(description = "단어장 참조값")
    @ManyToOne
    @JoinColumn(name = "vocalist_id")
    private VocaListEntity vocaListEntity;


//    생성자
    public VocaContentEntity(String text, String transtext, String sampleSentence, VocaListEntity vocaListEntity) {
        this.text = text;
        this.transtext = transtext;
        this.sampleSentence = sampleSentence;
        this.vocaListEntity = vocaListEntity;
    }

    public VocaContentEntity() {

    }


}
