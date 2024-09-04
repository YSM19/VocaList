package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.UserControllerDocs;
import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    public List<UserEntity> findAllUser(){//모든 유저 조회
       return userService.findAllUsers();
    }

    @GetMapping("/{userId}")// 특정 id 사용자 조회
    public ResponseEntity<UserEntity> findUserById(@PathVariable("userId")Long userId){
        UserEntity findUser = userService.findUserById(userId);

        return ResponseEntity.ok(findUser);
    }

    @DeleteMapping("{userId}")//특정 id 사용자 삭제
    public ResponseEntity<UserEntity> delete(@PathVariable("userId")Long userId){
        UserEntity userEntity = userService.findUserById(userId);
        if (userEntity == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        userService.delete(userEntity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/logout")//로그아웃
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/myuserdata")
    public UserEntity getMyUserData(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        String email = customOAuth2User.getAttribute("email");
        return userService.findUserByEmail(email);
    }

}
