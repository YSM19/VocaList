package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.QuizControllerDocs;
import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.dto.QuizDTO;
import com.example.vocatest.entity.QuizEntity;
import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.service.QuizService;
import com.example.vocatest.service.VocaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
@Tag(name = "퀴즈", description = "퀴즈 관련 API")
public class QuizController implements QuizControllerDocs {

    private final VocaService vocaService;
    private final QuizService quizService;

    @GetMapping("/{id}/{quizcount}")
    public List<VocaContentEntity> showRandomQuiz(@PathVariable("id") Long id,
                                                  @PathVariable("quizcount") Long quizcount){//퀴즈 랜덤으로 반환

        List<VocaContentEntity> selectedVocaContents = vocaService.findAllVocasByVocaListId(id);
        log.info("selectedVocaContents 잘 됨" + selectedVocaContents);
        Collections.shuffle(selectedVocaContents);

        if (quizcount > selectedVocaContents.size()) {
            quizcount = (long) selectedVocaContents.size();
        }

        return selectedVocaContents.subList(0, quizcount.intValue()); 
    }

    @PostMapping("/history/{vocalistId}")
    public ResponseEntity<QuizEntity> saveQuizScore(@PathVariable("vocalistId") Long vocalistId,
                                                          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                          @RequestBody QuizDTO quizDTO) {
        if (customOAuth2User != null){
            String email = customOAuth2User.getAttribute("email");
            QuizEntity quizEntity = quizService.saveQuizScore(vocalistId, email, quizDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(quizEntity);
        } else{
            log.info("로그인 되어있지 않음.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<QuizEntity>> showHistory(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        String email = customOAuth2User.getAttribute("email");
        if (email == null) {
            log.info("email아 존재하지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<QuizEntity> quizEntity = quizService.showAllQuizHistoryByEmail(email);

        if (quizEntity == null) {
            log.info("보여줄 QuizScore History 값이 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(quizEntity);

    }

}
