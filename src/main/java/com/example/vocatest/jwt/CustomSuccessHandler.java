package com.example.vocatest.jwt;


import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.redis_change.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${config.front_url}")
    private String frontUrl;

    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetail = (CustomOAuth2User) authentication.getPrincipal();

        // 토큰 생성시에 사용자명과 권한이 필요
        String username = customUserDetail.getUserName();
        String name = customUserDetail.getName();
        String email = customUserDetail.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

//        //*original*
//        String token = jwtUtil.createJwt(username, name, email, role, 1000*60*60*24*7L);
////        String token = jwtUtil.createJwt(username, name, email, role, 60*60*24L);
//        response.addCookie(createCookie("Authorization", token));
//        response.setStatus(HttpStatus.OK.value());
//        //*aws*
//        response.sendRedirect("http://ec2-52-78-64-218.ap-northeast-2.compute.amazonaws.com:3000");      // 로그인 성공시 프론트에 알려줄 redirect 경로
//        // */

        //*change*
//        accessToken과 refreshToken 생성
        String accessToken = jwtUtil.createJwt("access", username, name, email, role, 1000*60*30L);
        String refreshToken = jwtUtil.createJwt("refresh", username, name, email, role, 86400000L);

        // redis에 insert (key = username / value = refreshToken)
        redisService.setValues(username, refreshToken, Duration.ofMillis(86400000L));

        // 응답 바디에 accessToken과 refreshToken을 JSON으로 담아서 전송
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        String jsonResponse = String.format(
//                "{\"accessToken\": \"%s\", \"redirectUrl\": \"http://ec2-15-164-103-179.ap-northeast-2.compute.amazonaws.com:3000\"}",
//                accessToken
//        );

//        String redirectUrl = String.format("http://ec2-15-164-103-179.ap-northeast-2.compute.amazonaws.com:3000/auth-callback?accessToken=%s&refresh_token=%s", accessToken, refreshToken);
//        String redirectUrl = String.format("https://vocalist.kro.kr/auth-callback?accessToken=%s&refresh_token=%s", accessToken, refreshToken);
//        String redirectUrl = String.format("https://vocalist.kro.kr/auth-callback?accessToken=%s", accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
//        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        // 응답 //
//        response.setHeader("access", "Bearer " + accessToken);
        response.addCookie(createCookie("access", accessToken));
//        response.getWriter().write(jsonResponse);
//        response.addCookie(createCookie("refresh", refreshToken));
//        response.setStatus(HttpStatus.OK.value());
        //*aws*
//        response.sendRedirect(frontUrl);      // 로그인 성공시 프론트에 알려줄 redirect 경로
        // */

    }


    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);     // 쿠키가 살아있을 시간
        /*cookie.setSecure();*/         // https에서만 동작할것인지 (로컬은 http 환경이라 안먹음)
        cookie.setPath("/");            // 쿠키가 전역에서 동작
        cookie.setHttpOnly(true);       // http에서만 쿠키가 동작할 수 있도록 (js와 같은곳에서 가져갈 수 없도록)

        return cookie;
    }

}
