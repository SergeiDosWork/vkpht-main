package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.NotificationLogEmployeeService;
import me.goodt.vkpht.module.notification.api.NotificationLogService;
import me.goodt.vkpht.module.notification.api.NotificationTemplateContentAttachmentService;
import me.goodt.vkpht.module.notification.api.configuration.ConfigServiceClient;
import me.goodt.vkpht.module.notification.api.dto.NotificationLogDto;
import me.goodt.vkpht.module.notification.api.dto.configuration.ModuleDto;
import me.goodt.vkpht.module.notification.api.dto.data.NotificationLogStatusEnum;
import me.goodt.vkpht.module.notification.api.dto.kafka.NotificationLogResponseKafkaDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.notification.application.NotificationLogEmailService;
import me.goodt.vkpht.module.notification.application.orgstructure.OrgstructureServiceAdapter;
import me.goodt.vkpht.module.notification.application.utils.TextConstants;
import me.goodt.vkpht.module.notification.domain.dao.NotificationLogDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateContentDao;
import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentAttachmentEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEntity;
import me.goodt.vkpht.module.notification.domain.factory.NotificationLogFactory;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationLogServiceImpl implements NotificationLogService {
    private static final boolean IS_COPY = true;

    private final NotificationTemplateContentDao notificationTemplateContentDao;

    private final NotificationLogDao notificationLogDao;

    private final NotificationLogEmployeeService notificationLogEmployeeService;

    private final OrgstructureServiceAdapter orgstructureServiceAdapter;

    private final NotificationTemplateContentAttachmentService notificationTemplateContentAttachmentService;

    private final ConfigServiceClient configurationServiceClient;

    private final NotificationLogEmailService notificationLogEmailService;

    private final UnitAccessService unitAccessService;

    @Override
    @Transactional
    public NotificationLogEntity create(Long ntcId, Map<String, Object> content) {
        NotificationLogEntity notificationLog = new NotificationLogEntity();

        NotificationTemplateContentEntity ntc =
            notificationTemplateContentDao.findById(ntcId)
                .orElseThrow(() -> new NotFoundException("Не найден шаблон уведомления с id = %d".formatted(ntcId)));

        String receiverSystemName = ntc.getReceiverSystem().getName();

        notificationLog.setNotificationTemplateContent(ntc);
        notificationLog.setDateTime(new Date());
        notificationLog.setStatus(NotificationLogStatusEnum.READY.getStatusName());
        notificationLog.setSubject(parseSubject(content, receiverSystemName));
        notificationLog.setMessage(parseBody(content, receiverSystemName));

        return notificationLogDao.save(notificationLog);
    }

    @Override
    @Transactional
    public void updateStatusFromOutside(NotificationLogResponseKafkaDto notificationLogResponseKafka) {
        List<NotificationLogEntity> notificationLogList = notificationLogDao
            .findAllById(notificationLogResponseKafka.getNotificationLogIds());
        notificationLogList.forEach(notificationLog -> {
            if (notificationLogResponseKafka.isSendSuccess()) {
                updateStatus(notificationLog, NotificationLogStatusEnum.SUCCESS);
            } else {
                if (StringUtils.isNotEmpty(notificationLogResponseKafka.getErrorMessage())) {
                    notificationLog.setErrorMessage(notificationLogResponseKafka.getErrorMessage());
                }
                updateStatus(notificationLog, NotificationLogStatusEnum.ERROR);
            }
        });
    }

    @Override
    @Transactional
    public void updateStatus(NotificationLogEntity notificationLog, NotificationLogStatusEnum newStatus) {
        unitAccessService.checkUnitAccess(notificationLog.getNotificationTemplateContent().getNotificationTemplate().getUnitCode());
        notificationLog.setStatus(newStatus.getStatusName());
        notificationLogDao.save(notificationLog);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationLogDto load(Long id) throws NotFoundException {
        NotificationLogEntity notificationLog = notificationLogDao.getByIdEager(id)
            .orElseThrow(() -> new NotFoundException("Не найдена запись в журнале уведомления id = %d".formatted(id)));

        unitAccessService.checkUnitAccess(notificationLog.getNotificationTemplateContent().getNotificationTemplate().getUnitCode());

        List<Long> employeeRecipientIdList = notificationLogEmployeeService.findEmployeeIdByNotificationLogId(notificationLog.getId(), !IS_COPY);
        List<Long> employeeRecipientIdCopyList = notificationLogEmployeeService.findEmployeeIdByNotificationLogId(notificationLog.getId(), IS_COPY);

        List<EmployeeInfoDto> employeeRecipientList = null;
        List<EmployeeInfoDto> employeerecipientCopyList = null;
        if (CollectionUtils.isNotEmpty(employeeRecipientIdList)) {
            employeeRecipientList = orgstructureServiceAdapter.findEmployee(employeeRecipientIdList).getData();
        }
        if (CollectionUtils.isNotEmpty(employeeRecipientIdCopyList)) {
            employeerecipientCopyList = orgstructureServiceAdapter.findEmployee(employeeRecipientIdCopyList).getData();
        }

        List<String> emailRecipientList = notificationLogEmailService.findEmailByNotificationLogId(notificationLog.getId(), !IS_COPY);
        List<String> emailRecipientCopyList = notificationLogEmailService.findEmailByNotificationLogId(notificationLog.getId(), IS_COPY);

        List<NotificationTemplateContentAttachmentEntity> attachments = new ArrayList<>();
        if (Objects.nonNull(notificationLog.getNotificationTemplateContent())) {
            attachments = notificationTemplateContentAttachmentService.getAttachments(notificationLog.getNotificationTemplateContent());
        }
        ModuleDto module = null;

        if (Objects.nonNull(notificationLog.getNotificationTemplateContent())
            && Objects.nonNull(notificationLog.getNotificationTemplateContent().getCodeModule())) {
            module = configurationServiceClient.getModuleInfoByCode(notificationLog.getNotificationTemplateContent().getCodeModule());
        }

        NotificationLogDto notificationLogDto = NotificationLogFactory.create(
            notificationLog,
            module,
            employeeRecipientList,
            employeerecipientCopyList,
            emailRecipientList,
            emailRecipientCopyList,
            attachments
        );

        return notificationLogDto;
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationLogEntity findById(Long id) {
        return notificationLogDao.findById(id)
            .orElseThrow(() -> new NotFoundException("Не найдена запись в журнале уведомления id = %d".formatted(id)));
    }

    private String parseSubject(Map<String, Object> content, String receiverSystemName) {
        String subject = null;
        if (content.containsKey(receiverSystemName)) {
            HashMap<String, Object> contentReceiverSystem = (HashMap<String, Object>) content.get(receiverSystemName);
            if (contentReceiverSystem.containsKey(TextConstants.TITLE)) {
                subject = (String) contentReceiverSystem.get(TextConstants.TITLE);
            } else if (contentReceiverSystem.containsKey(TextConstants.SUBJECT)) {
                subject = (String) contentReceiverSystem.get(TextConstants.SUBJECT);
            }
        }

        return subject;
    }

    private String parseBody(Map<String, Object> content, String receiverSystemName) {
        String body = null;
        if (content.containsKey(receiverSystemName)) {
            HashMap<String, Object> contentReceiverSystem = (HashMap<String, Object>) content.get(receiverSystemName);
            if (contentReceiverSystem.containsKey(TextConstants.BODY)) {
                body = (String) contentReceiverSystem.get(TextConstants.BODY);
            } else if (contentReceiverSystem.containsKey(TextConstants.TEXT)) {
                body = (String) contentReceiverSystem.get(TextConstants.TEXT);
            }
        }

        return body;
    }

}
