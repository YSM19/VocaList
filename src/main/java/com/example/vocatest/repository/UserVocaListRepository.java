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
    @Query("SELECT uv FROM UserVocaListEntity uv JOIN FETCH uv.vocaListEntity JOIN FETCH uv.userEntity WHERE uv.userEntity.email = :email")
    List<UserVocaListEntity> findByUserEntityEmail(String email); //파라미터로 받는 값이 내가 조회할 속성값

    UserVocaListEntity findByVocaListEntityId(Long vocaId);
    List<UserVocaListEntity> findAllByVocaListEntity_Id(Long vocalistId);
    List<UserVocaListEntity> findAllByUserEntity_Id(Long userId);

}
