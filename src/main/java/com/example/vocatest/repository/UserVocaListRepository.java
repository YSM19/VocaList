package com.example.vocatest.repository;

import com.example.vocatest.entity.UserVocaListEntity;
import com.example.vocatest.entity.VocaListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserVocaListRepository extends JpaRepository<UserVocaListEntity, Long> {
    List<UserVocaListEntity> findByUserEntityEmail(String email); //파라미터로 받는 값이 내가 조회할 속성값


    UserVocaListEntity findByVocaListEntityId(Long vocaId);

 

}
