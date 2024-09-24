package com.example.vocatest.controllerDocs;

import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "유저", description = "유저 관련 API")
public interface UserControllerDocs {

    @Operation(summary = "모든 사용자 조회", description = "모든 사용자의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "사용자 조회 실패", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public List<UserEntity> findAllUser();

    @Parameters(value = {
            @Parameter(name = "userId", description = "유저 id 값"),
    })
    @Operation(summary = "특정 사용자 조회", description = "특정 사용자의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "사용자 조회 실패", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> findUserById(@PathVariable("userId")Long userId);

    @Parameters(value = {
            @Parameter(name = "userId", description = "유저 id 값"),
    })
    @Operation(summary = "특정 사용자 삭제", description = "특정 사용자의 정보를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 삭제 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "사용자 삭제 실패", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("{userId}")
    public ResponseEntity<UserEntity> delete(@PathVariable("userId")Long userId);

//    @Operation(summary = "로그아웃", description = "로그아웃")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(mediaType = "application/json")),
//            @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = @Content(mediaType = "application/json"))
//    })
//    @GetMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response);

    @Operation(summary = "유저정보 받아오기", description = "로그인한 유저정보 받아오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저정보 받아오기 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유저정보 받아오기 실패", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<UserEntity> getMyUserData(@AuthenticationPrincipal CustomOAuth2User customOAuth2User);
}