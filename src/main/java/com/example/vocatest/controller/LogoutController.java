package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.LogoutControllerDocs;
import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Controller
@RequiredArgsConstructor
public class LogoutController implements LogoutControllerDocs {

    private final JwtUtil jwtUtil;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);     // 쿠키가 살아있을 시간
        cookie.setPath("/");            // 쿠키가 전역에서 동작
        cookie.setHttpOnly(true);       // http에서만 쿠키가 동작할 수 있도록 (js와 같은곳에서 가져갈 수 없도록)

        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }
}
