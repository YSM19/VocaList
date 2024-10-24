package com.example.vocatest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class VocaListEntity { //단어장 목록

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "단어장 작성한 자지 이메일", example = "xxxx@gmail.com")
    private String email; // 단어장 작성한 자지 이메일

    @Schema(description = "단어장 저자", example = "김길똥")
    private String username; // 단어장의 작성자

    @Schema(description = "단어장 제목", example = "English")
    private String title; // 단어장의 제목

    @Schema(description = "단어장의 sercret 값 0비공개 / 1 공개", example = "1")
    private int secret; // 0이면 비공개 1이면 공개

    @Schema(description = "단어장을 받아간 횟수", example = "0")
    private Long count; // 받아간 횟수

    @OneToMany(mappedBy = "vocalist", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<VocaContentEntity> vocaContentEntity = new ArrayList<>();

    // 아래부턴 생성자
    public VocaListEntity() {

    }


    
    public VocaListEntity(String email, String username, String title, int secret, Long count) {
        this.email = email;
        this.username = username;
        this.title = title;
        this.secret = secret;
        this.count = count;
    }

    public void addCount(Long count){
        this.count += count;
    }

}
