package com.example.vocatest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserVocaListEntity { //유저가 가지고 있는 단어장
//
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "단어장 참조값")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocalist_id")
    private VocaListEntity vocaListEntity;

    @Schema(description = "유저 참조값")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public UserVocaListEntity(){

    }

}
