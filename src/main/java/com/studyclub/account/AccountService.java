package com.studyclub.account;


import com.studyclub.domain.Account;
import com.studyclub.settings.Notifications;
import com.studyclub.settings.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(newAccount);
        //        ConsoleMailSender consoleMailSender = new ConsoleMailSender();
        //        consoleMailSender.send(simpleMailMessage);
        return newAccount;
    }

    /**
     * 캡슐화
     */
    private Account saveAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword())) //TODO
                .email(signUpForm.getEmail())
                .emailCheckTokenGeneratedAt(LocalDateTime.now())
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();
        return accountRepository.save(account);
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(newAccount.getEmail());
        simpleMailMessage.setSubject("스터디클럽, 회원 가입 인증");
        simpleMailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

    public void login(Account account) {
        //방법1
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),  //encoded password
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);


        // 방법2- 정석 //이걸 하려면 plain password를 써야해., authenticationManager bean에 등록되어있지 않음
//        UsernamePasswordAuthenticationToken token2 = new UsernamePasswordAuthenticationToken(account.getNickname(), account.getPassword());
//        Authentication authentication = authenticationManager.authenticate(token2);
//        SecurityContext context2 = SecurityContextHolder.getContext();
//        context2.setAuthentication(authentication);
    }


    //UserDetailsService가 bean으로만 등록되어 잇으면 spring security에서 사용함
    // nickname, email 둘 다 unique true이기 때문에 검증이 됨
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if (account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }

        if (account == null) {
            throw new UsernameNotFoundException(emailOrNickname);
        }
        return new UserAccount(account);

    }

    public void completeSignUp(Account account) {
        account.completeSignUp();
        login(account);
    }


    public void updateProfile(Account account, Profile profile) {
        //TODO 프로필이미지
        modelMapper.map(profile, account); //source를 destination으로 mapping
        accountRepository.save(account);
        //TODO 문제한개더
    }

    public void updatePassword(Account account, String newPassword) {
        account.updatePassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void updateNotifications(Account account, Notifications notifications) {
        modelMapper.map(notifications, account);
        accountRepository.save(account);
    }
}
