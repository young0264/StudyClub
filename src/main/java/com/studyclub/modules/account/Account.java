package com.studyclub.modules.account;

import com.studyclub.modules.study.Study;
import com.studyclub.modules.tag.Tag;
import com.studyclub.modules.zone.Zone;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter @Builder
@EqualsAndHashCode(of = "id") //id값에 대해서만 equals와 hash값 참조 (무한루프 방지)
@NoArgsConstructor @AllArgsConstructor
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

    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt; //가입한 시간

    private String bio; //프로필

    private String url;

    private String occupation; //직업

    private String location; //살고있는 지역 //varchar(255)

    //varchar(255) image같은경우 255넘어갈 수 있음
    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage; //프로필 이미지

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb = true;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb = true;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb = true;

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        if (this.emailCheckToken.equals(token)) {
            return true;
        }
        return false;
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1)); //1시간 이전에 만들었는지
    }


    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

}
