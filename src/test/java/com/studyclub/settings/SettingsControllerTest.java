package com.studyclub.settings;

import com.studyclub.account.AccountRepository;
import com.studyclub.account.AccountService;
import com.studyclub.account.SignUpForm;
import com.studyclub.domain.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {
        SignUpForm newSignUpForm = SignUpForm.builder()
                .nickname("young")
                .password("12341234")
                .email("ny2485@naver.com")
                .build();
        accountService.processNewAccount(newSignUpForm);
    }

    /**
     * 데이타 넣기 전에 before이 실행이 되기 위해(WithUserDetail이 먼저 실행) setupBefore
     */
    @WithUserDetails(value = "young", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("프로필 수정하기 - 입력값 정상일 때")
    @Test
    void updateProfile() throws Exception {
        String bio = "짧은 소개를 수정하는 경우?";

        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE)
                        .param("bio", bio)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE))
                .andExpect(flash().attributeExists("message"));

        Account account = accountRepository.findByNickname("young");
        assertEquals(bio, account.getBio());

    }
}