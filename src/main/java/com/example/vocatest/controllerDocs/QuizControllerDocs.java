package com.example.vocatest.controllerDocs;

import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.dto.QuizDTO;
import com.example.vocatest.entity.QuizEntity;
import com.example.vocatest.entity.VocaContentEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "퀴즈", description = "퀴즈 관련 API")
public interface QuizControllerDocs {
    @Parameters(value = {
            @Parameter(name = "id", description = "단어장 id 값"),
            @Parameter(name = "quizcount", description = "퀴즈 개수"),
    })
    @Operation(summary = "퀴즈 조회", description = "단어장에 등록된 단어들을 원하는 갯수만큼 랜덤으로 조회합니다. <br><br> quizcount : 퀴즈 개수")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어 조회 성공"),
            @ApiResponse(responseCode = "400", description = "단어 조회 실패")
    })
    public List<VocaContentEntity> showRandomQuiz(@PathVariable("id") Long id, @PathVariable("quizcount") Long quizcount);

    @Parameters(value = {
            @Parameter(name = "vocalistId", description = "단어장 id 값")
    })
    @Operation(summary = "푼 퀴즈개수 저장", description = "유저가 문제 푼 개수를 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "푼 퀴즈개수 저장 성공"),
            @ApiResponse(responseCode = "400", description = "푼 퀴즈개수 저장 실패")
    })
    public ResponseEntity<QuizDTO> saveQuizScore(@PathVariable("vocalistId") Long vocalistId,
                                                    @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                    @RequestBody QuizDTO quizDTO);


    @Operation(summary = "푼 퀴즈 개수 조회", description = "유저가 푼 퀴즈 개수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "푼 퀴즈 개수 조회 성공"),
            @ApiResponse(responseCode = "400", description = "푼 퀴즈 개수 조회 실패")
    })
    public ResponseEntity<List<QuizEntity>> showHistory(@AuthenticationPrincipal CustomOAuth2User customOAuth2User);
}
