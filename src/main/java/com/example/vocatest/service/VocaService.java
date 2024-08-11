package com.example.vocatest.service;

import com.example.vocatest.dto.VocaContentDto;
import com.example.vocatest.dto.VocaListDto;
import com.example.vocatest.entity.UserVocaListEntity;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.repository.UserVocaListRepository;
import com.example.vocatest.repository.VocaContentRepository;
import com.example.vocatest.repository.VocaListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VocaService {

    private final VocaListRepository vocaListRepository;
    private final VocaContentRepository vocaContentRepository;
    private final UserVocaListRepository userVocaListRepository;
    private final UserService userService;

    //    --------------------------단어장-------------------------
    // create
    public VocaListEntity createVocaList (String email, VocaListDto vocaListDto){
        // VocaList 생성
        VocaListEntity vocaListEntity = vocaListDto.createVocalistToEntity(email);
        vocaListRepository.save(vocaListEntity);

        // UserVocaList 생성
        UserVocaListEntity userVocaListEntity = new UserVocaListEntity();
        userVocaListEntity.setUserEntity(userService.findUserByEmail(email));
        userVocaListEntity.setVocaListEntity(vocaListEntity);
        userVocaListRepository.save(userVocaListEntity);

        return vocaListEntity;
    }


    public VocaListEntity saveVocaList(VocaListEntity vocaListEntity){
        return vocaListRepository.save(vocaListEntity);
    }

    // read
    public List<VocaListEntity> findAllVocaList(){
        return vocaListRepository.findAll();
    }

    public List<VocaListEntity> findSecretVocaList(int secret){
        return vocaListRepository.findBySecret(secret);
    }

    public VocaListEntity findVocaListById(Long id){
            return vocaListRepository.findById(id).orElse(null);
    }

    // update
    public VocaListEntity updateVocaListById(Long id, VocaListDto vocaListDto, String email){
        VocaListEntity vocaListEntity = findVocaListById(id);
        if (vocaListEntity != null) {
            vocaListEntity.setAuthor(email);
            vocaListEntity.setTitle(vocaListDto.getTitle());

            return vocaListRepository.save(vocaListEntity);
        }
        return null;
    }

    // delete
    public void deleteVocaList(VocaListEntity vocaListEntity){ 
        vocaListRepository.delete(vocaListEntity);
    }

    //    --------------------------단어 내용 메소드------------------------
    // create
    public VocaContentEntity createVocaContent(Long vocaListId, VocaContentDto vocaContentDto){
        VocaListEntity vocaListEntity = findVocaListById(vocaListId);
        if (vocaListEntity == null) {
            throw new IllegalArgumentException("유효한 VocaList ID가 없음");
        }
        VocaContentEntity vocaContentEntity = vocaContentDto.toEntity(vocaListEntity);
        return vocaContentRepository.save(vocaContentEntity);
    }

    // read
    @Transactional(readOnly = true)
    public List<VocaContentEntity> findAllVocasByVocaListId(Long vocaListId) {
        return vocaContentRepository.findByVocaListEntityId(vocaListId);
    }

    public VocaContentEntity getVocaContentId(Long wordid){ // 단어의 번호를 가져오는 메소드
        return vocaContentRepository.findById(wordid).orElse(null);
    }

    // update
    public VocaContentEntity updateVocaContent(Long id, Long wordid, VocaContentDto vocaContentDto){
        VocaContentEntity target = getVocaContentId(wordid);
        if (target == null) {
            throw new IllegalArgumentException("유효한 Word ID가 없음");
        }
        target.setText(vocaContentDto.getText());
        target.setTranstext(vocaContentDto.getTranstext());
        return vocaContentRepository.save(target);
    }

    // delete
    public void deleteVocaContent(VocaContentEntity vocaContentEntity){
        vocaContentRepository.delete(vocaContentEntity);
    }

    //--------------------유저가 보유한 단어장 처리 메소드--------------------------

    // create
    public UserVocaListEntity saveUserVocaList(UserVocaListEntity userVocaListEntity){
        return userVocaListRepository.save(userVocaListEntity);
    }

    public UserVocaListEntity createUserVocaList(String userEmail, Long id) {

        // 단어장에 들어가야할 단어 리스트
        VocaListEntity originalVocaListEntity = vocaListRepository.findById(id).orElse(null);

        VocaListEntity createVocaListEntity = VocaListDto.createVocaListToEntity(originalVocaListEntity, userEmail);

//        VocaListEntity createVocaListEntity = new VocaListEntity();
//        createVocaListEntity.setAuthor(originalVocaListEntity.getAuthor());
//        createVocaListEntity.setTitle(originalVocaListEntity.getTitle());
//        createVocaListEntity.setCount(0L);
        vocaListRepository.save(createVocaListEntity);

        List<VocaContentEntity> selectedAllVocaContent = vocaContentRepository.findByVocaListEntityId(id);

        for (VocaContentEntity vocaContentEntity : selectedAllVocaContent) {
            VocaContentEntity createVocaContentEntity = new VocaContentEntity();
            createVocaContentEntity.setText(vocaContentEntity.getText());
            createVocaContentEntity.setTranstext(vocaContentEntity.getTranstext());
            createVocaContentEntity.setVocaListEntity(createVocaListEntity);

            vocaContentRepository.save(createVocaContentEntity);
        }

        // UserVocaList 생성
        UserVocaListEntity userVocaListEntity = new UserVocaListEntity();
        userVocaListEntity.setVocaListEntity(createVocaListEntity);
        userVocaListEntity.setUserEntity(userService.findUserByEmail(userEmail));
//        originalVocaListEntity.addCount(1L); // 불러온 count 증가
        saveVocaList(originalVocaListEntity);


        return userVocaListRepository.save(userVocaListEntity);
    }

    // read
    public List<UserVocaListEntity> getUserVocaList(String userEmail){
        return userVocaListRepository.findByUserEntityEmail(userEmail);
    }


    public UserVocaListEntity getUserVocaListId(Long title){
        return userVocaListRepository.findByVocaListEntityId(title);
    }

    // delete
    public void deleteUserVocaList(Long id){
        UserVocaListEntity target = userVocaListRepository.findByVocaListEntityId(id);

        if (target != null) {
            userVocaListRepository.delete(target);
        }

    }

    //-------------------퀴즈 관련 메소드---------------------------


}
