package com.studyclub.modules.account;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

//Account 프로퍼티를 들고있을 수 있는 중간다리역할
// spring security user와 현재 user 중간다리역할
public class UserAccount extends User {
    private Account account;

    //nickname으로 리턴
    public UserAccount(Account account) {
        super(account.getNickname(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
