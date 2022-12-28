package com.studyclub.account;


import com.studyclub.ConsoleMailSender;
import com.studyclub.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

//    @Autowired
    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;
    private final JavaMailSender javaMailSender;

//        signUpFormValidator.validate(signUpForm,errors);
//        if (errors.hasErrors()) {
//            return "account/sign-up";
//        }
    //를 initBinder로
    //위에것이 직관적인데
    @InitBinder("signUpForm") //SignUpForm의 변수명이아니라 타입의 카멜케이스와 매핑
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signup(Model model) {
        model.addAttribute(new SignUpForm()); //"SignUpForm"이 model로 들어감 -> Test
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signupSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }
        accountService.processNewAccount(signUpForm);
        return "redirect:/";
    }
}
