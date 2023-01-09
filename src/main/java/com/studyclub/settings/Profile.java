package com.studyclub.settings;

import com.studyclub.domain.Account;

public class Profile {

    private String bio; //프로필

    private String url;

    private String occupation; //직업

    private String location; //살고있는 지역 //varchar(255)

    public Profile(Account account) {
        this.bio = account.getBio();
    }
}
