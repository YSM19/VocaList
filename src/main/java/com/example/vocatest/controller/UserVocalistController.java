package com.example.vocatest.controller;

import com.example.vocatest.dto.VocaListDto;
import com.example.vocatest.entity.UserVocaListEntity;
import com.example.vocatest.service.UserService;
import com.example.vocatest.service.VocaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/uservocalist")
public class UserVocalistController {

    private final VocaService vocaService;
    private final UserService userService;

    @GetMapping("/{id}") //유저가 목록에 있는 특정 id 단어장 가져오기
    public ResponseEntity<UserVocaListEntity> bringUserVocaList(@AuthenticationPrincipal OAuth2User oAuth2User,
                                                                @PathVariable("id")Long id){

        if (oAuth2User == null) {
            log.info("No user logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String email = oAuth2User.getAttribute("email");
        log.info("Logged in as : " + email);

        try {
            UserVocaListEntity userVocaListEntity = vocaService.createUserVocaList(email, id);
            return ResponseEntity.status(HttpStatus.CREATED).body(userVocaListEntity);
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @GetMapping() // 유저가 가지고 있는 단어장 보여주기
    public List<UserVocaListEntity> findUserVocaList(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User != null) {
            String email = oAuth2User.getAttribute("email");
            log.info("Logged in as : " + email);

            return vocaService.getUserVocaList(email);
        } else {
            log.info("No user logged in");
            return null;
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserVocaList(@AuthenticationPrincipal OAuth2User oAuth2User,
                                                     @PathVariable("id")Long id){ //유저가 가지고 있는 단어장 삭제 메소드

        if (oAuth2User == null) {
            log.info("No user logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String email = oAuth2User.getAttribute("email");
        log.info("Logged in as : " + email);

        //여기서 유저가 없는 단어장을 delete요청 한다면 예외처리 해야함
//            List<UserVocaListEntity> userVocaListEntity = vocaService.getUserVocaList(email); // ??
//            log.info("유저가 가지고 있는 모든 단어장 :" + userVocaListEntity.toString()); //여기까지 잘 됨

        try {
            vocaService.deleteUserVocaList(id);

            return ResponseEntity.ok("VocaList 삭제함");
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
