package com.example.vocatest.config;

import com.example.vocatest.jwt.CustomSuccessHandler;
import com.example.vocatest.jwt.JwtFilter;
import com.example.vocatest.jwt.JwtUtil;
import com.example.vocatest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
//
    @Value("${config.front_url}")
    private String frontUrl;

    private final JwtUtil jwtUtil;
    private final CustomSuccessHandler customSuccessHandler;
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf((csrf) -> csrf.disable())
                .formLogin((auth) -> auth.disable())
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll())
                .httpBasic((basic) -> basic.disable());


        http
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
//              .addFilterAfter(new JwtFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);


        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(userService))
                        .successHandler(customSuccessHandler)
                );

//        // *original
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .anyRequest().permitAll());

        // *change
        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/", "/reissue").permitAll()
                        .requestMatchers("/", "/login", "/reissue",
                                "/api/vocalist/showall", "/api/vocalist/show/{vocalistId}",
                                "/api/vocacontent/showall/{vocalistId}").permitAll()
                        .anyRequest().authenticated());
        // */

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

//                        AWS 버전
                        configuration.setAllowedOrigins(Collections.singletonList(frontUrl));
//                        configuration.setAllowedOrigins(Collections.singletonList("*"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setExposedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        // 우리쪽 서버에서 보낼때
                        // change
//                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
//                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
//                        configuration.setExposedHeaders(Collections.singletonList("access"));
                        // original
//                        configuration.setExposedHeaders(Collections.singletonList("*"));
//                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        return http.build();
    }
}