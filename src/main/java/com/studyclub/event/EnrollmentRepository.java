package com.studyclub.event;

import com.studyclub.domain.Account;
import com.studyclub.domain.Enrollment;
import com.studyclub.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByEventAndAccount(Event event, Account account);

    Enrollment findByEventAndAccount(Event event, Account account);
}
