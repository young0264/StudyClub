package com.studyclub.settings;

import com.studyclub.WithAccount;
import com.studyclub.account.AccountRepository;
import com.studyclub.account.AccountService;
import com.studyclub.account.SignUpForm;
import com.studyclub.domain.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired AccountService accountService;

    @Autowired AccountRepository accountRepository;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @WithAccount("young")
    @DisplayName("프로필 페이지 이동")
    @Test
    void show_form() throws Exception {
        String bio = "짧은 소개를 수정하는 경우?";
        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE));
    }

    /**
     * 데이타 넣기 전에 before이 실행이 되기 위해(WithUserDetail이 먼저 실행) setupBefore
     */
//    @WithUserDetails(value = "young", setupBefore = TestExecutionEvent.TEST_EXECUTION)
//    @WithSecurityContext()
    @WithAccount("young")
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
    @WithAccount("young")
    @DisplayName("프로필 수정하기 - 입력값 정상이 아닐 때")
    @Test
    void updateProfile_error() throws Exception {
        String bio = "길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우?길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우?";

        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE)
                        .param("bio", bio)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account account = accountRepository.findByNickname("young");
        assertNull(account.getBio());
    }

    @WithAccount("young")
    @DisplayName("패스워드 수정")
    @Test
    void updatePassword_form() throws Exception {
        mockMvc.perform(get("/settings/password")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(view().name(SettingsController.SETTINGS_PASSWORD_VIEW_NAME));
    }



    @WithAccount("young")
    @DisplayName("패스워드 수정-입력정상")
    @Test
    void updatePassword_success() throws Exception {

    }

    @WithAccount("young")
    @DisplayName("패스워드 수정-입력에러")
    @Test
    void updatePassword_fail() throws Exception {

    }




}