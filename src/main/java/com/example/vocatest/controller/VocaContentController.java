package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.VocaContentControllerDocs;
import com.example.vocatest.dto.VocaContentDto;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.service.VocaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vocacontent")
public class VocaContentController implements VocaContentControllerDocs { // 단어

    private final VocaService vocaService;

    // create
    @PostMapping("/create/{vocalistId}")
    public ResponseEntity<VocaContentDto> addVocaContent(@PathVariable("vocalistId") Long vocalistId,
                                                            @RequestBody VocaContentDto vocaContentDto) { // 단어장에 단어 등록
        VocaContentEntity vocaContentEntity = vocaService.createVocaContent(vocalistId, vocaContentDto);
        
        VocaContentDto responseDto = new VocaContentDto();
        responseDto.setText(vocaContentEntity.getText());
        responseDto.setTranstext(vocaContentEntity.getTranstext());
        responseDto.setSampleSentence(vocaContentEntity.getSampleSentence());
        responseDto.setVocaListId(vocaContentEntity.getVocaListEntity().getId()); // Assuming you want to return the ID

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // read
    @GetMapping("/showall/{vocalistId}")
    public ResponseEntity<List<VocaContentDto>> getAllVocaContentByVocaListId(@PathVariable("vocalistId") Long vocalistId) { //단어장에 있는 모든 단어 조회
        List<VocaContentEntity> vocas = vocaService.findAllVocasByVocaListId(vocalistId);
        
        List<VocaContentDto> responseDtos = new ArrayList<>();
        for (VocaContentEntity entity : vocas) {
            VocaContentDto dto = new VocaContentDto();
            dto.setText(entity.getText());
            dto.setTranstext(entity.getTranstext());
            dto.setSampleSentence(entity.getSampleSentence());
            dto.setVocaListId(entity.getVocaListEntity().getId());
            responseDtos.add(dto);
        }

        return ResponseEntity.ok().body(responseDtos);
    }

    // 단어 전체 read
    @GetMapping("/showall/all")
    public ResponseEntity<List<VocaContentDto>> getAllVocaContents() {
        List<VocaContentEntity> allContents = vocaService.findAllContents();
        List<VocaContentDto> responseDtos = new ArrayList<>();
        for (VocaContentEntity entity : allContents) {
            VocaContentDto dto = new VocaContentDto();
            dto.setText(entity.getText());
            dto.setTranstext(entity.getTranstext());
            dto.setSampleSentence(entity.getSampleSentence());
            dto.setVocaListId(entity.getVocaListEntity().getId());
            responseDtos.add(dto);
        }
        return ResponseEntity.ok(responseDtos);
    }

//    @GetMapping("/show/{vocalistId}/{wordid}")// 특정 단어 조회
//    public ResponseEntity<VocaContentEntity> showVocaContent(@PathVariable("wordid") Long wordid){
//        VocaContentEntity target = vocaService.getVocaContentId(wordid);
//        if (target == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(target);
//    }

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
