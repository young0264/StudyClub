package com.studyclub.modules.account;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyclub.modules.account.form.NicknameForm;
import com.studyclub.modules.account.form.Notifications;
import com.studyclub.modules.account.form.PasswordForm;
import com.studyclub.modules.account.form.Profile;
import com.studyclub.modules.account.validator.NicknameValidator;
import com.studyclub.modules.tag.Tag;
import com.studyclub.modules.zone.Zone;
import com.studyclub.modules.account.validator.PasswordFormValidator;
import com.studyclub.modules.tag.TagForm;
import com.studyclub.modules.tag.TagRepository;
import com.studyclub.modules.tag.TagService;
import com.studyclub.modules.zone.ZoneForm;
import com.studyclub.modules.zone.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SettingsController {

    private final TagRepository tagRepository;

    static final String SETTINGS_PROFILE = "/settings/profile";
    static final String SETTINGS_PASSWORD_VIEW_NAME = "settings/password";
    static final String SETTINGS_PASSWORD_URL = "/settings/password";
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final NicknameValidator nicknameValidator;
    static final String SETTINGS_TAGS_VIEW_NAME = "settings/tags";
    static final String SETTINGS_TAGS_URL = "/" + SETTINGS_TAGS_VIEW_NAME;
    private final ObjectMapper objectMapper;
    private final ZoneRepository zoneRepository;

    private final TagService tagService;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameValidator);
    }

    @GetMapping("/settings/profile")
    public String updateProfileForm(@CurrentAccount Account account, Model model) {

        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));
        return SETTINGS_PROFILE;
    }

    @PostMapping("/settings/profile") //??? account??? ??????????????? ??????, model ?????? ??????
    public String updateProfile(@CurrentAccount Account account, @Valid Profile profile,
                                Errors errors, Model model, RedirectAttributes attributes) {
        log.info("errors : " + errors);
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PROFILE;
        }

        accountService.updateProfile(account, profile);
        attributes.addFlashAttribute("message", "???????????? ??????????????????.");
        return "redirect:" + SETTINGS_PROFILE;
    }

    @GetMapping(SETTINGS_PASSWORD_URL)
    public String passwordUpdateForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return SETTINGS_PASSWORD_VIEW_NAME;
    }

    @PostMapping("/settings/password")
    public String updatePassword(@CurrentAccount Account account, @Valid PasswordForm passwordForm, Errors errors, Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PASSWORD_VIEW_NAME;
        }
        accountService.updatePassword(account, passwordForm.getNewPassword());
        attributes.addFlashAttribute("message", "??????????????? ??????????????????.");
        return "redirect:" + SETTINGS_PASSWORD_URL;
    }

    @GetMapping("/settings/notifications")
    public String updateNotificationsForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Notifications.class)); //account??? ???????????? notifications ?????? ???????????? & post??? ????????????
        return "/settings/notifications";
    }

    @PostMapping("/settings/notifications")
    public String updateNotifications(@CurrentAccount Account account, Model model, @Valid Notifications notifications, Errors errors, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "/settings/notifications";
        }
        accountService.updateNotifications(account, notifications);
        attributes.addFlashAttribute("message", "?????? ????????? ?????????????????????.");
        return "redirect:/settings/notifications";
    }

    @GetMapping("/settings/account")
    public String updateAccountForm(@CurrentAccount Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute(modelMapper.map(account, NicknameForm.class));
        return "/settings/account";
    }

    @PostMapping("/settings/account")
    public String updateAccount(@CurrentAccount Account account, Model model, @Valid NicknameForm nicknameForm, Errors errors, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "/settings/account";
        }
        accountService.updateNickname(account, nicknameForm.getNickname());
        attributes.addFlashAttribute("message", "????????? ????????? ??????????????????");
        return "redirect:/settings/account";
    }

    @GetMapping(SETTINGS_TAGS_URL)
    public String updateTags(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        Set<Tag> tags = accountService.getTags(account);
        model.addAttribute("tags", tags.stream().map(Tag::getTitle).collect(Collectors.toList())); //???????????? ????????? ????????????

        List<String> allTags = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));

        return SETTINGS_TAGS_VIEW_NAME;
    }

    @PostMapping(SETTINGS_TAGS_URL + "/add")
    @ResponseBody
    public ResponseEntity addTag(@CurrentAccount Account account, @RequestBody TagForm tagForm) {
        Tag tag = tagService.findOrCreateNew(tagForm.getTagTitle());
        accountService.addTag(account, tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping(SETTINGS_TAGS_URL + "/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentAccount Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title);
        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }
        accountService.removeTag(account, tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/zones")
    public String updateZonesForm(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
//        Set<Zone> zones = account.getZones();
        Set<Zone> zones = accountService.getZones(account);
        model.addAttribute("zones", zones.stream().map(Zone::toString).collect(Collectors.toList()));

        List<String> allZone = zoneRepository.findAll().stream().map(Zone::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZone));

        return "/settings/zones";
    }

    @PostMapping("/zones/add")
    @ResponseBody
    public ResponseEntity addZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm) {
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }
        accountService.addZone(account, zone);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/zones/remove")
    @ResponseBody
    public ResponseEntity removeZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm) {
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }
        accountService.removeZone(account, zone);
        return ResponseEntity.ok().build();
    }
}
