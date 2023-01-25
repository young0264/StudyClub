package com.studyclub.modules.settings.form;


import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

public class Notifications {

    private boolean studyCreatedByEmail;
    private boolean studyCreatedByWeb;
    private boolean studyEnrollmentResultByEmail;
    private boolean studyEnrollmentResultByWeb;
    private boolean studyUpdatedByEmail;
    private boolean studyUpdatedByWeb;

}
