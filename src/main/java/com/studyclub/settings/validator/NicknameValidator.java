package com.studyclub.settings.validator;


import com.studyclub.account.AccountRepository;
import com.studyclub.domain.Account;
import com.studyclub.settings.form.NicknameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm) target;
        Account byNickname = accountRepository.findByNickname(nicknameForm.getNickname()); //변경 닉네임 중복 확인
        if (byNickname != null) {
            errors.rejectValue("nmickname", "wrong.value", "입력하신 닉네임을 사용할 수 없습니다.");
        }
    }
}
