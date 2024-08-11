package com.example.vocatest.dto;

import com.example.vocatest.entity.VocaListEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VocaListDto { 
    private String author;
    private String title;

    /*
    public VocaListEntity toEntity(){
        return new VocaListEntity(author, title); 
    }
    */
    public VocaListEntity createVocalistToEntity(String email){
        return new VocaListEntity(email, title, 0, 0L);
    }

    public static VocaListEntity createVocaListToEntity (VocaListEntity originalVocaListEntity){
        return new VocaListEntity(originalVocaListEntity.getAuthor(), originalVocaListEntity.getTitle(), 0, 0L);

    }

}
