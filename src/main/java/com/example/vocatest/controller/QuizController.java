package com.example.vocatest.controller;

import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.service.VocaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/uservocalist/quiz")
public class QuizController {

    private final VocaService vocaService;

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
}
