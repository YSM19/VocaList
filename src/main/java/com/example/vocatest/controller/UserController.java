package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.UserControllerDocs;
import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.entity.QuizEntity;
import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.service.UserService;
import com.example.vocatest.service.VocaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

    private final UserService userService;
    private final VocaService vocaService;
    
    // no use
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
        // 유저 id로 유저 찾기
        UserEntity userEntity = userService.findUserById(userId);
        if (userEntity == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        // 유저가 있으면
        // user와 연관되어있는 uservocalist에서 user가 만든 모든 단어장 삭제
        vocaService.deleteAllUserVocaListByuserId(userId);
        
        // 유저 삭제
        userService.delete(userEntity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


//    @GetMapping("/logout")//로그아웃
//    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }
//
//        return ResponseEntity.ok("Logged out successfully");
//    }

    @GetMapping("/myuserdata")
    public ResponseEntity<UserEntity> getMyUserData(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        String email = customOAuth2User.getAttribute("email");
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PostMapping("/addtotalscore")
    public ResponseEntity<UserEntity> addTotalScore(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                    @RequestBody QuizEntity quizEntity) {
        String email = customOAuth2User.getAttribute("email");
        userService.addTotalScrore(email, quizEntity.getScore());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
