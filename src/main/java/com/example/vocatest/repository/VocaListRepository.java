package com.example.vocatest.repository;

import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VocaListRepository extends JpaRepository<VocaListEntity, Long> {
      List<VocaListEntity> findBySecret(int secret);
}
