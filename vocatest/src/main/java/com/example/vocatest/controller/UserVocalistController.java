package com.example.vocatest.controller;

import com.example.vocatest.entity.UserVocaListEntity;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
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

    @GetMapping("/uservocalist") // 유저가 가지고 있는 단어장 보여주기
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

    @GetMapping("/uservocalist/{id}") //유저가 목록에 있는 특정 id 단어장 가져오기
    public ResponseEntity<UserVocaListEntity> addUserVocaList(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("id")Long id){
        if (oAuth2User != null) { //없는 단어장으로 get 요청 갔을 때 예외처리 필요
            String email = oAuth2User.getAttribute("email");
            log.info("Logged in as : " + email);

            VocaListEntity originalVocaListEntity = vocaService.findVocaListById(id);


            VocaListEntity createVocaListEntity = new VocaListEntity();
            createVocaListEntity.setAuthor(originalVocaListEntity.getAuthor());
            createVocaListEntity.setTitle(originalVocaListEntity.getTitle());
            vocaService.saveVocaList(createVocaListEntity);


            List<VocaContentEntity> selectedAllVocaContent = vocaService.findAllVocasByVocaListId(id);

            log.info("단어장에 들어가야 할 단어 리스트 : "+ selectedAllVocaContent.toString());

            for (VocaContentEntity vocaContent : selectedAllVocaContent) {
                VocaContentEntity createVocaContentEntity = new VocaContentEntity();
                createVocaContentEntity.setText(vocaContent.getText());
                createVocaContentEntity.setTranstext(vocaContent.getTranstext());
                createVocaContentEntity.setVocaListEntity(createVocaListEntity);
                vocaService.saveVocaContent(createVocaContentEntity);
            }

            UserVocaListEntity userVocaListEntity = new UserVocaListEntity();
            userVocaListEntity.setVocaListEntity(createVocaListEntity);
            //userVocaListEntity.setVocaListEntity(vocaService.findVocaListById(id));
            userVocaListEntity.setUserEntity(userService.findUserByEmail(email));

            UserVocaListEntity userVocaListEntity1 = vocaService.saveUserVocaList(userVocaListEntity);

            return ResponseEntity.status(HttpStatus.CREATED).body(userVocaListEntity1);
        } else {
            log.info("No user logged in");
            return null;
        }
    }

    @DeleteMapping("/uservocalist/delete/{id}")
    public ResponseEntity<String> deleteUserVocaList(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("id")Long id){ //유저가 가지고 있는 단어장 삭제 메소드
        if (oAuth2User != null){
            String email = oAuth2User.getAttribute("email");
            log.info("Logged in as : " + email);

            //여기서 유저가 없는 단어장을 delete요청 한다면 예외처리 해야함

            List<UserVocaListEntity> userVocaListEntity = vocaService.getUserVocaList(email); //
            log.info("유저가 가지고 있는 모든 단어장 :" + userVocaListEntity.toString()); //여기까지 잘 됨

            UserVocaListEntity deleteTarget = vocaService.getUserVocaListId(id);
            log.info("삭제되어야 할 단어장: " + deleteTarget); //잘 됨.

            vocaService.deleteUserVocaList(deleteTarget);
            log.info("삭제 완료 db확인");
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else{
            log.info("No user logged in");
            return null;
        }
    }
}
