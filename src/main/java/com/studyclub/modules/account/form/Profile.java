package com.studyclub.modules.account.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
public class Profile {

    @Length(max = 35)
    private String bio; //프로필

    @Length(max = 50)
    private String url;

    @Length(max = 50)
    private String occupation; //직업

    @Length(max = 50)
    private String location; //살고있는 지역 //varchar(255)

    private String profileImage;

}
