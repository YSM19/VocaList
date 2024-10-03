package com.example.vocatest.service;

import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.dto.GoogleResponse;
import com.example.vocatest.dto.OAuth2Response;
import com.example.vocatest.dto.UserDTO;
import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 부모 클래스의 메서드를 사용하여 객체를 생성함
        OAuth2User oauth2User = super.loadUser(userRequest); 

        // 제공자
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oauth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username);

        if (existData == null) {

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setName(oAuth2Response.getName());
            userEntity.setRole("ROLE_USER");
            userRepository.save(userEntity);

//            UserDTO userDTO = new UserDTO();
//            userDTO.setName(oAuth2Response.getName());
//            userDTO.setUserName(username);
//            userDTO.setEmail(oAuth2Response.getEmail());
//            userDTO.setRole("ROLE_USER");
            UserDTO userDTO = setUserDTO(userEntity);

            return new CustomOAuth2User(userDTO);
        }
        else { 

            existData.setName(oAuth2Response.getName());
            existData.setEmail(oAuth2Response.getEmail());
            userRepository.save(existData);

            UserDTO userDTO = setUserDTO(existData);

            return new CustomOAuth2User(userDTO);
        }

    }

    private UserDTO setUserDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(userEntity.getUsername());
        userDTO.setName(userEntity.getName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setRole("ROLE_USER");

        return userDTO;
    }

    public List<UserEntity> findAllUsers(){
        return userRepository.findAll();
    }

    public UserEntity findUserById(Long id){
        return userRepository.findById(id).orElse(null);
    } 

    public void delete(UserEntity userEntity){
        userRepository.delete(userEntity);
    }

    public UserEntity findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

}