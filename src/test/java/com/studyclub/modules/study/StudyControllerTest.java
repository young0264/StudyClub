package com.studyclub.modules.study;

import static org.junit.jupiter.api.Assertions.*;

import com.studyclub.modules.account.WithAccount;
import com.studyclub.infra.MockMvcTest;
import com.studyclub.modules.account.AccountRepository;
import com.studyclub.modules.account.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
public class StudyControllerTest  {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected StudyService studyService;
    @Autowired
    protected StudyRepository studyRepository;
    @Autowired
    protected AccountRepository accountRepository;

    @Test
    @WithAccount("young")
    @DisplayName("스터디 개설 폼 조회")
    void createStudyForm() throws Exception {
        mockMvc.perform(get("/new-study"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("studyForm"));
    }

    @Test
    @WithAccount("young")
    @DisplayName("스터디 개설 - 완료")
    void createStudy_success() throws Exception {
        mockMvc.perform(post("/new-study")
                        .param("path", "test-path")
                        .param("title", "study title")
                        .param("shortDescription", "short description of a study")
                        .param("fullDescription", "full description of a study")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/test-path"));

        Study study = studyRepository.findByPath("test-path");
        assertNotNull(study);
        Account account = accountRepository.findByNickname("young");
        assertTrue(study.getManagers().contains(account));
    }

    @Test
    @WithAccount("young")
    @DisplayName("스터디 개설 - 실패")
    void createStudy_fail() throws Exception {
        mockMvc.perform(post("/new-study")
                        .param("path", "wrong path")
                        .param("title", "study title")
                        .param("shortDescription", "short description of a study")
                        .param("fullDescription", "full description of a study")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("studyForm"))
                .andExpect(model().attributeExists("account"));

        Study study = studyRepository.findByPath("test-path");
        assertNull(study);
    }

    @Test
    @WithAccount("young")
    @DisplayName("스터디 조회")
    void viewStudy() throws Exception {
        Study study = new Study();
        study.setPath("test-path");
        study.setTitle("test study");
        study.setShortDescription("short description");
        study.setFullDescription("<p>full description</p>");

        Account keesun = accountRepository.findByNickname("keesun");
        studyService.createNewStudy(study, keesun);

        mockMvc.perform(get("/study/test-path"))
                .andExpect(view().name("study/view"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("study"));
    }

    @Test
    @WithAccount("young")
    @DisplayName("스터디 가입")
    void joinStudy() throws Exception {
        Account whiteship = createAccount("whiteship");

        Study study = createStudy("test-study", whiteship);

        mockMvc.perform(get("/study/" + study.getPath() + "/join"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/members"));

        Account keesun = accountRepository.findByNickname("young");
        assertTrue(study.getMembers().contains(keesun));
    }

    @Test
    @WithAccount("young")
    @DisplayName("스터디 탈퇴")
    void leaveStudy() throws Exception {
        Account whiteship = createAccount("whiteship");
        Study study = createStudy("test-study", whiteship);

        Account keesun = accountRepository.findByNickname("young");
        studyService.addMember(study, keesun);

        mockMvc.perform(get("/study/" + study.getPath() + "/leave"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/members"));

        assertFalse(study.getMembers().contains(keesun));
    }

    protected Study createStudy(String path, Account manager) {
        Study study = new Study();
        study.setPath(path);
        studyService.createNewStudy(study, manager);
        return study;
    }

    protected Account createAccount(String nickname) {
        Account whiteship = new Account();
        whiteship.setNickname(nickname);
        whiteship.setEmail(nickname + "@email.com");
        accountRepository.save(whiteship);
        return whiteship;
    }


}


