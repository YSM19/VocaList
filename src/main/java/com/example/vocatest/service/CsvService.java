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
        listStrings.add(new String[]{"text", "transtext", "voca_id"});
        for (VocaContentEntity vocaContentEntity : list){
            String[] rowData = new String[3];
            rowData[0] = vocaContentEntity.getText();
            rowData[1] = vocaContentEntity.getTranstext();
            rowData[2] = vocaContentEntity.getVocaListEntity().getTitle();
            listStrings.add(rowData);
        }
        return listStrings;
    }

}
