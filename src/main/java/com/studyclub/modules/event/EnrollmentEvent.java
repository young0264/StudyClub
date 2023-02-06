package com.studyclub.modules.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EnrollmentEvent {
    protected final Enrollment enrollment;
    protected final String message;
}
