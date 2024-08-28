package com.example.vocatest.controllerDocs;

import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.entity.UserVocaListEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "사용자의 단어장", description = "사용자의 단어장 관련 API")
public interface UserVocaListControllerDocs {
    @Operation(summary = "사용자의 단어장 조회", description = "로그인 되어 있는 사용자가 가지고 있는 단어장을 모두 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어장 조회 성공"),
            @ApiResponse(responseCode = "400", description = "단어장 조회 실패")
    })

    @GetMapping()
    public List<UserVocaListEntity> findUserVocaList(@AuthenticationPrincipal OAuth2User OAuth2User);

    @Parameters(value = {
            @Parameter(name = "id", description = "단어장 id 값"),
    })
    @Operation(summary = "단어장 받아오기", description = "공개 되어있는 다른 사용자의 단어장을 받아옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어장 받아오기 성공"),
            @ApiResponse(responseCode = "400", description = "단어장 받아오기 실패")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserVocaListEntity> bringUserVocaList(@AuthenticationPrincipal OAuth2User OAuth2User, @PathVariable("id")Long id);


    @Parameters(value = {
            @Parameter(name = "id", description = "단어장 id 값"),
    })
    @Operation(summary = "사용자가 가지고 있는 특정 단어장 삭제", description = "사용자가 가지고 있는 특정 단어장을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어장 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "단어장 삭제 실패")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserVocaList(@AuthenticationPrincipal OAuth2User OAuth2User, @PathVariable("id")Long id);
}
