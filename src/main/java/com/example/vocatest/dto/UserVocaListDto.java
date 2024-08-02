package com.example.vocatest.dto;

import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.entity.VocaListEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVocaListDto {
    private VocaListEntity vocaListEntity;

    private UserEntity userEntity;



    public UserVocaListDto(){

    }
}
