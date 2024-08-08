package com.example.vocatest.controller;

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
public class VocaContentController { // 단어

    private final VocaService vocaService;

    // create
    @PostMapping("/{id}/word")
    public ResponseEntity<VocaContentEntity> addVocaContent(@PathVariable("id") Long id,
                                                            @RequestBody VocaContentDto vocaContentDto){ // 단어장에 단어 등록

        VocaContentEntity vocaContentEntity = vocaService.createVocaContent(id, vocaContentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vocaContentEntity);
    }

    // read
    @GetMapping("{id}/word")
    public ResponseEntity<List<VocaContentEntity>> getAllVocaContentByVocaListId(@PathVariable("id") Long id) { //단어장에 있는 모든 단어 조회
        List<VocaContentEntity> vocas = vocaService.findAllVocasByVocaListId(id);
        return ResponseEntity.ok().body(vocas);
    }

    //update
    @PatchMapping("/{id}/word/{wordid}")//단어수정
    public ResponseEntity<VocaContentEntity> updateVocaContent(@PathVariable("id")Long id,
                                                               @PathVariable("wordid") Long wordid,
                                                               @RequestBody VocaContentDto vocaContentDto){

        VocaContentEntity updated = vocaService.updateVocaContent(id, wordid, vocaContentDto);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    //delete
    @DeleteMapping("{id}/word/{wordid}")//단어 삭제
    public ResponseEntity<VocaContentEntity> deleteVocaContent(@PathVariable("id")Long id, @PathVariable("wordid")Long wordid){
        VocaContentEntity target = vocaService.getVocaContentId(wordid);

        if(target == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        vocaService.deleteVocaContent(target);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
