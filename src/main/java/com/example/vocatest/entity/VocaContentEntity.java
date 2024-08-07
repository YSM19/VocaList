package com.example.vocatest.entity;

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

    private String text; // 원본

    private String transtext; //해석

    @ManyToOne
    @JoinColumn(name = "vocalist_id")
    private VocaListEntity vocaListEntity;


//    생성자
    public VocaContentEntity(String text, String transtext, VocaListEntity vocaListEntity) {
        this.text = text;
        this.transtext = transtext;
        this.vocaListEntity = vocaListEntity;
    }

    public VocaContentEntity() {

    }


}
