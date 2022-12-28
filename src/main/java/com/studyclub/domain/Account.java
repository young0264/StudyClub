package com.studyclub.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter @Builder
@EqualsAndHashCode(of = "id") //id값에 대해서만 equals와 hash값 참조 (무한루프 방지)
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime joinedAt; //가입한 시간

    private String bio; //프로필

    private String url;

    private String occupation; //직업

    private String location; //살고있는 지역 //varchar(255)

    //varchar(255) image같은경우 255넘어갈 수 있음
    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage; //프로필 이미지

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }
}
