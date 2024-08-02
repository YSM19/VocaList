package com.example.vocatest.dto;

import java.util.Map;

public interface OAuth2Response {

    String getProvider(); // 제공자 ex) google, naver

    String getProviderId(); // 제공자에서 발급해주는 아이디

    String getEmail();

    String getName();

    Map<String, Object> getAttributes();
}
