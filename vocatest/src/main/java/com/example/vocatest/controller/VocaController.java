package com.example.vocatest.controller;

import com.example.vocatest.dto.VocaContentDto;
import com.example.vocatest.dto.VocaListDto;
import com.example.vocatest.entity.UserVocaListEntity;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.service.UserService;
import com.example.vocatest.service.VocaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/vocalist")
public class VocaController {

    private final VocaService vocaService;
    private final UserService userService;

    public VocaController(VocaService vocaService, UserService userService){
        this.vocaService = vocaService;
        this.userService = userService;
    }





    @GetMapping
    public List<VocaListEntity> findAllVocaList(){ // 단어장의 모든 리스트를 보여주기
        return vocaService.findAllVocaList(); 
    }

    @GetMapping("{id}") // 선택한 단어장 보기
    public VocaListEntity findVocaListById(@PathVariable("id")Long id){
        return vocaService.findVocaListById(id);
    }

    @PostMapping // 단어장 생성
    public VocaListEntity createVocaList(@RequestBody VocaListDto vocaListDto){ //단어장 만들기
        VocaListEntity vocaListEntity = vocaListDto.toEntity();
        return vocaService.saveVocaList(vocaListEntity);
    }

    @PatchMapping("{id}") // 단어장 수정
    public ResponseEntity<VocaListEntity> updateVocaList(@PathVariable("id")Long id, @RequestBody VocaListDto vocaListDto){
       //VocaListEntity vocaListEntity = vocaListDto.toEntity(); 

        VocaListEntity target = vocaService.findVocaListById(id);

        if (target == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        target.setAuthor(vocaListDto.getAuthor());
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



//    {
//  "author": "123Updated",
//  "title": "chinese"
//    }



    //------------------여기까지 단어장 관리-----------------------

    @GetMapping("{id}/word")
    public ResponseEntity<List<VocaContentEntity>> getAllVocasByVocaListId(@PathVariable("id") Long id) { //단어장에 있는 모든 단어를 보여주는 메소드
        List<VocaContentEntity> vocas = vocaService.findAllVocasByVocaListId(id);
        return ResponseEntity.ok().body(vocas);
    }

    @PostMapping("/{id}/word")
    public VocaContentEntity addWord(@PathVariable("id") Long id, @RequestBody VocaContentDto vocaContentDto){ // 단어장에 단어 등록
        VocaListEntity vocaListEntity = vocaService.findVocaListById(id); 
        VocaContentEntity vocaContentEntity = vocaContentDto.toEntity(vocaListEntity); 
        return vocaService.saveVocaContent(vocaContentEntity); 
    }

    @PatchMapping("/{id}/word/{wordid}")//단어수정
    public ResponseEntity<VocaContentEntity> updateVocaContent(@PathVariable("id")Long id, @PathVariable("wordid") Long wordid, @RequestBody VocaContentDto vocaContentDto){
        VocaContentEntity target = vocaService.getVocaContentId(wordid);

        if (target == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        target.setText(vocaContentDto.getText());
        target.setTranstext(vocaContentDto.getTranstext());

        VocaContentEntity updated = vocaService.saveVocaContent(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("{id}/word/{wordid}")//단어 삭제
    public ResponseEntity<VocaContentEntity> deleteVocaContent(@PathVariable("id")Long id, @PathVariable("wordid")Long wordid){
        VocaContentEntity target = vocaService.getVocaContentId(wordid);

        if(target == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        vocaService.deleteVocaContent(target);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

// ---------------------------여기까지 단어장 내용 관리------------------------


    //등록,삭제,조회만 하면 될듯
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

    @GetMapping("/uservocalist/{id}") //유저가 단어장 얻어오기
    public UserVocaListEntity addUserVocaList(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("id")Long id){
        if (oAuth2User != null) { //없는 단어장으로 post 요청 갔을 때 예외처리 해줘야 할듯
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
            return vocaService.saveUserVocaList(userVocaListEntity);

            //대충 했는데 테스트 좀 많이 필요할듯.
        } else {
            log.info("No user logged in");
            return null;
        }
    }

    //유저가 가지고 있는 단어장 삭제 메소드
    //여기서 주는 id 값은 단어장 PK Id값임.
    @DeleteMapping("/uservocalist/delete/{id}")
    public ResponseEntity<String> deleteUserVocaList(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("id")Long id){
        if (oAuth2User != null){
            String email = oAuth2User.getAttribute("email");
            log.info("Logged in as : " + email);

            //여기서 유저가 없는 단어장을 delete요청 한다면 예외처리 해야될 것 같은데 그럴 일이 없을거같으니 일단 패스
    

          
            List<UserVocaListEntity> userVocaListEntity = vocaService.getUserVocaList(email); //
            log.info("유저가 가지고 있는 모든 단어장 :" + userVocaListEntity.toString()); //여기까지 잘 됨
          
            UserVocaListEntity deleteTarget = vocaService.getUserVocaId(id);
            log.info("삭제되어야 할 단어장: " + deleteTarget); //잘 됨.

            vocaService.deleteUserVocaList(deleteTarget);
            log.info("삭제 완료 db확인");
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else{
            log.info("No user logged in");
            return null;
        }
    }

// -----------------------로그인한 유저의 단어장 가져오는 것들--------------------
 
    @GetMapping("/uservocalist/quiz/{id}/{quizcount}") //post로 받을까?
    public List<VocaContentEntity> showRandomQuiz(@PathVariable("id") Long id, @PathVariable("quizcount") Long quizcount){


        List<VocaContentEntity> selectedVocaContents = vocaService.findAllVocasByVocaListId(id);
        Collections.shuffle(selectedVocaContents);
 
        if (quizcount > selectedVocaContents.size()) {
            quizcount = (long) selectedVocaContents.size();
        }

        return selectedVocaContents.subList(0, quizcount.intValue()); //list 가 지원하는 메소드 출력할 범위를 지정함. 끝 인덱스는 포함x
    }


//  -----------------------퀴즈 불러오기------------------------------
}
