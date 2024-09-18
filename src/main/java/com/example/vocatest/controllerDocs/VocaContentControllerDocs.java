package com.example.vocatest.controllerDocs;

import com.example.vocatest.dto.VocaContentDto;
import com.example.vocatest.entity.VocaContentEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "단어", description = "단어 관련 API")
public interface VocaContentControllerDocs {

    @Parameters(value = {
            @Parameter(name = "vocalistId", description = "단어장 id 값"),
    })
    @Operation(summary = "단어 조회", description = "특정 단어장에 있는 모든 단어를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어 조회 성공"),
            @ApiResponse(responseCode = "400", description = "단어 조회 실패")
    })
    @GetMapping("/showall/{vocalistId}")
    public ResponseEntity<List<VocaContentEntity>> getAllVocaContentByVocaListId(@PathVariable("vocalistId") Long vocalistId);

    @Parameters(value = {
            @Parameter(name = "vocalistId", description = "단어장 id 값"),
    })
    @Operation(summary = "단어 등록", description = "단어를 등록합니다. <br><br> 필요 파라미터 : 단어, 단어 해석")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어 등록 성공"),
            @ApiResponse(responseCode = "400", description = "단어 등록 실패")
    })
    @PostMapping("/create/{vocalistId}")
    public ResponseEntity<VocaContentEntity> addVocaContent(@PathVariable("vocalistId") Long vocalistId, @RequestBody VocaContentDto vocaContentDto);

    @Parameters(value = {
            @Parameter(name = "id", description = "단어장 id 값"),
            @Parameter(name = "wordid", description = "단어 id 값"),
    })
    @Operation(summary = "단어 수정", description = "단어를 수정합니다. <br><br> 필요 파라미터 : 단어, 단어 해석")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어 수정 성공"),
            @ApiResponse(responseCode = "400", description = "단어 수정 실패")
    })
    @PatchMapping("/modify/{vocalistId}/{wordid}")
    public ResponseEntity<VocaContentEntity> updateVocaContent(@PathVariable("vocalistId")Long vocalistId, @PathVariable("wordid") Long wordid, @RequestBody VocaContentDto vocaContentDto);

    @Parameters(value = {
            @Parameter(name = "vocalistId", description = "단어장 id 값"),
            @Parameter(name = "wordid", description = "단어 id 값"),
    })
    @Operation(summary = "단어 삭제", description = "단어를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "단어 삭제 실패")
    })
    @DeleteMapping("/delete/{vocalistId}/{wordid}")
    public ResponseEntity<VocaContentEntity> deleteVocaContent(@PathVariable("vocalistId")Long vocalistId, @PathVariable("wordid")Long wordid);

    @Parameters(value = {
            @Parameter(name = "wordid", description = "단어 id 값")
    })
    @Operation(summary = "특정 단어 조회", description = "특정 단어를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어 조회 성공"),
            @ApiResponse(responseCode = "400", description = "단어 조회 실패")
    })
    @GetMapping("/show/{wordid}")
    public ResponseEntity<VocaContentEntity> showVocaContent(@PathVariable("wordid") Long wordid);
}
