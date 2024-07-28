package com.example.vocatest.controller;

import com.example.vocatest.dto.VocaContentDto;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
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


    @GetMapping("{id}/word")
    public ResponseEntity<List<VocaContentEntity>> getAllVocasByVocaListId(@PathVariable("id") Long id) { //단어장에 있는 모든 단어를 보여주는 메소드
        List<VocaContentEntity> vocas = vocaService.findAllVocasByVocaListId(id);
        return ResponseEntity.ok().body(vocas);
    }

    @PostMapping("/{id}/word")
    public VocaContentEntity addWord(@PathVariable("id") Long id, @RequestBody VocaContentDto vocaContentDto){ // 단어장에 단어 등록
        VocaListEntity vocaListEntity = vocaService.findVocaListById(id);
        VocaContentEntity vocaContentEntity = vocaContentDto.toEntity(vocaListEntity);
        return vocaService.saveVocaContent(vocaContentEntity);
    }

    @PatchMapping("/{id}/word/{wordid}")//단어수정
    public ResponseEntity<VocaContentEntity> updateVocaContent(@PathVariable("id")Long id, @PathVariable("wordid") Long wordid, @RequestBody VocaContentDto vocaContentDto){
        VocaContentEntity target = vocaService.getVocaContentId(wordid);

        if (target == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        target.setText(vocaContentDto.getText());
        target.setTranstext(vocaContentDto.getTranstext());

        VocaContentEntity updated = vocaService.saveVocaContent(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

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
