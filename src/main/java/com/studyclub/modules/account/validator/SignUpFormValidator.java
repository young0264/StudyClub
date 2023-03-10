package com.studyclub.modules.account.validator;


import com.studyclub.modules.account.AccountRepository;
import com.studyclub.modules.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 회원가입 validate 클래스
 * Validator 인터페이스를 상속받아 supports와 validate 오버라이드
 */
@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class); //이 form dto를 validate할것
    }

    @Override
    public void validate(Object object, Errors errors) {
        //TODO email, nickname
        SignUpForm signUpForm = (SignUpForm) object;
        if (accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()},"이미 사용중인 이메일입니다.");
        }
        if (accountRepository.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname","invalid.nickname",  new Object[]{signUpForm.getNickname()},"이미 사용중인 닉네임입니다.");
        }
    }
}
