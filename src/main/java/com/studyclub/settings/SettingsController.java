package com.studyclub.settings;


import com.studyclub.account.AccountService;
import com.studyclub.account.CurrentUser;
import com.studyclub.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SettingsController {

    static final String SETTINGS_PROFILE = "/settings/profile";
    private final AccountService accountService;

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new Profile(account));
//        model.addAttribute("bio", account.getBio());
        return SETTINGS_PROFILE;
    }

    @PostMapping("/settings/profile") //이 account는 영속상태가 아님, model 기본 등록
    public String updateProfile(@CurrentUser Account account, @Valid Profile profile,
                                Errors errors, Model model, RedirectAttributes attributes) {
        log.info("errors : " + errors);
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PROFILE;
        }
        log.info("update profile : " + profile.getBio());
        log.info("update profile : " + profile.getUrl());
        log.info("update profile : " + profile.getLocation());
        log.info("update profile : " + profile.getOccupation());
        accountService.updateProfile(account, profile);
        attributes.addFlashAttribute("message", "프로필을 수정했습니다.");
        return "redirect:" + SETTINGS_PROFILE;
    }


}
