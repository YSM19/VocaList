//package com.example.vocatest.redis_change;
//
//import com.example.vocatest.entity.UserEntity;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.UUID;
//
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Setter
//@Entity
//public class UserRefreshToken {
//
//    @Id
//    private UUID userId;
//    @OneToOne (fetch = FetchType.LAZY) //회원 한명당 1개의 리프레시 토큰만 가질 수 있게 할 것이므로 RefreshToken과 User를 1:1 연관관계로 묶어준다
//    @MapsId
//    @JoinColumn(name = "member_id") // 연관된 회원ID를 외래키 겸 기본키로 지정해준다.
//    private UserEntity userEntity;
//    private String refreshToken;
//    private int reissueCount = 0; // 리프레시 토큰마다 재발급 횟수를 저장할 프로퍼티도 추가 (재발급 횟수를 제한할 것이기 때문)
//
//    public UserRefreshToken(UserEntity userEntity, String refreshToken) {
//        this.userEntity = userEntity;
//        this.refreshToken = refreshToken;
//    }
//
//    public void updateRefreshToken(String refreshToken) {
//        this.refreshToken = refreshToken;
//    }
//
//    public boolean validateRefreshToken(String refreshToken) {
//        return this.refreshToken.equals(refreshToken);
//    }
//
//    public void increaseReissueCount() {
//        reissueCount++;
//    }
//}
