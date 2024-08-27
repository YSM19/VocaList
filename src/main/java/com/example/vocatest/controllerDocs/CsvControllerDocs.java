package com.example.vocatest.controllerDocs;

import com.example.vocatest.dto.VocaListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Tag(name = "CSV", description = "CSV 관련 API")
public interface CsvControllerDocs {
    @Parameters(value = {
            @Parameter(name = "id", description = "단어장 id 값")
    })
    @Operation(summary = "CSV다운로드", description = "특정 단어장을 CSV파일로 다운로드 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "다운 성공"),
            @ApiResponse(responseCode = "400", description = "다운 실패")
    })
    public ResponseEntity<String> csvDown(HttpServletResponse response, @PathVariable("id") Long id) throws IOException;

    @Parameters(value = {
            @Parameter(name = "title", description = "단어장 제목")
    })
    @Operation(summary = "CSV불러오기", description = "CSV파일을 불러와 단어장에 등록합니다. <br><br> 경로: D:/vocatest/단어.csv <br><br> 필요 파라미터 : 단어장 제목")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "등록 실패")
    })
    public ResponseEntity<String> readCsv(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody VocaListDto vocaListDto);
}
