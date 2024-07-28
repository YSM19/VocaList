package com.example.vocatest.controller;

import com.example.vocatest.dto.VocaListDto;
import com.example.vocatest.entity.UserVocaListEntity;
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
@RequestMapping("/api/vocalist")
public class VocalistController { // 단어장

    private final VocaService vocaService;
    private final UserService userService;

    @GetMapping
    public List<VocaListEntity> findAllVocaList(){ // 단어장의 모든 리스트를 보여주기
        return vocaService.findAllVocaList();
    }

    @GetMapping("{id}") // 선택한 단어장 보기
    public VocaListEntity findVocaListById(@PathVariable("id")Long id){
        return vocaService.findVocaListById(id);
    }

    // @PostMapping // 단어장 생성
    // public VocaListEntity createVocaList(@RequestBody VocaListDto vocaListDto){ //단어장 만들기
    //     VocaListEntity vocaListEntity = vocaListDto.toEntity();
    //     return vocaService.saveVocaList(vocaListEntity);
    // }

    @PostMapping //단어장 생성
    public ResponseEntity<VocaListEntity> createVocaList(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody VocaListDto vocaListDto){
        if (oAuth2User != null){
            String email = oAuth2User.getAttribute("email");
            VocaListEntity vocaListEntity = vocaListDto.createVocalistToEntity(email);
            vocaService.saveVocaList(vocaListEntity);

            UserVocaListEntity userVocaListEntity = new UserVocaListEntity();
            userVocaListEntity.setUserEntity(userService.findUserByEmail(email));
            userVocaListEntity.setVocaListEntity(vocaListEntity);
            vocaService.saveUserVocaList(userVocaListEntity);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else{
            log.info("로그인 되어있지 않음.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @PatchMapping("{id}") // 단어장 수정
    public ResponseEntity<VocaListEntity> updateVocaList(@PathVariable("id")Long id, @RequestBody VocaListDto vocaListDto, @AuthenticationPrincipal OAuth2User oAuth2User){
        String email = oAuth2User.getAttribute("email");
        VocaListEntity target = vocaService.findVocaListById(id);

        if (target == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        //target.setAuthor(vocaListDto.getAuthor());
        target.setAuthor(email);
        target.setTitle(vocaListDto.getTitle());

        VocaListEntity updated = vocaService.saveVocaList(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<VocaListEntity> deleteVocaList(@PathVariable("id")Long id){ // 단어장 삭제
        VocaListEntity vocaListEntity = vocaService.findVocaListById(id);
        if(vocaListEntity == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        vocaService.deleteVocaList(vocaListEntity);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("{id}/editsecret/open")
    public void openVocaListSecret(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("id")Long id){
        String email = oAuth2User.getAttribute("email");
        String authorEmail = vocaService.findVocaListById(id).getAuthor();
        log.info("접근하는 유저의 이메일 " + email);
        log.info("단어장 저자의 이메일 " + vocaService.findVocaListById(id).getAuthor());

        if (email != null && email.equals(authorEmail)) {
            log.info("수정 가능한 이용자");
            vocaService.findVocaListById(id).setSecret(1); // 공개로 설정함.
            vocaService.saveVocaList(findVocaListById(id));// 저장
            log.info("공개 설정 완료 db확인");
        } else {
            log.info("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
        }

    }

    @GetMapping("{id}/editsecret/close")
    public void closeVocaListSecret(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("id")Long id){
        String email = oAuth2User.getAttribute("email");
        String authorEmail = vocaService.findVocaListById(id).getAuthor();
        log.info("접근하는 유저의 이메일 " + email);
        log.info("단어장 저자의 이메일 " + vocaService.findVocaListById(id).getAuthor());

        if (email != null && email.equals(authorEmail)) {
            log.info("수정 가능한 이용자");
            vocaService.findVocaListById(id).setSecret(0); // 비공개로 설정함.
            vocaService.saveVocaList(findVocaListById(id));// 저장
            log.info("비공개 설정 완료 db확인");
        } else {
            log.info("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
        }
    }


//    {
//  "author": "123Updated",
//  "title": "chinese"
//    }

}
