package com.example.vocatest.service;

import com.example.vocatest.dto.VocaContentDto;
import com.example.vocatest.dto.VocaListDto;
import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.entity.UserVocaListEntity;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.repository.UserRepository;
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
    private final UserRepository userRepository;

    //    --------------------------단어장-------------------------
    // create
    public VocaListEntity createVocaList(String email, VocaListDto vocaListDto){
        UserEntity user = userService.findUserByEmail(email);

        // VocaList 생성
        VocaListEntity vocaListEntity = vocaListDto.toEntity(email, user.getName());
        saveVocaList(vocaListEntity);

        // UserVocaList 생성
        UserEntity userEntity = userService.findUserByEmail(email);
        saveUserVocaList(vocaListEntity, userEntity);

        return vocaListEntity;
    }


    public VocaListEntity saveVocaList(VocaListEntity vocaListEntity){
        return vocaListRepository.save(vocaListEntity);
    }

    // read
//    public List<VocaListEntity> findAllVocaList(){
//        return vocaListRepository.findAll();
//    }

    public List<VocaListEntity> findSecretVocaList(int secret){
        return vocaListRepository.findBySecret(secret);
    }

    public VocaListEntity findVocaListById(Long vocalistId){
            return vocaListRepository.findById(vocalistId).orElse(null);
    }

    public List<VocaListEntity> findVocaListByEmail(String email){
        return vocaListRepository.findByEmail(email);
    }

    // update
    public VocaListEntity updateVocaListById(Long id, VocaListDto vocaListDto, String email){
        VocaListEntity vocaListEntity = findVocaListById(id);
        if (vocaListEntity != null) {
            vocaListEntity.setEmail(email);
            vocaListEntity.setTitle(vocaListDto.getTitle());

            return vocaListRepository.save(vocaListEntity);
        }
        return null;
    }

    // delete
    public void deleteVocaList(VocaListEntity vocaListEntity){
        vocaListRepository.delete(vocaListEntity);
    }

    // 가져간 단어장 count 증가
    public void addCount(Long uservocalistId) {
        UserVocaListEntity userVocaListEntity = userVocaListRepository.findById(uservocalistId).orElse(null);
        Long vocalistId = userVocaListEntity.getVocaListEntity().getId();

        VocaListEntity vocaListEntity = findVocaListById(vocalistId);
        if (vocaListEntity != null) {
            vocaListEntity.setCount(vocaListEntity.getCount() + 1);
        }
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
//    @Transactional(readOnly = true)
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
        target.setSampleSentence(vocaContentDto.getSampleSentence());
        return vocaContentRepository.save(target);
    }

    // delete
    public void deleteVocaContent(VocaContentEntity vocaContentEntity){
        vocaContentRepository.delete(vocaContentEntity);
    }

    public void deleteAllVocaContentByVocaListId(Long vocaListId){
        List<VocaContentEntity> vocaContentEntityList = vocaContentRepository.findByVocaListEntityId(vocaListId);
        vocaContentRepository.deleteAll(vocaContentEntityList);
    }

    // save
    public VocaContentEntity saveVocaContent(VocaContentEntity vocaContentEntity){
        return vocaContentRepository.save(vocaContentEntity);
    }

    //--------------------유저가 보유한 단어장 처리 메소드--------------------------

    // create
    public UserVocaListEntity createUserVocaList(String userEmail, Long vocalistId) {
        UserEntity user = userService.findUserByEmail(userEmail);
        // 단어장 생성
        VocaListEntity originalVocaListEntity = findVocaListById(vocalistId);
        VocaListEntity newVocaListEntity = VocaListDto.createVocaListToEntity(originalVocaListEntity, userEmail, user.getName());
        vocaListRepository.save(newVocaListEntity);

        // 단어장에 들어가야할 단어 리스트
        List<VocaContentEntity> selectedAllVocaContent = vocaContentRepository.findByVocaListEntityId(vocalistId);
        for (VocaContentEntity vocaContentEntity : selectedAllVocaContent) {
            VocaContentEntity newVocaContent = new VocaContentEntity();
            newVocaContent.setText(vocaContentEntity.getText());
            newVocaContent.setTranstext(vocaContentEntity.getTranstext());
            newVocaContent.setSampleSentence(vocaContentEntity.getSampleSentence());
            newVocaContent.setVocaListEntity(newVocaListEntity);

            vocaContentRepository.save(newVocaContent);
        }

        // UserVocaList 생성
        UserEntity userEntity = userService.findUserByEmail(userEmail);

        return saveUserVocaList(originalVocaListEntity, userEntity);
    }

    public UserVocaListEntity saveUserVocaList(UserVocaListEntity userVocaListEntity){
        return userVocaListRepository.save(userVocaListEntity);
    }

    public UserVocaListEntity saveUserVocaList(VocaListEntity vocaListEntity, UserEntity userEntity){
        UserVocaListEntity userVocaListEntity = new UserVocaListEntity();
        userVocaListEntity.setUserEntity(userEntity);
        userVocaListEntity.setVocaListEntity(vocaListEntity);
        return userVocaListRepository.save(userVocaListEntity);
    }

    // read
//    @Transactional
    public List<UserVocaListEntity> getUserVocaList(String userEmail){
        return userVocaListRepository.findByUserEntityEmail(userEmail);
    }

//    public UserVocaListEntity findUserVocaListById(Long uservocalistId){
//        return userVocaListRepository.findById(uservocalistId);
//    }

//    public UserVocaListEntity getUserVocaListId(Long title){
//        return userVocaListRepository.findByVocaListEntityId(title);
//    }

    // delete
    public void deleteUserVocaList(Long id){
        UserVocaListEntity target = userVocaListRepository.findByVocaListEntityId(id);

        if (target != null) {
            userVocaListRepository.delete(target);
        }

    }

    public void deleteAllUserVocaListByvocalistId(Long vocalistId){
        List<UserVocaListEntity> target = userVocaListRepository.findAllByVocaListEntity_Id(vocalistId);
        if (target != null) {
            userVocaListRepository.deleteAll(target);
        }
    }

    public void deleteAllUserVocaListByuserId(Long userId){
        List<UserVocaListEntity> target = userVocaListRepository.findAllByUserEntity_Id(userId);
        if (target != null) {
            userVocaListRepository.deleteAll(target);
        }
    }

}
