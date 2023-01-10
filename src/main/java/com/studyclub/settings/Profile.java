package com.studyclub.settings;

import com.studyclub.domain.Account;
import lombok.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;

@NoArgsConstructor
//@Data
//@ComponentScan
@Getter @Setter
public class Profile {

//    Account account;

    private String bio; //프로필

    private String url;

    private String occupation; //직업

    private String location; //살고있는 지역 //varchar(255)

    public Profile(Account account) {
        this.bio = account.getBio();
    }
}
