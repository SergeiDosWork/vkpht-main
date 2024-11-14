package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.notification.domain.dao.NotificationLogEmployeeDao;
import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEmployeeEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEntity;
import me.goodt.vkpht.module.notification.api.NotificationLogEmployeeService;

@Service
@RequiredArgsConstructor
public class NotificationLogEmployeeServiceImpl implements NotificationLogEmployeeService {

    private final NotificationLogEmployeeDao notificationLogEmployeeDao;

    @Override
    @Transactional
    public void create(NotificationLogEntity notificationLog, Map<Long, Boolean> employeeIdIsCopy) {
        if (!employeeIdIsCopy.isEmpty()) {
            List<NotificationLogEmployeeEntity> entityList = new ArrayList<>();

            employeeIdIsCopy.forEach((employeeId, isCopy) -> {
                NotificationLogEmployeeEntity entity = new NotificationLogEmployeeEntity();

                entity.setNotificationLog(notificationLog);
                entity.setEmployeeId(employeeId);
                entity.setIsCopy(isCopy);

                entityList.add(entity);
            });

            notificationLogEmployeeDao.saveAll(entityList);
        }
    }

    @Override
    public List<Long> findEmployeeIdByNotificationLogId(Long notificationLogId, boolean isCopy) {
        return notificationLogEmployeeDao.loadEmployeesByNotificationLogId(notificationLogId, isCopy);
    }
}
