package com.studyclub.account;


import com.studyclub.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    public void processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(newAccount);
        //        ConsoleMailSender consoleMailSender = new ConsoleMailSender();
        //        consoleMailSender.send(simpleMailMessage);
    }
    private Account saveAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword())) //TODO
                .email(signUpForm.getEmail())
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();
        return accountRepository.save(account);
    }
    private void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(newAccount.getEmail());
        simpleMailMessage.setSubject("스터디클럽, 회원 가입 인증");
        simpleMailMessage.setText("/check-email-token?token="+ newAccount.getEmailCheckToken()+
                "&email=" + newAccount.getEmail());
        javaMailSender.send(simpleMailMessage);
    }
}
