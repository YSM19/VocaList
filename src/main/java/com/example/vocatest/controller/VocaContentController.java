package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.VocaContentControllerDocs;
import com.example.vocatest.dto.VocaContentDto;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.service.VocaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vocacontent")
public class VocaContentController implements VocaContentControllerDocs { // 단어

    private final VocaService vocaService;

    // create
    @PostMapping("/create/{vocalistId}")
    public ResponseEntity<VocaContentEntity> addVocaContent(@PathVariable("vocalistId") Long vocalistId,
                                                            @RequestBody VocaContentDto vocaContentDto){ // 단어장에 단어 등록

        VocaContentEntity vocaContentEntity = vocaService.createVocaContent(vocalistId, vocaContentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vocaContentEntity);
    }

    // read
    @GetMapping("/showall/{vocalistId}")
    public ResponseEntity<List<VocaContentEntity>> getAllVocaContentByVocaListId(@PathVariable("vocalistId") Long vocalistId) { //단어장에 있는 모든 단어 조회
        List<VocaContentEntity> vocas = vocaService.findAllVocasByVocaListId(vocalistId);
        return ResponseEntity.ok().body(vocas);
    }

    @GetMapping("/show/{wordid}")// 특정 단어 조회
    public ResponseEntity<VocaContentEntity> showVocaContent(@PathVariable("wordid") Long wordid){
        VocaContentEntity target = vocaService.getVocaContentId(wordid);
        if (target == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(target);
    }

    //update
    @PatchMapping("/modify/{vocalistId}/{wordid}") //단어수정
    public ResponseEntity<VocaContentEntity> updateVocaContent(@PathVariable("vocalistId")Long vocalistId,
                                                               @PathVariable("wordid") Long wordid,
                                                               @RequestBody VocaContentDto vocaContentDto){

        VocaContentEntity updated = vocaService.updateVocaContent(vocalistId, wordid, vocaContentDto);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    //delete
    @DeleteMapping("/delete/{vocalistId}/{wordid}")//단어 삭제
    public ResponseEntity<VocaContentEntity> deleteVocaContent(@PathVariable("vocalistId")Long vocalistId, @PathVariable("wordid")Long wordid){
        VocaContentEntity target = vocaService.getVocaContentId(wordid);

        if(target == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        vocaService.deleteVocaContent(target);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
