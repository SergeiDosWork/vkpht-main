package me.goodt.vkpht.module.notification.domain.factory;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.notification.api.dto.NotificationLogDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentAttachmentDto;
import me.goodt.vkpht.module.notification.api.dto.configuration.ModuleDto;
import me.goodt.vkpht.module.notification.api.dto.orgstructure.EmployeeInfoDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationLogEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentAttachmentEntity;
import com.goodt.drive.notify.application.utils.DataUtils;

public final class NotificationLogFactory {
    public static NotificationLogDto create(NotificationLogEntity entity,
                                            ModuleDto module,
                                            List<EmployeeInfoDto> employeeRecipientList,
                                            List<EmployeeInfoDto> employeeRecipientCopyList,
                                            List<String> emailRecipientList,
                                            List<String> emailRecipientCopyList,
                                            List<NotificationTemplateContentAttachmentEntity> attachments) {
        NotificationLogDto dto = new NotificationLogDto();

        if (Objects.nonNull(entity.getNotificationTemplateContent())) {
            if (Objects.nonNull(entity.getNotificationTemplateContent().getReceiverSystem())) {
                dto.setReceiverSystemId(entity.getNotificationTemplateContent().getReceiverSystem().getId());
                dto.setReceiverSystemName(entity.getNotificationTemplateContent().getReceiverSystem().getDescription());
            }
            dto.setDescription(entity.getNotificationTemplateContent().getDescription());
            dto.setId(entity.getNotificationTemplateContent().getId());
            dto.setPriority(entity.getNotificationTemplateContent().getPriority());
        }
        if (Objects.nonNull(module)) {
            dto.setCodeModule(module.getCode());
            dto.setModuleName(module.getName());
        }

        List<String> recipients = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(employeeRecipientList)) {
            recipients.addAll(employeeRecipientList.stream()
                .map(employeeRecipient ->
                    DataUtils.getEmployeeShortName(employeeRecipient))
                .collect(Collectors.toList())
            );
        }

        if (CollectionUtils.isNotEmpty(emailRecipientList)) {
            recipients.addAll(emailRecipientList);
        }

        if (CollectionUtils.isNotEmpty(recipients)) {
            dto.setRecipients(recipients.stream().collect(Collectors.joining(", ")));
        }

        List<String> recipientsCopy = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(employeeRecipientCopyList)) {
            recipientsCopy.addAll(employeeRecipientCopyList.stream()
                .map(employeeRecipient -> DataUtils.getEmployeeShortName(employeeRecipient))
                .collect(Collectors.toList())
            );
        }

        if (CollectionUtils.isNotEmpty(emailRecipientCopyList)) {
            recipientsCopy.addAll(emailRecipientCopyList);
        }

        if (CollectionUtils.isNotEmpty(recipientsCopy)) {
            dto.setRecipientsCopy(recipientsCopy.stream().collect(Collectors.joining(", ")));
        }

        if (Objects.nonNull(attachments) && !attachments.isEmpty()) {
            List<NotificationTemplateContentAttachmentDto> attachmentDtoList = new ArrayList<>();
            attachments.forEach(attachment -> {
                NotificationTemplateContentAttachmentDto attachmentDto = new NotificationTemplateContentAttachmentDto();
                attachmentDto.setFileName(attachment.getFileName());
                attachmentDto.setStorageFilePath(attachment.getStorageFilePath());
                attachmentDtoList.add(attachmentDto);
            });
            dto.setAttachments(attachmentDtoList);
        }

        dto.setDateModify(entity.getDateTime());
        dto.setStatus(entity.getStatus());
        dto.setErrorMessage(entity.getErrorMessage());
        dto.setSubscribe(entity.getSubject());
        dto.setMessage(entity.getMessage());

        return dto;
    }
}
