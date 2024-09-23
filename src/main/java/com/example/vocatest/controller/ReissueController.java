//package com.example.vocatest.controller;
//
//import com.example.vocatest.dto.CustomOAuth2User;
//import com.example.vocatest.jwt.JwtUtil;
//import com.example.vocatest.redis_change.RedisService;
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.Duration;
//
//@RestController
//@RequiredArgsConstructor
//public class ReissueController {
//
//    private final JwtUtil jwtUtil;
//
//    private final RedisService redisService;
//
//    @PostMapping("/reissue")
//    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//
//        // 쿠키에 존재하는 refreshToken을 가져오자
//        String refresh = null;
//        Cookie[] cookies = request.getCookies();
//        for(Cookie cookie : cookies) {
//            if("refresh".equals(cookie.getName())) {
//                refresh = cookie.getValue();
//            }
//        }
//
//        // 검증 시작
//        // refreshToken이 없는 경우
//        if(refresh == null) {
//            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
//        }
//
//        // 유효기간 확인
//        try {
//            if(jwtUtil.isExpired(refresh)) {
//                return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
//            }
//        } catch (ExpiredJwtException e) {
//            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
//        }
//
//        // 토큰이 refresh인지 확인
//        String category = jwtUtil.getCategory(refresh);
//
//        if(!category.equals("refresh")) {
//            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
//        }
//
//        CustomOAuth2User customUserDetail = (CustomOAuth2User) authentication.getPrincipal();
//        // 새로운 Token을 만들기 위해 준비
//        String username = jwtUtil.getUsername(refresh);
//        String name = customUserDetail.getName();
//        String email = customUserDetail.getEmail();
//
//        String role = jwtUtil.getRole(refresh);
//
//        // Redis내에 존재하는 refreshToken인지 확인
//        String redisRefrshToken = redisService.getValue(username);
//        if(redisService.checkExistsValue(redisRefrshToken)) {
//            return new ResponseEntity<>("no exists in redis refresh token", HttpStatus.BAD_REQUEST);
//        }
//
//        // 새로운 JWT Token 생성
//        String newAccessToken = jwtUtil.createJwt("access", username, name, email, role, 60000L);
//        String newRefreshToken = jwtUtil.createJwt("refresh", username, name, email, role, 86400000L);
//
//        // update refreshToken to Redis
//        redisService.setValues(username, newRefreshToken, Duration.ofMillis(86400000L));
//
//        // 응답
//        response.setHeader("access", "Bearer " + newAccessToken);
//        response.addCookie(createCookie("refresh", newRefreshToken));
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    private Cookie createCookie(String key, String value) {
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(24*60*60);     // 쿠키가 살아있을 시간
//        /*cookie.setSecure();*/         // https에서만 동작할것인지 (로컬은 http 환경이라 안먹음)
//        cookie.setPath("/");        // 쿠키가 전역에서 동작
//        cookie.setHttpOnly(true);       // http에서만 쿠키가 동작할 수 있도록 (js와 같은곳에서 가져갈 수 없도록)
//
//        return cookie;
//    }
//
//}
