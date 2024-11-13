package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.notification.application.NotificationLogEmailService;
import me.goodt.vkpht.module.notification.domain.dao.NotificationLogEmailDao;
import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEmailEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEntity;

@RequiredArgsConstructor
@Service
public class NotificationLogEmailServiceImpl implements NotificationLogEmailService {

    private final NotificationLogEmailDao notificationLogEmailDao;

    @Override
    @Transactional
    public void create(NotificationLogEntity notificationLog, Map<String, Boolean> emailIsCopy) {
        if (!emailIsCopy.isEmpty()) {
            List<NotificationLogEmailEntity> entityList = new ArrayList<>();

            emailIsCopy.forEach((email, isCopy) -> {
                NotificationLogEmailEntity entity = new NotificationLogEmailEntity();

                entity.setNotificationLog(notificationLog);
                entity.setEmail(email);
                entity.setIsCopy(isCopy);

                entityList.add(entity);
            });

            notificationLogEmailDao.saveAll(entityList);
        }
    }

    @Override
    public List<String> findEmailByNotificationLogId(Long notificationLogId, boolean isCopy) {
        return notificationLogEmailDao.findEmailByNotificationLogId(notificationLogId, isCopy);
    }

}
