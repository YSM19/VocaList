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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/vocalist")
public class VocalistController implements VocaListControllerDocs { // 단어장

    private final VocaService vocaService;

    @GetMapping("/showall")
    public ResponseEntity<List<VocaListEntity>> findAllVocaList(){ // secret이 1인 단어장의 모든 리스트를 보여주기
//        List<VocaListEntity> vocaListEntity = vocaService.findAllVocaList();
        List<VocaListEntity> openedVocaListEntity = vocaService.findSecretVocaList(1);
        return ResponseEntity.ok(openedVocaListEntity);
    }

    @GetMapping("/show/{vocalistId}") // 선택한 단어장 조회
    public ResponseEntity<VocaListEntity> findVocaListById(@PathVariable("vocalistId")Long vocalistId){
        VocaListEntity vocaListEntity = vocaService.findVocaListById(vocalistId);
        return ResponseEntity.ok(vocaListEntity);
    }

    //단어장 생성
    @PostMapping("/create")
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

    @PatchMapping("/modify/{vocalistId}") // 단어장 수정
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

    @DeleteMapping("/delete/{vocalistId}") // 단어장 삭제
    public ResponseEntity<VocaListEntity> deleteVocaList(@PathVariable("vocalistId")Long vocalistId,
                                                         @AuthenticationPrincipal CustomOAuth2User customOAuth2User){ // 단어장 삭제
        // 로그인 여부
        if (customOAuth2User == null){
            log.info("로그인 되어있지 않음.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // 단어장과 연관된 Uservocalist 먼저 삭제
        vocaService.deleteAllUserVocaListByvocalistId(vocalistId);
        log.info("Uservocalist 삭제됨");

        // 단어장과 연관된 vocaContent 삭제
        vocaService.deleteAllVocaContentByVocaListId(vocalistId);

        // 단어장 삭제
        VocaListEntity vocaListEntity = vocaService.findVocaListById(vocalistId);
        if(vocaListEntity == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        vocaService.deleteVocaList(vocaListEntity);
        log.info("Vocalist 삭제됨");
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/{vocalistId}/editsecret/open") // 단어장 공개 설정
    public ResponseEntity<String> openVocaListSecret(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                     @PathVariable("vocalistId")Long vocalistId){

        String email = customOAuth2User.getAttribute("email");
        String authorEmail = vocaService.findVocaListById(vocalistId).getEmail();
        log.info("접근하는 유저의 이메일 " + email);
        log.info("단어장 저자의 이메일 " + authorEmail);

        if (email != null && email.equals(authorEmail)) {
            log.info("수정 가능한 이용자");
            vocaService.findVocaListById(vocalistId).setSecret(1); // 공개로 설정함.
            vocaService.saveVocaList(vocaService.findVocaListById(vocalistId));// 저장
            log.info("공개 설정 완료 db확인");
            return ResponseEntity.status(HttpStatus.OK).body("공개설정 완료");
        } else {
            log.info("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
            return ResponseEntity.status(HttpStatus.OK).body("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
        }

    }

    @GetMapping("/{vocalistId}/editsecret/close") //단어장 비공개 설정
    public ResponseEntity<String> closeVocaListSecret(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                      @PathVariable("vocalistId")Long vocalistId){

        String email = customOAuth2User.getAttribute("email");
        String authorEmail = vocaService.findVocaListById(vocalistId).getEmail();
        log.info("접근하는 유저의 이메일 " + email);
        log.info("단어장 저자의 이메일 " + vocaService.findVocaListById(vocalistId).getEmail());

        if (email != null && email.equals(authorEmail)) {
            log.info("수정 가능한 이용자");
            vocaService.findVocaListById(vocalistId).setSecret(0); // 비공개로 설정함.
            vocaService.saveVocaList(vocaService.findVocaListById(vocalistId));// 저장
            log.info("비공개 설정 완료 db확인");
            return ResponseEntity.status(HttpStatus.OK).body("비공개설정 완료");
        } else {
            log.info("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
            return ResponseEntity.status(HttpStatus.OK).body("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
        }
    }

}
