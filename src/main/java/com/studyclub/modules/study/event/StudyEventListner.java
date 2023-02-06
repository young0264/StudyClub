package com.studyclub.modules.study.event;


import com.studyclub.infra.config.AppProperties;
import com.studyclub.modules.account.Account;
import com.studyclub.modules.account.AccountPredicates;
import com.studyclub.modules.account.AccountRepository;
import com.studyclub.modules.notification.Notification;
import com.studyclub.modules.notification.NotificationRepository;
import com.studyclub.modules.notification.NotificationType;
import com.studyclub.modules.study.Study;
import com.studyclub.modules.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class StudyEventListner {

    private final StudyRepository studyRepository;
    private final AccountRepository accountRepository;
//    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;

    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleStudyCreatedEvent(StudyCreatedEvent studyCreatedEvent) {
        Study study = studyRepository.findStudyWithTagsAndZonesById(studyCreatedEvent.getStudy().getId());
        Iterable<Account> accounts = accountRepository.findAll(AccountPredicates.findByTagsAndZones(study.getTags(), study.getZones()));
        accounts.forEach(account -> {
//            TODO email
//            if (account.isStudyCreatedByEmail()) {
//                sendStudyCreatedEmail(study, account);
//            }
            if (account.isStudyCreatedByWeb()) {
                createNotification(study, account, study.getShortDescription(),NotificationType.STUDY_CREATED);
            }
        });
    }

    @EventListener
    public void handleStudyUpdateEvent(StudyUpdateEvent studyUpdateEvent) {
        Study study = studyRepository.findStudyWithManagersAndMembersById(studyUpdateEvent.getStudy().getId());
        Set<Account> accounts = new HashSet<>();
        accounts.addAll(study.getManagers());
        accounts.addAll(study.getMembers());
        accounts.forEach(account -> {
            if (account.isStudyUpdatedByEmail()) {
                //TODO email
//                sendStudyCreatedEmail(study, account, studyUpdateEvent.getMessage(),
//                        "스터디올래, '" + study.getTitle() + "' 스터디에 새소식이 있습니다.");
            }
            if (account.isStudyUpdatedByWeb()) {
                //TODO noti
                createNotification(study, account, studyUpdateEvent.getMessage(), NotificationType.STUDY_UPDATED);
            }
        });
     }

    private void createNotification(Study study, Account account, String message, NotificationType notificationType) {
        //TODO create notification
        Notification notification = new Notification();
        notification.setTitle(study.getTitle());
        notification.setLink("/study/" + study.getEncodedPath());
        notification.setChecked(false);
        notification.setCreatedDateTime(LocalDateTime.now());
        notification.setMessage(message);
        notification.setAccount(account);
        notification.setNotificationType(notificationType);
        notificationRepository.save(notification);
    }

//    private void sendStudyCreatedEmail(Study study, Account account, String contextMessage, String emailSubject) {
//        //TODO 이메일전송
//        Context context = new Context();
//        context.setVariable("nickname", account.getNickname());
//        context.setVariable("link", "/study/" + study.getEncodedPath());
//        context.setVariable("linkName", study.getTitle());
//        context.setVariable("message", contextMessage);
//        context.setVariable("host", appProperties.getHost());
//        String message = templateEngine.process("mail/simple-link", context);
//
//        EmailMessage emailMessage = EmailMessage.builder()
//                .subject(emailSubject)
//                .to(account.getEmail())
//                .message(message)
//                .build();
//
//        emailService.sendEmail(emailMessage);
//    }




}
