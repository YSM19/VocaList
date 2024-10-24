package com.example.vocatest.repository;

import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VocaContentRepository extends JpaRepository<VocaContentEntity, Long> {
    //@Query("SELECT vc from VocaContentEntity vc JOIN fetch vc.vocaListEntity WHERE vc.vocaListEntity.id = :vocaListId")
    List<VocaContentEntity> findByVocaListEntityId(Long vocaListId);
}
