package com.example.vocatest.service;

import com.example.vocatest.dto.QuizDTO;
import com.example.vocatest.entity.QuizEntity;
import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final VocaService vocaService;
    private final UserService userService;

    public QuizEntity saveQuizScore(Long vocalistId, String email, QuizDTO quizDTO) {

        VocaListEntity vocaListEntity = vocaService.findVocaListById(vocalistId);
        UserEntity userEntity = userService.findUserByEmail(email);

        QuizEntity quizEntity = quizDTO.toEntity(email, vocaListEntity);
        return quizRepository.save(quizEntity);
    }

    public List<QuizEntity> showAllQuizHistoryByEmail(String email) {
        return quizRepository.findAllByEmail(email);
    }
}
