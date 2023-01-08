package com.studyclub.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    //PasswordEncoder가 bean으로만 등록되어 잇으면 spring security에서 사용함
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // PasswordEncoder를 직접 생성하는게 아닌 createDelegatingPasswordEncoder에 위임
    }

}
