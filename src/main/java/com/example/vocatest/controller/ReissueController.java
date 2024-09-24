package com.example.vocatest.controller;

import com.example.vocatest.jwt.JwtUtil;
import com.example.vocatest.redis_change.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

// *change
@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에 존재하는 refreshToken을 가져오기
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        // 검증 시작
        // refreshToken이 없는 경우
        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // 유효기간 확인
        try {
            if (jwtUtil.isExpired(refresh)) {
                return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        //  토큰이 refresh인지 확인
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 새로운 Token을 만들기 위해 준비
        String username = jwtUtil.getUsername(refresh);
        String name = jwtUtil.getName(refresh);
        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);

        // Redis내에 존재하는 refreshToken인지 확인
        String redisRefreshToken = redisService.getValue(username);
        if (redisService.checkExistsValue(redisRefreshToken)) {
            return new ResponseEntity<>("no exists in redis refresh token", HttpStatus.BAD_REQUEST);
        }

        // 새로운 JWT Token 생성
        String newAccessToken = jwtUtil.createJwt("access", username, name, email, role, 1000*60*30L);
        String newRefreshToken  = jwtUtil.createJwt("refresh", username, name, email, role, 86400000L);

        // update refreshToken to Redis
        redisService.setValues(username, newRefreshToken, Duration.ofMillis(86400000L));

        // 응답
        response.setHeader("access", "Bearer " + newAccessToken);
        response.addCookie(createCookie("refresh", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60); // 쿠키가 살아있을 시간
//        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }


}
