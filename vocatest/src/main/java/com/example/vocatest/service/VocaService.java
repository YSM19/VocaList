package com.example.vocatest.service;

import com.example.vocatest.entity.UserVocaListEntity;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.repository.UserVocaListRepository;
import com.example.vocatest.repository.VocaContentRepository;
import com.example.vocatest.repository.VocaListRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VocaService {

    private final VocaListRepository vocaListRepository;
    private final VocaContentRepository vocaContentRepository;
    private final UserVocaListRepository userVocaListRepository;

    public VocaService (VocaListRepository vocaListRepository, VocaContentRepository vocaContentRepository, UserVocaListRepository userVocaListRepository){
        this.vocaListRepository = vocaListRepository;
        this.vocaContentRepository = vocaContentRepository;
        this.userVocaListRepository = userVocaListRepository;
    }
// ----------------------------   생성자 -----------------------------

    public List<VocaListEntity> findAllVocaList(){
        return vocaListRepository.findAll();
    }

    public VocaListEntity saveVocaList(VocaListEntity vocaListEntity){
        return vocaListRepository.save(vocaListEntity);
    }

    public VocaListEntity findVocaListById(Long id){
            return vocaListRepository.findById(id).orElse(null);
    } 

    public void deleteVocaList(VocaListEntity vocaListEntity){ 
        vocaListRepository.delete(vocaListEntity);
    }
//    --------------------------단어장-------------------------

    @Transactional(readOnly = true)
    public List<VocaContentEntity> findAllVocasByVocaListId(Long vocaListId) {
        return vocaContentRepository.findByVocaListEntityId(vocaListId);
    }

    public VocaContentEntity saveVocaContent(VocaContentEntity vocaContentEntity){
        return vocaContentRepository.save(vocaContentEntity);
    }

    public VocaContentEntity getVocaContentId(Long wordid){ // 단어의 번호를 가져오는 메소드
        return vocaContentRepository.findById(wordid).orElse(null);
    }

    public void deleteVocaContent(VocaContentEntity vocaContentEntity){
        vocaContentRepository.delete(vocaContentEntity);
    }

//    --------------------------단어 내용 수정 메소드------------------------

    public List<UserVocaListEntity> getUserVocaList(String userEmail){
        return userVocaListRepository.findByUserEntityEmail(userEmail);
    }

    public UserVocaListEntity saveUserVocaList(UserVocaListEntity userVocaListEntity){
        return userVocaListRepository.save(userVocaListEntity);
    }

    public UserVocaListEntity getUserVocaId(Long title){
        return userVocaListRepository.findByVocaListEntityId(title);
    }

    public void deleteUserVocaList(UserVocaListEntity userVocaListEntityList){
        userVocaListRepository.delete(userVocaListEntityList);
    }

    //--------------------유저가 보유한 단어장 처리 메소드--------------------------


    //아직까지 없음. 필요한가 싶네

    //-------------------퀴즈 관련 메소드---------------------------




}
