package com.example.vocatest.service;

import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.repository.VocaContentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {
    private final VocaContentRepository vocaContentRepository;

    public CsvService(VocaContentRepository vocaContentRepository){
        this.vocaContentRepository = vocaContentRepository;
    }

    public List<String[]> listVocaContentString(Long id){
        List<VocaContentEntity> list = vocaContentRepository.findByVocaListEntityId(id);
        List<String[]> listStrings = new ArrayList<>();
        listStrings.add(new String[]{"text", "transtext","exsentence","voca_id"});// 첫 배열엔 이거 생성
        for (VocaContentEntity vocaContentEntity : list){ //list 만큼 돌려서 넣을거야. vocaContentEntity 타입을
            String[] rowData = new String[4];
            rowData[0] = vocaContentEntity.getText();
            rowData[1] = vocaContentEntity.getTranstext();
            rowData[2] = vocaContentEntity.getSampleSentence();
            rowData[3] = vocaContentEntity.getVocaListEntity().getTitle();
            listStrings.add(rowData);
        }

        return listStrings;
    }

}
