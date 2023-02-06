package com.studyclub.modules.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class NotificationsServcie {
    private final NotificationRepository notificationRepository;
    public void markAfterRead(List<Notification> notifications) {
        notifications.forEach(noti -> noti.setChecked(true));
        notificationRepository.saveAll(notifications);
    }
}
