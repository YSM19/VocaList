package com.example.vocatest.repository;

import com.example.vocatest.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, UUID> {
    // 소유자의 ID가 id면서 재발급 횟수가 count보다 작은 MemberRefreshToken 객체를 반환
    Optional<UserRefreshToken> findByUserIdAndReissueCountLessThan(UUID id, long count);
}
