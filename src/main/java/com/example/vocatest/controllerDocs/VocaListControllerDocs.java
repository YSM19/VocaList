package com.example.vocatest.controllerDocs;

import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.dto.VocaListDto;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.swagger.ApiCommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "단어장", description = "단어장 관련 API")
public interface VocaListControllerDocs {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 단어장 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = VocaListEntity.class)))),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = @Content(schema = @Schema(implementation = ApiCommonResponse.class))) })
    @Operation(summary = "모든 단어장 조회", description = "모든 단어장의 리스트를 조회합니다.")
    @GetMapping
    public List<VocaListEntity> findAllVocaList();

    @Parameters(value = {
            @Parameter(name = "id", description = "단어장 id 값"),
    })
    @Operation(summary = "단어장 조회", description = "특정 단어장의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "조회 실패")
    })
    @GetMapping("{id}") // 선택한 단어장 보기
    public VocaListEntity findVocaListById(@PathVariable("id")Long id);


    @Operation(summary = "단어장 생성", description = "단어장을 생성합니다. <br><br> 필요 파라미터 : 단어장 제목")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단어장 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VocaListEntity.class))),
            @ApiResponse(responseCode = "400", description = "생성 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiCommonResponse.class))) }
    )
    public ResponseEntity<VocaListEntity> createVocaList(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody VocaListDto vocaListDto) ;

    @Parameters(value = {
            @Parameter(name = "id", description = "단어장 id 값"),
    })
    @Operation(summary = "단어장 수정", description = "단어장을 수정합니다. <br><br> 필요 파라미터 : 단어장 제목")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어장 수정 성공"),
            @ApiResponse(responseCode = "400", description = "단어장 수정 실패")
    })
    @PatchMapping("{id}") // 단어장 수정
    public ResponseEntity<VocaListEntity> updateVocaList(@PathVariable("id")Long id, @RequestBody VocaListDto vocaListDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User);


    @Parameters(value = {
            @Parameter(name = "id", description = "단어장 id 값"),
    })
    @Operation(summary = "단어장 삭제", description = "특정 단어장을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 완료"),
            @ApiResponse(responseCode = "400", description = "삭제 실패")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<VocaListEntity> deleteVocaList(@PathVariable("id")Long id);

    @Parameters(value = {
            @Parameter(name = "id", description = "단어장 id 값"),
    })
    @Operation(summary = "공개 설정", description = "특정 단어장을 다른 사용자가 볼 수 있도록 공개합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공개 설정 완료"),
            @ApiResponse(responseCode = "400", description = "공개 설정 실패")
    })
    @GetMapping("{id}/editsecret/open")
    public String openVocaListSecret(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @PathVariable("id")Long id);

    @Parameters(value = {
            @Parameter(name = "id", description = "단어장 id 값"),
    })
    @Operation(summary = "비공개 설정", description = "특정 단어장을 다른 사용자가 볼 수 없도록 비공개합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비공개 설정 완료"),
            @ApiResponse(responseCode = "400", description = "비공개 설정 실패")
    })
    @GetMapping("{id}/editsecret/close")
    public String closeVocaListSecret(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @PathVariable("id")Long id);

}
