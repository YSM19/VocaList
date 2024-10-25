package com.example.vocatest.repository;

import com.example.vocatest.entity.UserVocaListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVocaListRepository extends JpaRepository<UserVocaListEntity, Long> {
    List<UserVocaListEntity> findByUserEntityEmail(String email); //파라미터로 받는 값이 내가 조회할 속성값

    UserVocaListEntity findByVocaListEntityId(Long vocaId);
    Optional<UserVocaListEntity> findById(Long id);
    List<UserVocaListEntity> findAllByVocaListEntity_Id(Long vocalistId);
    List<UserVocaListEntity> findAllByUserEntity_Id(Long userId);

}
