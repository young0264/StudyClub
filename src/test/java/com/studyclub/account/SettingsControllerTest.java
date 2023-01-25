package com.studyclub.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyclub.modules.account.AccountRepository;
import com.studyclub.modules.account.AccountService;
import com.studyclub.modules.tag.TagRepository;
import com.studyclub.modules.zone.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@Transactional
//@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired ObjectMapper objectMapper;
    @Autowired TagRepository tagRepository;
    @Autowired ZoneRepository zoneRepository;

//    private Zone testZone = Zone.builder().city("test").localNameOfCity("테스트시").province("테스트주").build();
//
//    @AfterEach
//    void afterEach() {
//        accountRepository.deleteAll();
//    }
//
//    @BeforeEach
//    void beforeEach() {
//        zoneRepository.save(testZone);
//    }
//    @WithAccount("young")
//    @DisplayName("프로필 페이지 이동")
//    @Test
//    void show_form() throws Exception {
//        String bio = "짧은 소개를 수정하는 경우?";
//        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attributeExists("profile"))
//                .andExpect(view().name(SettingsController.SETTINGS_PROFILE));
//    }
//
//    /**
//     * 데이타 넣기 전에 before이 실행이 되기 위해(WithUserDetail이 먼저 실행) setupBefore
//     */
////    @WithUserDetails(value = "young", setupBefore = TestExecutionEvent.TEST_EXECUTION)
////    @WithSecurityContext()
//    @WithAccount("young")
//    @DisplayName("프로필 수정하기 - 입력값 정상일 때")
//    @Test
//    void updateProfile() throws Exception {
//        String bio = "짧은 소개를 수정하는 경우?";
//
//        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE)
//                        .param("bio", bio)
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE))
//                .andExpect(flash().attributeExists("message"));
//
//        Account account = accountRepository.findByNickname("young");
//        assertEquals(bio, account.getBio());
//    }
//
//    @WithAccount("young")
//    @DisplayName("프로필 수정하기 - 입력값 정상이 아닐 때")
//    @Test
//    void updateProfile_error() throws Exception {
//        String bio = "길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우?길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우 길게 소개를 수정하는 경우?";
//
//        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE)
//                        .param("bio", bio)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(view().name(SettingsController.SETTINGS_PROFILE))
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attributeExists("profile"))
//                .andExpect(model().hasErrors());
//
//        Account account = accountRepository.findByNickname("young");
//        assertNull(account.getBio());
//    }
//
//    @WithAccount("young")
//    @DisplayName("패스워드 수정")
//    @Test
//    void updatePassword_form() throws Exception {
//        mockMvc.perform(get("/settings/password")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attributeExists("passwordForm"))
//                .andExpect(view().name(SettingsController.SETTINGS_PASSWORD_VIEW_NAME));
//    }
//
//
//    @WithAccount("young")
//    @DisplayName("패스워드 수정-입력정상")
//    @Test
//    void updatePassword_success() throws Exception {
//        mockMvc.perform(post("/settings/password")
//                        .with(csrf())
//                        .param("newPassword", "123123123")
//                        .param("newPasswordConfirm", "123123123"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl(SettingsController.SETTINGS_PASSWORD_URL))
//                .andExpect(flash().attributeExists("message"));
//        Account young = accountRepository.findByNickname("young");
//        assertTrue(passwordEncoder.matches("123123123", young.getPassword()));
//    }
//
//    //post에서 passwordForm이 들어간단 의미는 페이지에 머물러 있는거지
//    @WithAccount("young")
//    @DisplayName("패스워드 수정-입력에러(불일치)")
//    @Test
//    void updatePassword_fail() throws Exception {
//        mockMvc.perform(post("/settings/password")
//                        .with(csrf())
//                        .param("newPassword", "123123123")
//                        .param("newPasswordConfirm", "232323232"))
//                .andExpect(status().isOk())
//                .andExpect(view().name(SettingsController.SETTINGS_PASSWORD_VIEW_NAME))
//                .andExpect(model().hasErrors())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attributeExists("passwordForm"));
//
//    }
//
//    @WithAccount("young")
//    @DisplayName("프로필 수정 폼")
//    @Test
//    void updateProfileForm() throws Exception {
//        mockMvc.perform(get("/settings/account"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attributeExists("nicknameForm"));
//    }
//
//    @WithAccount("young")
//    @DisplayName("닉네임 수정하기 - 입력값 정상")
//    @Test
//    void updateAccount_success() throws Exception {
//        String newNickname = "young2";
//        mockMvc.perform(post("/settings/account")
//                        .param("nickname", newNickname)
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/settings/account"))
//                .andExpect(flash().attributeExists("message"));
//        assertNotNull(accountRepository.findByNickname("young2"));
//    }
//
//    @WithAccount("young")
//    @DisplayName("닉네임 수정하기 - 입력값 에러")
//    @Test
//    void updateAccount_fail() throws Exception {
//        String newNickname = "_////";
//        mockMvc.perform(post("/settings/account")
//                        .param("nickname", newNickname)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("/settings/account"))
//                .andExpect(model().hasErrors())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attributeExists("nicknameForm"))
//        ;
//
//        assertNull(accountRepository.findByNickname(newNickname));
//    }
//
//    @WithAccount("young")
//    @DisplayName("태그 수정 폼")
//    @Test
//    void updateTagsForm() throws Exception {
//        mockMvc.perform(get(SettingsController.SETTINGS_TAGS_URL))
//                .andExpect(view().name(SettingsController.SETTINGS_TAGS_VIEW_NAME))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("account"))
//                .andExpect(model().attributeExists("whitelist"))
//                .andExpect(model().attributeExists("tags"));
//    }
//
//    @WithAccount("young")
//    @DisplayName("계정에 태그 추가")
//    @Test
//    @Transactional
//    void addTag() throws Exception {
//
//        TagForm tagForm = new TagForm();
//        tagForm.setTagTitle("newTag");
//        mockMvc.perform(post(SettingsController.SETTINGS_TAGS_URL + "/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(tagForm))
//                        .with(csrf()))
//                .andExpect(status().isOk());
//
//        Tag newTag = tagRepository.findByTitle("newTag");
//        assertNotNull(newTag);
//        accountRepository.findByNickname("young").getTags().contains(newTag); //account 는 detached 상태임을 인지
//    }
//
//
//    @WithAccount("young")
//    @DisplayName("계정에 태그 삭제")
//    @Test
//    @Transactional
//    void removeTag() throws Exception {
//        Tag newTag = Tag.builder().title("newTag").build();
//        Account account = accountRepository.findByNickname("young");
//        tagRepository.save(newTag);
//
//        accountService.addTag(account, newTag);
//
//        assertTrue(account.getTags().contains(newTag));
//
//
//        TagForm tagForm = new TagForm();
//        tagForm.setTagTitle("newTag");
//
//        mockMvc.perform(post(SettingsController.SETTINGS_TAGS_URL + "/remove")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(tagForm))
//                        .with(csrf()))
//                .andExpect(status().isOk());
//
//        assertFalse(account.getTags().contains(newTag));
//
//    }




}