package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.VocaListControllerDocs;
import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.dto.VocaListDto;
import com.example.vocatest.entity.VocaListEntity;
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
public class VocalistController implements VocaListControllerDocs { // 단어장

    private final VocaService vocaService;

    @GetMapping
    public List<VocaListEntity> findAllVocaList(){ // 단어장의 모든 리스트를 보여주기
//        List<VocaListEntity> vocaListEntity = vocaService.findAllVocaList();
        List<VocaListEntity> openedVocaListEntity = vocaService.findSecretVocaList(1);
        return openedVocaListEntity;
    }

    @GetMapping("{vocalistId}") // 선택한 단어장 조회
    public VocaListEntity findVocaListById(@PathVariable("vocalistId")Long vocalistId){
        return vocaService.findVocaListById(vocalistId);
    }

    @PostMapping //단어장 생성
    public ResponseEntity<VocaListEntity> createVocaList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                         @RequestBody VocaListDto vocaListDto){
        if (customOAuth2User != null){
            String email = customOAuth2User.getAttribute("email");
            VocaListEntity vocaListEntity = vocaService.createVocaList(email, vocaListDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(vocaListEntity);
        } else{
            log.info("로그인 되어있지 않음.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    @PatchMapping("{vocalistId}") // 단어장 수정
    public ResponseEntity<VocaListEntity> updateVocaList(@PathVariable("vocalistId")Long vocalistId,
                                                         @RequestBody VocaListDto vocaListDto,
                                                         @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        if (customOAuth2User != null){
            String email = customOAuth2User.getAttribute("email");
            VocaListEntity updatedVocaList = vocaService.updateVocaListById(vocalistId, vocaListDto, email);
            if (updatedVocaList != null) {
                return ResponseEntity.status(HttpStatus.OK).body(updatedVocaList);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @DeleteMapping("{vocalistId}")
    public ResponseEntity<VocaListEntity> deleteVocaList(@PathVariable("vocalistId")Long vocalistId){ // 단어장 삭제
        VocaListEntity vocaListEntity = vocaService.findVocaListById(vocalistId);
        if(vocaListEntity == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        vocaService.deleteVocaList(vocaListEntity);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("{vocalistId}/editsecret/open") // 단어장 공개 설정
    public String openVocaListSecret(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                   @PathVariable("vocalistId")Long vocalistId){
        String email = customOAuth2User.getAttribute("email");
        String authorEmail = vocaService.findVocaListById(vocalistId).getEmail();
        log.info("접근하는 유저의 이메일 " + email);
        log.info("단어장 저자의 이메일 " + vocaService.findVocaListById(vocalistId).getEmail());

        if (email != null && email.equals(authorEmail)) {
            log.info("수정 가능한 이용자");
            vocaService.findVocaListById(vocalistId).setSecret(1); // 공개로 설정함.
            vocaService.saveVocaList(findVocaListById(vocalistId));// 저장
            log.info("공개 설정 완료 db확인");
            return("공개설정 완료");
        } else {
            log.info("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
            return("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
        }

    }

    @GetMapping("{vocalistId}/editsecret/close") //단어장 비공개 설정
    public String closeVocaListSecret(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @PathVariable("vocalistId")Long vocalistId){
        String email = customOAuth2User.getAttribute("email");
        String authorEmail = vocaService.findVocaListById(vocalistId).getEmail();
        log.info("접근하는 유저의 이메일 " + email);
        log.info("단어장 저자의 이메일 " + vocaService.findVocaListById(vocalistId).getEmail());

        if (email != null && email.equals(authorEmail)) {
            log.info("수정 가능한 이용자");
            vocaService.findVocaListById(vocalistId).setSecret(0); // 비공개로 설정함.
            vocaService.saveVocaList(findVocaListById(vocalistId));// 저장
            log.info("비공개 설정 완료 db확인");
            return("비공개 설정 완료");
        } else {
            log.info("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
            return("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
        }
    }

}
