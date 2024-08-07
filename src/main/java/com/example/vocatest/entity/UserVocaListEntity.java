package com.example.vocatest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserVocaListEntity { //유저가 가지고 있는 단어장

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vocalist_id")
    private VocaListEntity vocaListEntity; 
                                            

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity; 

    public UserVocaListEntity(){

    }

}
