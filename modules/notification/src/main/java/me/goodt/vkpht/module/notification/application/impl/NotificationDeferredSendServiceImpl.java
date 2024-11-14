package me.goodt.vkpht.module.notification.application.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.module.notification.application.NotificationDeferredSendService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import me.goodt.vkpht.module.notification.domain.dao.NotificationDeferredSendDao;
import me.goodt.vkpht.module.notification.api.dto.data.NotificationLogStatusEnum;
import me.goodt.vkpht.module.notification.api.dto.kafka.NoticeDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationDeferredSendEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEntity;
import me.goodt.vkpht.module.notification.api.NotificationLogService;
import me.goodt.vkpht.module.notification.api.kafka.KafkaService;

@Slf4j
@Service
public class NotificationDeferredSendServiceImpl implements NotificationDeferredSendService {

    private final NotificationDeferredSendDao dao;
    private final NotificationLogService notificationLogService;
    private final KafkaService kafkaService;

    private final ObjectMapper mapper;

    public NotificationDeferredSendServiceImpl(NotificationDeferredSendDao dao,
                                               NotificationLogService notificationLogService,
                                               @Lazy KafkaService kafkaService) {
        this.dao = dao;
        this.notificationLogService = notificationLogService;
        this.kafkaService = kafkaService;
        this.mapper = new ObjectMapper();
    }

    @Override
    @Transactional
    public void create(NotificationLogEntity notificationLogEntity, String noticeBodyJson) {
        NotificationDeferredSendEntity entity = new NotificationDeferredSendEntity();
        entity.setNotificationLog(notificationLogEntity);
        entity.setNoticeBodyJson(noticeBodyJson);
        entity.setDateFrom(new Date());
        dao.save(entity);
    }

    @Override
    @Transactional
    public NotificationDeferredSendEntity save(NotificationDeferredSendEntity entity) {
        return dao.save(entity);
    }

    @Transactional(readOnly = true)
    public NotificationDeferredSendEntity findByNotificationLogId(Long notificationLogId) {
        return dao.findById(notificationLogId).orElseGet(() -> null);
    }

    @Override
    @Transactional
    public void startDeferredSendNotification() {
        List<Long> notificationLogIds = dao.findAllNotificationLogId();
        log.info("Количество отложенных уведомлений: {}", notificationLogIds.size());
        if (CollectionUtils.isNotEmpty(notificationLogIds)) {
            int countSend = 0;
            for (Long id : notificationLogIds) {
                NotificationDeferredSendEntity entity = findByNotificationLogId(id);
                NoticeDto noticeDto = null;
                try {
                    noticeDto = mapper.readValue(entity.getNoticeBodyJson(), NoticeDto.class);
                    dao.delete(entity);
                } catch (Exception e) {
                    continue;
                }

                if (kafkaService.kafkaTemplateSend(noticeDto)) {
                    countSend++;
                    notificationLogService.updateStatus(entity.getNotificationLog(), NotificationLogStatusEnum.SUCCESS);
                }
            }

            log.info("Количество отправленных отложенных уведомлений: {} из {}", countSend, notificationLogIds.size());
        }
    }
}
