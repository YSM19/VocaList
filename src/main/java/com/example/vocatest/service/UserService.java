package com.example.vocatest.service;

import com.example.vocatest.dto.UserDto;
import com.example.vocatest.dto.GoogleResponse;
import com.example.vocatest.dto.OAuth2Response;
import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest); 
        System.out.println(oauth2User.getAttributes()); 

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); 
        OAuth2Response oAuth2Response = null;
        /*
        if (registrationId.equals("naver")) {
           
        }*/
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oauth2User.getAttributes());
    
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username); 

        String role = "ROLE_USER";
        if (existData == null) {

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setRole(role);

            userRepository.save(userEntity);
        }
        else { 

            existData.setUsername(username);
            existData.setEmail(oAuth2Response.getEmail());

            role = existData.getRole();

            userRepository.save(existData);
        }

        return new UserDto(oAuth2Response, role);
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