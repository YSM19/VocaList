package com.example.vocatest.service;

import com.example.vocatest.dto.QuizDTO;
import com.example.vocatest.entity.QuizEntity;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.repository.QuizRepository;
import com.example.vocatest.repository.VocaListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final VocaService vocaService;
    private final VocaListRepository vocaListRepository;

    public QuizEntity saveQuizScore(Long vocalistId, String email, QuizDTO quizDTO) {
        VocaListEntity vocaListEntity = vocaService.findVocaListById(vocalistId);
        if (vocaListEntity == null) {
            throw new IllegalArgumentException("유효한 VocaList ID가 없음");
        }
        QuizEntity quizEntity = quizDTO.toEntity(vocaListEntity);
        return quizRepository.save(quizEntity);
    }

}
