package com.studyclub.study;


import com.studyclub.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepositody extends JpaRepository<Study, Long> {
}
