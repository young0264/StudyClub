package com.studyclub.modules.study.event;

import com.studyclub.modules.account.Account;
import com.studyclub.modules.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    long countByAccountAndChecked(Account account, boolean checked);



}
