package com.studyclub.modules.study;

import com.studyclub.modules.account.Account;
import com.studyclub.modules.study.Study;
import com.studyclub.modules.study.StudyRepository;
import com.studyclub.modules.study.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudyFactory {

    @Autowired
    StudyService studyService;
    @Autowired
    StudyRepository studyRepository;

    public Study createStudy(String path, Account manager) {
        Study study = new Study();
        study.setPath(path);
        studyService.createNewStudy(study, manager);
        return study;
    }

}
