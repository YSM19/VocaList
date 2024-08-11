package com.example.vocatest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class VocaListEntity { //단어장 목록

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author; // 단어장의 작성자

    private String title; // 단어장의 제목

    private int secret; // 0이면 비공개 1이면 공개

    private Long count; // 받아간 횟수

    // 아래부턴 생성자
    public VocaListEntity() {

    }


    
    public VocaListEntity(String author, String title, int secret, Long count) {
        this.author = author;
        this.title = title;
        this.secret = secret;
        this.count = count;
    }

    public void addCount(Long count){
        this.count += count;
    }

}
