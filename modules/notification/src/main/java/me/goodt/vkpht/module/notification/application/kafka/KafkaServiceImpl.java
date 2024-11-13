package me.goodt.vkpht.module.notification.application.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.notification.api.NotificationLogEmployeeService;
import me.goodt.vkpht.module.notification.api.NotificationLogService;
import me.goodt.vkpht.module.notification.api.dto.data.KafkaEmployeeInfo;
import me.goodt.vkpht.module.notification.api.dto.data.NotificationLogStatusEnum;
import me.goodt.vkpht.module.notification.api.dto.kafka.NoticeDto;
import me.goodt.vkpht.module.notification.api.dto.kafka.PayloadDto;
import me.goodt.vkpht.module.notification.application.NotificationDeferredSendService;
import me.goodt.vkpht.module.notification.application.NotificationLogEmailService;
import me.goodt.vkpht.module.notification.application.NotificationRecipientEmailService;
import me.goodt.vkpht.module.notification.config.KafkaConfig;
import me.goodt.vkpht.module.notification.domain.entity.NotificationDeferredSendEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEntity;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.RECEIVER_SYSTEM_EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final NotificationLogService notificationLogService;
    private final NotificationLogEmployeeService notificationLogEmployeeService;
    private final NotificationRecipientEmailService notificationRecipientEmailService;
    private final NotificationLogEmailService notificationLogEmailService;
    private final NotificationDeferredSendService notificationDeferredSendService;
    private final KafkaConfig kafkaConfig;
    private final KafkaTemplate<Long, NoticeDto> kafkaTemplate;
    private final UnitAccessService unitAccessService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void sendKafkaMessage(
        List<Long> notificationTemplateContentIds,
        Collection<Long> employeeIds,
        Collection<Long> employeeCopyIds,
        Collection<Long> emailIds,
        Collection<Long> emailCopyIds,
        Collection<String> usersKeycloakIds,
        Collection<String> channels,
        Map<String, Object> content,
        String eventSubType
    ) {

        try {
            List<NotificationLogEntity> notificationLogList = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(notificationTemplateContentIds)) {
                for (Long ntcId : notificationTemplateContentIds) {
                    NotificationLogEntity notificationLog = null;

                    Map<Long, Boolean> employeeIdIsCopy = Collections.emptyMap();
                    if (CollectionUtils.isNotEmpty(employeeIds) || CollectionUtils.isNotEmpty(employeeCopyIds)) {
                        employeeIdIsCopy = new HashMap<>();
                        if (CollectionUtils.isNotEmpty(employeeIds)) {
                            for (Long employeeId : employeeIds) {
                                employeeIdIsCopy.put(employeeId, false);
                            }
                        }

                        if (CollectionUtils.isNotEmpty(employeeCopyIds)) {
                            for (Long employeeId : employeeCopyIds) {
                                employeeIdIsCopy.put(employeeId, true);
                            }
                        }
                    }

                    Map<String, Boolean> emailIsCopy = Collections.emptyMap();
                    if (CollectionUtils.isNotEmpty(emailIds) || CollectionUtils.isNotEmpty(emailCopyIds)) {
                        emailIsCopy = new HashMap<>();

                        if (CollectionUtils.isNotEmpty(emailIds)) {
                            List<String> emails = notificationRecipientEmailService.findEmailByEmailIds(emailIds);
                            for (String email : emails) {
                                emailIsCopy.put(email, false);
                            }
                        }

                        if (CollectionUtils.isNotEmpty(emailCopyIds)) {
                            List<String> emails = notificationRecipientEmailService.findEmailByEmailIds(emailCopyIds);
                            for (String email : emails) {
                                emailIsCopy.put(email, true);
                            }
                        }
                    }

                    if (!employeeIdIsCopy.isEmpty()) {
                        notificationLog = notificationLogService.create(ntcId, content);
                        notificationLogEmployeeService.create(notificationLog, employeeIdIsCopy);
                    }

                    if (!emailIsCopy.isEmpty()) {
                        if (Objects.isNull(notificationLog)) {
                            notificationLog = notificationLogService.create(ntcId, content);
                        }
                        notificationLogEmailService.create(notificationLog, emailIsCopy);
                    }

                    notificationLogList.add(notificationLog);
                }
            }

            NoticeDto notice = createNotice(
                notificationLogList.stream().map(nl -> nl.getId()).collect(Collectors.toList()),
                employeeIds,
                employeeCopyIds,
                emailIds,
                emailCopyIds,
                usersKeycloakIds,
                channels,
                content,
                eventSubType
            );

            kafkaTemplateSend(notice);

        } catch (Exception e) {
            log.error("An error occurred during sending notification.", e);
        }
    }

    @Override
    public void sendKafkaMessage(Collection<Long> employeeIds, Map<String, Object> content, String eventSubType) {
        try {
            NoticeDto notice = createNotice(null, employeeIds, null, null, null, null, null, content, eventSubType);
            kafkaTemplate.send(kafkaConfig.getTopic(), notice);
            log.info("sending data to kafka with topic {}", kafkaConfig.getTopic());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void sendKafkaMessage(Map<KafkaEmployeeInfo, Map<String, Object>> data, Collection<String> channels, String eventSubType) {
        try {
            data.forEach((kei, content) -> {
                NoticeDto notice = createNotice(null, Collections.singletonList(kei.getEmployeeId()), null, null, null, kei.getUsersKeycloakIds(), channels, content, eventSubType);
                kafkaTemplate.send(kafkaConfig.getTopic(), notice);
                log.info("sending data to kafka with topic {}", kafkaConfig.getTopic());
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public boolean kafkaTemplateSend(NoticeDto notice) {
        boolean isSend = false;

        Collection<Long> notificationLogIds = notice.getPayload().getNotificationLogIds();

        try {
            kafkaTemplate.send(kafkaConfig.getTopic(), notice);
            isSend = true;
            log.info("sending data to kafka with topic {}", kafkaConfig.getTopic());
        } catch (Exception e) {
            for (Long notificationLogId : notificationLogIds) {
                String noticeBodyJson = null;
                try {
                    noticeBodyJson = mapper.writeValueAsString(notice);
                } catch (Exception ex) {
                    continue;
                }
                NotificationDeferredSendEntity deferredSendEntity = notificationDeferredSendService.findByNotificationLogId(notificationLogId);
                if (Objects.isNull(deferredSendEntity)) {
                    deferredSendEntity = new NotificationDeferredSendEntity();
                    deferredSendEntity.setNoticeBodyJson(noticeBodyJson);
                    NotificationLogEntity notificationLog = notificationLogService.findById(notificationLogId);
                    deferredSendEntity.setId(notificationLog.getId());
                    deferredSendEntity.setNotificationLog(notificationLog);
                    deferredSendEntity.setDateFrom(new Date());
                    notificationDeferredSendService.save(deferredSendEntity);
                    notificationLogService.updateStatus(notificationLog, NotificationLogStatusEnum.BROKER_UNAVAILABLE);
                }
            }
        } finally {
            return isSend;
        }
    }

    private NoticeDto createNotice(
        List<Long> notificationLogIds,
        Collection<Long> employeeIds,
        Collection<Long> employeeCopyIds,
        Collection<Long> emailIds,
        Collection<Long> emailCopyIds,
        Collection<String> usersKeycloakIds,
        Collection<String> channels,
        Map<String, Object> content,
        String eventSubType
    ) {
        PayloadDto payload = new PayloadDto()
            .setEventSubtype(eventSubType == null ? kafkaConfig.getSubtype() : eventSubType)
            .setChannels(channels == null ? Collections.singletonList(RECEIVER_SYSTEM_EMAIL) : channels)
            .setNotificationLogIds(notificationLogIds)
            .setContent(content)
            .setEmployeeIds(employeeIds)
            .setEmployeeCopyIds(employeeCopyIds)
            .setEmailIds(emailIds)
            .setEmailCopyIds(emailCopyIds)
            .setUsersKeycloakIds(usersKeycloakIds)
            .setInitiatorEmployeeId(kafkaConfig.getInitiatorKeycloakId())
            .setUnitCode(unitAccessService.getCurrentUnit());

        return new NoticeDto(kafkaConfig.getType(), payload, 1L);
    }
}
