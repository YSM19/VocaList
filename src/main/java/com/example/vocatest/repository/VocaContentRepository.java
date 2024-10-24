package com.example.vocatest.repository;

import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VocaContentRepository extends JpaRepository<VocaContentEntity, Long> {
    @Query("SELECT DISTINCT uv FROM UserVocaListEntity uv " +
            "JOIN FETCH uv.vocaListEntity v " +
            "JOIN FETCH uv.userEntity u " +
            "JOIN FETCH VocaContentEntity vc ON vc.vocaListEntity.id = v.id " +  // VocaContentEntity를 패치 조인
            "WHERE uv.userEntity.email = :email")
    List<VocaContentEntity> findByVocaListEntityId(Long vocaListId);
}
