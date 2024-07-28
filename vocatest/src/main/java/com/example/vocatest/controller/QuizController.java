package com.example.vocatest.controller;

import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.service.VocaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final VocaService vocaService;

    @GetMapping("/uservocalist/quiz/{id}/{quizcount}")
    public List<VocaContentEntity> showRandomQuiz(@PathVariable("id") Long id, @PathVariable("quizcount") Long quizcount){


        List<VocaContentEntity> selectedVocaContents = vocaService.findAllVocasByVocaListId(id);
        Collections.shuffle(selectedVocaContents);

        if (quizcount > selectedVocaContents.size()) {
            quizcount = (long) selectedVocaContents.size();
        }

        return selectedVocaContents.subList(0, quizcount.intValue()); //list 가 지원하는 메소드 출력할 범위를 지정함. 끝 인덱스는 포함x
    }
}
