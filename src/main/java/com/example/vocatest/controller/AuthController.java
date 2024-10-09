package com.example.vocatest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @GetMapping("/oauth2/authorization/google")
    public ResponseEntity<Map<String, String>> getGoogleAuthUrl() {
        String authorizationUri = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=your-client-id" +
                "&redirect_uri=http://ec2-15-164-103-179.ap-northeast-2.compute.amazonaws.com:8080/login/oauth2/code/google" +
                "&response_type=code" +
                "&scope=email%20profile";

        Map<String, String> response = new HashMap<>();
        response.put("authorizationUrl", authorizationUri);
        return ResponseEntity.ok(response);
    }
}