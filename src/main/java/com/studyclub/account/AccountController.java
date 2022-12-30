package com.studyclub.account;


import com.studyclub.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AccountController {

    //    @Autowired
    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;
    private final AccountRepository accountRepository; // repository를 도메인(Account)레벨과 같이 보고 진행

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

    /**
     * 회원가입
     */
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

    /**
     * 회원가입 : 인증 이메일 확인
     */
    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);


        //error 하나로만 통일시켜서 뿌려주기
        String view = "account/checked-email";
        if (account==null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }
        if (!account.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }
        account.setEmailVerified(true);
        account.setJoinedAt(LocalDateTime.now());
        model.addAttribute("numberOfUser", accountRepository.count()); //유저 넘버
        model.addAttribute("nickname", account.getNickname());
        return view;

    }


//    @GetMapping("/check-email")
//    public String checkEmail() {
//
//    }
}




