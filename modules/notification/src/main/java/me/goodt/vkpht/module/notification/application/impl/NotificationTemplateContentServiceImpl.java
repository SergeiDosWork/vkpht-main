package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.entity.DomainObject;
import me.goodt.vkpht.module.notification.api.NotificationTemplateContentService;
import me.goodt.vkpht.module.notification.api.dto.NotificationAttachmentDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationDivisionRecipientDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationDynamicRecipientDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationEmployeeRecipientDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientEmailDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientNameDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentFilter;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentRequest;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentResponse;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentShortResponse;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentSubstituteInfo;
import me.goodt.vkpht.module.notification.api.dto.data.NotificationRecipientType;
import me.goodt.vkpht.module.notification.application.orgstructure.OrgstructureServiceAdapter;
import me.goodt.vkpht.module.notification.application.impl.resolver.strategy.NotificationTemplateContentRequestGetRecipientsStrategy;
import me.goodt.vkpht.module.notification.application.impl.resolver.strategy.NotificationTemplateContentRequestGetRecipientsStrategyCopy;
import me.goodt.vkpht.module.notification.application.impl.resolver.strategy.NotificationTemplateContentRequestGetRecipientsStrategyOriginal;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientEmailDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientParametersDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateContentAttachmentDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateContentDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateContentRecipientDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateDao;
import me.goodt.vkpht.module.notification.domain.entity.NotificationReceiverSystemEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEmailEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientParameterEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentAttachmentEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentRecipientLinkEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateEntity;
import me.goodt.vkpht.module.notification.domain.factory.NotificationReceiverSystemFactory;
import me.goodt.vkpht.module.notification.domain.mapper.NotificationRecipientEmailMapper;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoRequestDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;

import static me.goodt.vkpht.module.notification.api.dto.data.NotificationRecipientType.DYNAMIC;
import static me.goodt.vkpht.module.notification.api.dto.data.NotificationRecipientType.STATIC_DIVISION;
import static me.goodt.vkpht.module.notification.api.dto.data.NotificationRecipientType.STATIC_EMAIL;
import static me.goodt.vkpht.module.notification.api.dto.data.NotificationRecipientType.STATIC_EMPLOYEE;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTemplateContentServiceImpl implements NotificationTemplateContentService {

    private final NotificationTemplateContentDao notificationTemplateContentDao;

    private final NotificationTemplateContentRecipientDao notificationTemplateContentRecipientDao;

    private final NotificationRecipientDao notificationRecipientDao;

    private final NotificationRecipientParametersDao notificationRecipientParametersDao;

    private final NotificationTemplateContentAttachmentDao attachmentDao;

    private final OrgstructureServiceAdapter orgstructureClient;

    private final NotificationTemplateDao notificationTemplateDao;

    private final NotificationRecipientEmailDao notificationRecipientEmailDao;

    private final NotificationRecipientEmailMapper notificationRecipientEmailMapper;

    private final UnitAccessService unitAccessService;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateContentShortResponse> findAll(NotificationTemplateContentFilter filter) {
        filter.setUnitCode(unitAccessService.getCurrentUnit());

        List<NotificationTemplateContentEntity> entities = notificationTemplateContentDao.findAll(filter);

        Set<NotificationRecipientEntity> recipients = new HashSet<>();
        for (NotificationTemplateContentEntity entity : entities) {
            recipients.addAll(entity.getAllRecipients());
        }
        NotificationRecipientAggregate recipientAggregate = aggregateRecipients(recipients);

        return entities.stream()
            .map(item -> convertToShortResponse(item, recipientAggregate))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateContentResponse getById(Long id) throws NotFoundException {
        return convertToResponse(findById(id));
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public NotificationTemplateContentResponse create(NotificationTemplateContentRequest dto) throws NotFoundException {
        NotificationTemplateContentEntity entity = new NotificationTemplateContentEntity();

        NotificationTemplateEntity notificationTemplate = notificationTemplateDao.findById(dto.getNotificationTemplateId())
            .orElseThrow(() -> new NotFoundException(String.format("Not found notification_template by id = %d", dto.getNotificationTemplateId())));
        unitAccessService.checkUnitAccess(notificationTemplate.getUnitCode());
        entity.setNotificationTemplate(notificationTemplate);

        entity.setDescription(dto.getDescription());

        if (dto.getReceiverSystemId() != null) {
            NotificationReceiverSystemEntity receiverSystemEntity = new NotificationReceiverSystemEntity();
            receiverSystemEntity.setId(dto.getReceiverSystemId());
            entity.setReceiverSystem(receiverSystemEntity);
        }

        entity.setPriority(dto.isPriority());
        entity.setDateFrom(new Date());

        if (dto.getSubstituteId() != null) {
            NotificationTemplateContentEntity notificationTemplateContentEntity =
                new NotificationTemplateContentEntity();
            notificationTemplateContentEntity.setId(dto.getSubstituteId());
            entity.setSubstitute(notificationTemplateContentEntity);
        }

        entity.setBodyJson(dto.getBodyJson());
        entity.setEnabled(dto.isEnabled());
        entity.setCodeModule(dto.getCodeModule());

        NotificationTemplateContentEntity dbEntity = notificationTemplateContentDao.save(entity);

        saveAttachments(dto.getAttachments(), dbEntity);
        updateRecipients(dto, dbEntity);

        //        dto.setId(dbEntity.getId());
        //        dto.setSubstitute(dbEntity.getSubstitute());
        //        return convertToResponse(dbEntity);
        return null;
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public NotificationTemplateContentResponse update(Long id, NotificationTemplateContentRequest dto) throws NotFoundException {
        NotificationTemplateContentEntity entity = findById(id);

        entity.setDescription(dto.getDescription());

        if (dto.getReceiverSystemId() != null) {
            NotificationReceiverSystemEntity receiverSystemEntity = new NotificationReceiverSystemEntity();
            receiverSystemEntity.setId(dto.getReceiverSystemId());
            entity.setReceiverSystem(receiverSystemEntity);
        }

        entity.setPriority(dto.isPriority());

        if (dto.getSubstituteId() != null) {
            NotificationTemplateContentEntity notificationTemplateContentEntity =
                new NotificationTemplateContentEntity();
            notificationTemplateContentEntity.setId(dto.getSubstituteId());
            entity.setSubstitute(notificationTemplateContentEntity);
        } else {
            entity.setSubstitute(null);
        }

        entity.setBodyJson(dto.getBodyJson());
        entity.setEnabled(dto.isEnabled());
        entity.setCodeModule(dto.getCodeModule());

        NotificationTemplateContentEntity dbEntity = notificationTemplateContentDao.save(entity);

        updateAttachments(dto.getAttachments(), dbEntity);
        updateRecipients(dto, entity);

        return null;
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public void delete(Long id) throws NotFoundException {
        NotificationTemplateContentEntity dbEntity = findById(id);
        dbEntity.setDateTo(new Date());
        notificationTemplateContentDao.save(dbEntity);
    }

    @Override
    public List<NotificationTemplateContentSubstituteInfo> findSubstitutes(Long templateId, Long contentId) {
        NotificationTemplateContentFilter filter = new NotificationTemplateContentFilter();
        filter.setNotificationTemplateId(templateId);
        filter.setUnitCode(unitAccessService.getCurrentUnit());

        List<NotificationTemplateContentEntity> contents = notificationTemplateContentDao.findAll(filter);
        if (contentId != null) {
            contents.removeIf(content -> contentId.equals(content.getId()));
        }

        return contents.stream()
            .map(content -> new NotificationTemplateContentSubstituteInfo(
                content.getId(), content.getDescription()))
            .collect(Collectors.toList());
    }

    private void saveAttachments(List<NotificationAttachmentDto> attachments,
                                 NotificationTemplateContentEntity dbEntity) {
        if (attachments != null) {
            for (NotificationAttachmentDto attachment : attachments) {
                NotificationTemplateContentAttachmentEntity attachmentEntity =
                    new NotificationTemplateContentAttachmentEntity();
                attachmentEntity.setNotificationTemplateContent(dbEntity);
                attachmentEntity.setFileName(attachment.getFileName());
                attachmentEntity.setFileType(attachment.getFileType());
                attachmentEntity.setStorageFilePath(attachment.getStorageFilePath());
                attachmentDao.save(attachmentEntity);
            }
        }
    }

    private void updateAttachments(List<NotificationAttachmentDto> listNewAttachmentDto,
                                   NotificationTemplateContentEntity dbEntity) {

        //ранее прикрепленные файлы
        List<NotificationTemplateContentAttachmentEntity> listExistsAttachments =
            attachmentDao.getAttachmentsByNotificationTemplateContentEntityId(dbEntity.getId());

        //если прикрепленные файлы отсутствуют, то удаление всех ранее прикрепленных файлов
        if (CollectionUtils.isEmpty(listNewAttachmentDto)) {
            if (CollectionUtils.isEmpty(listExistsAttachments)) {
                return;
            }
            attachmentDao.deleteAll(listExistsAttachments);
            return;
        }

        //новые файлы
        List<NotificationTemplateContentAttachmentEntity> listNewAttachmentEntity = listNewAttachmentDto.stream()
            .map(attachment -> convertAttachmentToEntity(dbEntity, attachment))
            .collect(Collectors.toList());

        //удаляемые файлы (которые уже прикреплены, но отсутствуют в новых)
        List<NotificationTemplateContentAttachmentEntity> listDeletedAttachment = new ArrayList<>();
        listExistsAttachments.forEach(existAttachment -> {

            boolean isNotDelete = listNewAttachmentEntity.stream().anyMatch(newAttachmentEntity ->
                Objects.equals(existAttachment.getStorageFilePath(), newAttachmentEntity.getStorageFilePath())
            );

            if (!isNotDelete) {
                listDeletedAttachment.add(existAttachment);
            }
        });

        //сохраняемые файлы (новые за исключением тех, которые уже прикреплены)
        List<NotificationTemplateContentAttachmentEntity> savedAttachments = new ArrayList<>();
        listNewAttachmentEntity.forEach(newAttachmentEntity -> {

            boolean isNotSave = listExistsAttachments.stream().anyMatch(existAttachment ->
                Objects.equals(existAttachment.getStorageFilePath(), newAttachmentEntity.getStorageFilePath())
            );

            if (!isNotSave) {
                savedAttachments.add(newAttachmentEntity);
            }
        });

        if (CollectionUtils.isNotEmpty(listDeletedAttachment)) {
            attachmentDao.deleteAll(listDeletedAttachment);
            attachmentDao.flush();
        }

        attachmentDao.saveAll(savedAttachments);

    }

    private NotificationTemplateContentAttachmentEntity convertAttachmentToEntity(NotificationTemplateContentEntity dbContentEntity,
                                                                                  NotificationAttachmentDto attachment) {
        NotificationTemplateContentAttachmentEntity attachmentEntity =
            new NotificationTemplateContentAttachmentEntity();
        attachmentEntity.setId(attachment.getId());
        attachmentEntity.setNotificationTemplateContent(dbContentEntity);
        attachmentEntity.setFileName(attachment.getFileName());
        attachmentEntity.setFileType(attachment.getFileType());
        attachmentEntity.setStorageFilePath(attachment.getStorageFilePath());
        return attachmentEntity;
    }

    private NotificationAttachmentDto convertAttachmentToDto(NotificationTemplateContentAttachmentEntity entity) {
        NotificationAttachmentDto dto = new NotificationAttachmentDto();
        dto.setId(entity.getId());
        dto.setFileName(entity.getFileName());
        dto.setFileType(entity.getFileType());
        dto.setStorageFilePath(entity.getStorageFilePath());
        return dto;
    }

    private void updateRecipients(NotificationTemplateContentRequest dto, NotificationTemplateContentEntity entity) {
        List<NotificationTemplateContentRecipientLinkEntity> currentRecipients =
            notificationTemplateContentRecipientDao
                .findByNotificationTemplateContentId(entity.getId());

        syncRecipientList(
            entity,
            dto,
            currentRecipients.stream().filter(r -> !r.getIsCopy())
                .map(NotificationTemplateContentRecipientLinkEntity::getNotificationRecipient)
                .collect(Collectors.toList()),
            false);

        syncRecipientList(
            entity,
            dto,
            currentRecipients.stream().filter(NotificationTemplateContentRecipientLinkEntity::getIsCopy)
                .map(NotificationTemplateContentRecipientLinkEntity::getNotificationRecipient)
                .collect(Collectors.toList()),
            true);
    }

    private void syncRecipientList(NotificationTemplateContentEntity entity,
                                   NotificationTemplateContentRequest newDto,
                                   List<NotificationRecipientEntity> oldListAll,
                                   boolean isCopy) {
        NotificationTemplateContentRequestGetRecipientsStrategy strategy =
            isCopy ? new NotificationTemplateContentRequestGetRecipientsStrategyCopy() : new NotificationTemplateContentRequestGetRecipientsStrategyOriginal();
        // Этап 1 - синхронизируем все нестатичные токены
        List<Long> newDynamicRecipientIds = strategy.getDynamicRecipientIds(newDto);
        List<Long> oldDynamicRecipientIds = oldListAll.stream()
            .filter(n -> !n.getName().equals(STATIC_EMPLOYEE.getName()) && !n.getName().equals(STATIC_DIVISION.getName()) && !n.getName().equals(STATIC_EMAIL.getName()))
            .map(DomainObject::getId)
            .collect(Collectors.toList());

        List<Long> needToAddDynamicRecipients = newDynamicRecipientIds.stream().filter(id -> !oldDynamicRecipientIds.contains(id)).collect(Collectors.toList());
        List<Long> needToRemoveDynamicRecipients = oldDynamicRecipientIds.stream().filter(id -> !newDynamicRecipientIds.contains(id)).collect(Collectors.toList());

        for (Long needToRemoveAddDynamicRecipient : needToRemoveDynamicRecipients) {
            notificationTemplateContentRecipientDao.deleteAllByTemplateContentIdAndNotificationRecipientId(entity.getId(), needToRemoveAddDynamicRecipient);
        }
        for (Long needToAddDynamicRecipient : needToAddDynamicRecipients) {
            NotificationTemplateContentRecipientLinkEntity notificationTemplateContentRecipientLinkEntity = new NotificationTemplateContentRecipientLinkEntity();
            notificationTemplateContentRecipientLinkEntity.setNotificationTemplateContent(entity);
            notificationTemplateContentRecipientLinkEntity.setNotificationRecipient(notificationRecipientDao.findById(needToAddDynamicRecipient).orElseThrow(() -> new NotFoundException("Recipient not found")));
            notificationTemplateContentRecipientLinkEntity.setIsCopy(isCopy);
            notificationTemplateContentRecipientDao.save(notificationTemplateContentRecipientLinkEntity);
        }

        // Этап 2 - синхронизируем всех статичных получателей employee
        List<Long> newRecipientEmployeeIds = strategy.getRecipientEmployeeIds(newDto);
        saveStaticParameters(oldListAll, STATIC_EMPLOYEE, newRecipientEmployeeIds, entity, isCopy, newDto.getIsSystem());

        // Этап 3 - синхронизируем всех статичных получателей division
        List<Long> newRecipientDivisionIds = strategy.getRecipientDivisionIds(newDto);
        saveStaticParameters(oldListAll, STATIC_DIVISION, newRecipientDivisionIds, entity, isCopy, newDto.getIsSystem());

        // Этап 4 - синхронизируем всех получателей по email-адресам
        List<String> newEmailRecipients = strategy.getEmailRecipients(newDto);
        saveStaticEmails(oldListAll, newEmailRecipients, entity, isCopy, newDto.getIsSystem());
    }

    private void saveStaticEmails(
        List<NotificationRecipientEntity> oldListAll,
        List<String> newEmailList,
        NotificationTemplateContentEntity notificationTemplateContent,
        Boolean isCopy,
        Boolean isSystem
    ) {

        //Проверяем наличие указанных email в оргструктуре
        if (CollectionUtils.isNotEmpty(newEmailList)) {
            //Проверяем наличие указанных email в оргструктуре
            List<EmployeeInfoDto> employees = orgstructureClient.findEmployeeByEmails(newEmailList).getData();
            //если указанные email привязаны к сотрудникам в оргструктуре, то синхронизируем их как STATIC_EMPLOYEE
            if (CollectionUtils.isNotEmpty(employees)) {
                List<Long> existedEmployeeIdByEmail = employees.stream().map(EmployeeInfoDto::getId).collect(Collectors.toList());
                List<String> existedEmployeeEmailByEmail = employees.stream().map(EmployeeInfoDto::getEmail).collect(Collectors.toList());

                saveStaticParameters(
                    oldListAll,
                    STATIC_EMPLOYEE,
                    existedEmployeeIdByEmail,
                    notificationTemplateContent,
                    isCopy,
                    isSystem
                );

                //исключаем существующие в оргструктуре email адреса из списка новых
                newEmailList.removeAll(existedEmployeeEmailByEmail);
            }
        }

        List<NotificationRecipientEntity> existedStaticRecipientList = oldListAll.stream()
            .filter(n -> n.getName().equals(STATIC_EMAIL.getName()))
            .collect(Collectors.toList());

        //STATIC_EMAIL нет, создаем новые
        if (CollectionUtils.isEmpty(existedStaticRecipientList)) {
            //оставшиеся email-адреса, не привязанные к оргструктуре, сохраняем как STATIC_EMAIL
            if (CollectionUtils.isNotEmpty(newEmailList)) {
                NotificationRecipientEntity newStaticRecipient = new NotificationRecipientEntity(STATIC_EMAIL.getName(), STATIC_EMAIL.getDescription());
                newStaticRecipient.setUnitCode(notificationTemplateContent.getNotificationTemplate().getUnitCode());
                notificationRecipientDao.save(newStaticRecipient);
                List<NotificationRecipientEmailEntity> newNotificationRecipientEmailList = new ArrayList<>();
                for (String email : newEmailList) {
                    NotificationRecipientEmailEntity newEmailEntity = new NotificationRecipientEmailEntity();
                    newEmailEntity.setNotificationRecipient(newStaticRecipient);
                    newEmailEntity.setEmail(email);
                    newEmailEntity.setIsSystem(isSystem);
                    newNotificationRecipientEmailList.add(newEmailEntity);
                }
                notificationRecipientEmailDao.saveAll(newNotificationRecipientEmailList);

                NotificationTemplateContentRecipientLinkEntity notificationTemplateContentRecipientLinkEntity =
                    new NotificationTemplateContentRecipientLinkEntity(notificationTemplateContent, newStaticRecipient, isCopy, isSystem);
                notificationTemplateContentRecipientDao.save(notificationTemplateContentRecipientLinkEntity);
            }
        } else {
            //STATIC_EMAIL уже есть, нужно синхронизировать
            if (CollectionUtils.isNotEmpty(newEmailList)) {
                syncRecipientEmailList(existedStaticRecipientList.getFirst(), newEmailList);
            } else {
                //Новый список email пуст, значит старые email удаляем с получателем и связью

                List<NotificationRecipientEmailEntity> emails = notificationRecipientEmailDao.findByRecipientId(existedStaticRecipientList.getFirst().getId());
                notificationRecipientEmailDao.deleteAll(emails);
                notificationTemplateContentRecipientDao.deleteAllByTemplateContentIdAndNotificationRecipientId(
                    notificationTemplateContent.getId(),
                    existedStaticRecipientList.getFirst().getId()
                );
                notificationRecipientDao.delete(existedStaticRecipientList.getFirst());

            }
        }
    }

    private void saveStaticParameters(List<NotificationRecipientEntity> oldListAll, NotificationRecipientType staticName, List<Long> newRecipientStaticIds, NotificationTemplateContentEntity entity, boolean isCopy, Boolean isSystem) {
        Optional<NotificationRecipientEntity> existedStaticRecipient = oldListAll.stream().filter(n -> n.getName().equals(staticName.getName())).findFirst();
        if (existedStaticRecipient.isEmpty()) {
            if (!newRecipientStaticIds.isEmpty()) {
                // Нужно создать STATIC и присвоить параметры
                NotificationRecipientEntity newStaticRecipient = new NotificationRecipientEntity(staticName.getName(), null);
                newStaticRecipient.setUnitCode(entity.getNotificationTemplate().getUnitCode());
                notificationRecipientDao.save(newStaticRecipient);
                for (Long newRecipientDivisionId : newRecipientStaticIds) {
                    NotificationRecipientParameterEntity notificationRecipientParameterEntity = new NotificationRecipientParameterEntity(newRecipientDivisionId, newStaticRecipient);
                    notificationRecipientParametersDao.save(notificationRecipientParameterEntity);
                }
                NotificationTemplateContentRecipientLinkEntity notificationTemplateContentRecipientLinkEntity =
                    new NotificationTemplateContentRecipientLinkEntity(entity, newStaticRecipient, isCopy, isSystem);
                notificationTemplateContentRecipientDao.save(notificationTemplateContentRecipientLinkEntity);
            }
        } else {
            // STATIC уже есть, нужно синхронизировать список id
            if (!newRecipientStaticIds.isEmpty()) {
                // Если нужно синхронизировать списки параметров новые и старые
                syncRecipientParameterList(existedStaticRecipient.get(), newRecipientStaticIds);
            } else {
                // Новый список параметров пуст, значит старые параметры удаляем вместе с получателем и связью
                List<NotificationRecipientParameterEntity> params = notificationRecipientParametersDao.findByParent(existedStaticRecipient.get());
                notificationRecipientParametersDao.deleteAllInBatch(params);
                notificationTemplateContentRecipientDao.deleteAllByTemplateContentIdAndNotificationRecipientId(entity.getId(), existedStaticRecipient.get().getId());
                notificationRecipientDao.delete(existedStaticRecipient.get());
            }
        }
    }

    private void syncRecipientParameterList(NotificationRecipientEntity notificationRecipientEntity, List<Long> newIds) {
        List<NotificationRecipientParameterEntity> params = notificationRecipientParametersDao.findByParent(notificationRecipientEntity);
        List<Long> oldIds = params
            .stream().map(NotificationRecipientParameterEntity::getValue).collect(Collectors.toList());

        List<Long> needToAdd = newIds.stream().filter(id -> !oldIds.contains(id)).collect(Collectors.toList());
        List<Long> needToRemove = oldIds.stream().filter(id -> !newIds.contains(id)).collect(Collectors.toList());

        for (NotificationRecipientParameterEntity param : params) {
            if (needToRemove.contains(param.getValue())) {
                notificationRecipientParametersDao.delete(param);
            }
        }
        for (Long aLong : needToAdd) {
            NotificationRecipientParameterEntity newParamEntity = new NotificationRecipientParameterEntity(aLong, notificationRecipientEntity);
            notificationRecipientParametersDao.save(newParamEntity);
        }
    }

    private void syncRecipientEmailList(NotificationRecipientEntity notificationRecipient, List<String> newEmailList) {
        List<NotificationRecipientEmailEntity> emails = notificationRecipientEmailDao.findByRecipientId(notificationRecipient.getId());
        List<String> oldEmails = emails.stream()
            .map(NotificationRecipientEmailEntity::getEmail).collect(Collectors.toList());

        List<String> needToAdd = newEmailList.stream().filter(e -> !oldEmails.contains(e)).collect(Collectors.toList());
        List<String> needToRemove = oldEmails.stream().filter(e -> !newEmailList.contains(e)).collect(Collectors.toList());

        for (NotificationRecipientEmailEntity email : emails) {
            if (needToRemove.contains(email.getEmail())) {
                notificationRecipientEmailDao.delete(email);
            }
        }

        for (String email : needToAdd) {
            NotificationRecipientEmailEntity newNotificationRecipientEmailEntity = new NotificationRecipientEmailEntity();
            newNotificationRecipientEmailEntity.setEmail(email);
            newNotificationRecipientEmailEntity.setNotificationRecipient(notificationRecipient);
            notificationRecipientEmailDao.save(newNotificationRecipientEmailEntity);
        }
    }

    private NotificationTemplateContentShortResponse convertToShortResponse(NotificationTemplateContentEntity entity,
                                                                            NotificationRecipientAggregate recipientAggregate) {
        NotificationTemplateContentShortResponse response = new NotificationTemplateContentShortResponse();
        response.setId(entity.getId());
        response.setNotificationTemplateId(entity.getNotificationTemplate().getId());
        response.setDescription(entity.getDescription());
        response.setBodyJson(entity.getBodyJson());
        response.setReceiverSystem(NotificationReceiverSystemFactory.create(entity.getReceiverSystem()));
        response.setPriority(entity.getPriority());
        response.setEnabled(entity.isEnabled());

        if (Objects.nonNull(entity.getCodeModule())) {
            response.setCodeModule(entity.getCodeModule());
        }

        response.setRecipients(convertRecipientNames(entity.getAllRecipients(), recipientAggregate));

        return response;
    }

    private List<NotificationRecipientNameDto> convertRecipientNames(Collection<NotificationRecipientEntity> recipients,
                                                                     NotificationRecipientAggregate recipientAggregate) {
        List<NotificationRecipientNameDto> names = new ArrayList<>();

        for (NotificationRecipientEntity recipient : recipients) {

            if (STATIC_EMPLOYEE.getName().equals(recipient.getName())) {
                List<NotificationRecipientParameterEntity> parameters = recipientAggregate.getParameters(recipient.getId());

                if (parameters == null || parameters.isEmpty()) {
                    log.warn("Отсутствуют параметры для получатель ID={} c токеном STATIC_EMPLOYEE", recipient.getId());
                    continue;
                }

                parameters.forEach(parameter -> {
                    EmployeeInfoDto employee = recipientAggregate.getEmployee(parameter.getValue());
                    if (employee == null) {
                        log.warn("Сотрудник ID={}, указанный в параметрах получателя ID={} не получен из orgstructure",
                            parameter.getValue(), recipient.getId());
                        return;
                    }

                    StringJoiner nameJoiner = new StringJoiner(" ");
                    if (employee.getLastName() != null) {
                        nameJoiner.add(employee.getLastName());
                    }
                    if (employee.getFirstName() != null) {
                        nameJoiner.add(employee.getFirstName().charAt(0) + ".");
                    }
                    if (employee.getMiddleName() != null) {
                        nameJoiner.add(employee.getMiddleName().charAt(0) + ".");
                    }
                    names.add(new NotificationRecipientNameDto(nameJoiner.toString(), STATIC_EMPLOYEE));
                });
            } else if (STATIC_DIVISION.getName().equals(recipient.getName())) {
                List<NotificationRecipientParameterEntity> parameters = recipientAggregate.getParameters(recipient.getId());

                parameters.forEach(parameter -> {
                    DivisionDto division = recipientAggregate.getDivision(parameter.getValue());
                    if (division == null) {
                        log.warn("Подразделение ID={}, указанное в параметрах получателя ID={} не получено из orgstructure",
                            parameter.getValue(), recipient.getId());
                        return;
                    }

                    String name = division.getShortName() != null ? division.getShortName() : division.getFullName();

                    names.add(new NotificationRecipientNameDto(name, STATIC_DIVISION));
                });
            } else if (STATIC_EMAIL.getName().equals(recipient.getName())) {
                List<NotificationRecipientEmailDto> emails = recipientAggregate.getEmails(recipient.getId());
                for (NotificationRecipientEmailDto emailInfo : emails) {
                    names.add(new NotificationRecipientNameDto(emailInfo.getEmail(), STATIC_EMAIL));
                }
            } else {
                names.add(new NotificationRecipientNameDto(recipient.getDescription(), DYNAMIC));
            }
        }

        return names;
    }

    private NotificationTemplateContentResponse convertToResponse(NotificationTemplateContentEntity entity) {
        NotificationTemplateContentResponse response = new NotificationTemplateContentResponse();
        response.setId(entity.getId());
        if (entity.getNotificationTemplate() != null) {
            response.setNotificationTemplateId(entity.getNotificationTemplate().getId());
        }
        response.setDescription(entity.getDescription());
        if (entity.getSubstitute() != null) {
            response.setSubstitute(convertToSubstituteInfo(entity.getSubstitute()));
        }
        response.setBodyJson(entity.getBodyJson());
        response.setReceiverSystem(NotificationReceiverSystemFactory.create(entity.getReceiverSystem()));
        response.setPriority(entity.getPriority());
        response.setEnabled(entity.isEnabled());

        if (Objects.nonNull(entity.getCodeModule())) {
            response.setCodeModule(entity.getCodeModule());
        }

        response.setAttachments(attachmentDao.getAttachmentsByNotificationTemplateContentEntityId(entity.getId()).stream()
            .map(this::convertAttachmentToDto)
            .collect(Collectors.toList()));

        // /////////////////////////////////////////////////////////////////////
        // Данные получателей.
        //
        response.setEmployeeRecipients(new ArrayList<>());
        response.setDivisionRecipients(new ArrayList<>());
        response.setDynamicRecipients(new ArrayList<>());
        response.setEmailRecipients(new ArrayList<>());

        response.setEmployeeCopyRecipients(new ArrayList<>());
        response.setDivisionCopyRecipients(new ArrayList<>());
        response.setDynamicCopyRecipients(new ArrayList<>());
        response.setEmailRecipientsCopy(new ArrayList<>());

        NotificationRecipientAggregate recipientAggregate = aggregateRecipients(entity.getAllRecipients());

        // Основные получатели
        for (NotificationRecipientEntity recipient : entity.getNotificationRecipient()) {
            if (STATIC_EMPLOYEE.getName().equals(recipient.getName())) {
                recipientAggregate.getStaticEmployees(recipient.getId()).forEach(employee -> {
                    NotificationEmployeeRecipientDto employeeRecipient = new NotificationEmployeeRecipientDto();
                    employeeRecipient.setRecipientId(recipient.getId());
                    employeeRecipient.setEmployeeId(employee.getId());
                    employeeRecipient.setFirstName(employee.getFirstName());
                    employeeRecipient.setLastName(employee.getLastName());
                    employeeRecipient.setMiddleName(employee.getMiddleName());

                    response.getEmployeeRecipients().add(employeeRecipient);
                });
            } else if (STATIC_DIVISION.getName().equals(recipient.getName())) {
                recipientAggregate.getStaticDivisions(recipient.getId()).forEach(division -> {
                    NotificationDivisionRecipientDto divisionRecipient = new NotificationDivisionRecipientDto();
                    divisionRecipient.setRecipientId(recipient.getId());
                    divisionRecipient.setDivisionId(division.getId());
                    divisionRecipient.setAbbreviation(division.getAbbreviation());
                    divisionRecipient.setFullName(division.getFullName());
                    divisionRecipient.setShortName(division.getShortName());

                    response.getDivisionRecipients().add(divisionRecipient);
                });
            } else if (STATIC_EMAIL.getName().equals(recipient.getName())) {
                response.getEmailRecipients().addAll(
                    recipientAggregate.getEmails(recipient.getId())
                        .stream()
                        .map(e -> e.getEmail())
                        .collect(Collectors.toList())
                );
            } else { // Вычисляемые получатели по токену.
                NotificationDynamicRecipientDto dynamicRecipient = new NotificationDynamicRecipientDto();
                dynamicRecipient.setRecipientId(recipient.getId());
                dynamicRecipient.setToken(recipient.getName());
                dynamicRecipient.setDescription(recipient.getDescription());

                response.getDynamicRecipients().add(dynamicRecipient);
            }
        }

        // Получатели копий
        for (NotificationRecipientEntity recipient : entity.getNotificationRecipientCopy()) {
            if (STATIC_EMPLOYEE.getName().equals(recipient.getName())) {
                recipientAggregate.getStaticEmployees(recipient.getId()).forEach(employee -> {
                    NotificationEmployeeRecipientDto employeeRecipient = new NotificationEmployeeRecipientDto();
                    employeeRecipient.setRecipientId(recipient.getId());
                    employeeRecipient.setEmployeeId(employee.getId());
                    employeeRecipient.setFirstName(employee.getFirstName());
                    employeeRecipient.setLastName(employee.getLastName());
                    employeeRecipient.setMiddleName(employee.getMiddleName());

                    response.getEmployeeCopyRecipients().add(employeeRecipient);
                });
            } else if (STATIC_DIVISION.getName().equals(recipient.getName())) {
                recipientAggregate.getStaticDivisions(recipient.getId()).forEach(division -> {
                    NotificationDivisionRecipientDto divisionRecipient = new NotificationDivisionRecipientDto();
                    divisionRecipient.setRecipientId(recipient.getId());
                    divisionRecipient.setDivisionId(division.getId());
                    divisionRecipient.setAbbreviation(division.getAbbreviation());
                    divisionRecipient.setFullName(division.getFullName());
                    divisionRecipient.setShortName(division.getShortName());

                    response.getDivisionCopyRecipients().add(divisionRecipient);
                });
            } else if (STATIC_EMAIL.getName().equals(recipient.getName())) {
                response.getEmailRecipientsCopy().addAll(
                    recipientAggregate.getEmails(recipient.getId())
                        .stream()
                        .map(e -> e.getEmail())
                        .collect(Collectors.toList())
                );
            } else { // Вычисляемые получатели по токену.
                NotificationDynamicRecipientDto dynamicRecipient = new NotificationDynamicRecipientDto();
                dynamicRecipient.setRecipientId(recipient.getId());
                dynamicRecipient.setToken(recipient.getName());
                dynamicRecipient.setDescription(recipient.getDescription());

                response.getDynamicCopyRecipients().add(dynamicRecipient);
            }
        }

        return response;
    }

    private NotificationTemplateContentEntity findById(Long id) throws NotFoundException {
        NotificationTemplateContentEntity entity = notificationTemplateContentDao.findById(id).orElseThrow(() ->
            new NotFoundException("NotificationTemplateContent with id = %d not found".formatted(id)));
        unitAccessService.checkUnitAccess(entity.getNotificationTemplate().getUnitCode());
        return entity;
    }

    private NotificationTemplateContentSubstituteInfo convertToSubstituteInfo(NotificationTemplateContentEntity entity) {
        NotificationTemplateContentSubstituteInfo dto = new NotificationTemplateContentSubstituteInfo();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    private NotificationRecipientAggregate aggregateRecipients(Collection<NotificationRecipientEntity> recipients) {

        Set<Long> staticRecipientIds = new HashSet<>();
        for (NotificationRecipientEntity recipient : recipients) {
            if (STATIC_EMPLOYEE.getName().equals(recipient.getName()) ||
                STATIC_DIVISION.getName().equals(recipient.getName()) ||
                STATIC_EMAIL.getName().equals(recipient.getName())) {
                staticRecipientIds.add(recipient.getId());
            }
        }

        List<NotificationRecipientParameterEntity> parameters = notificationRecipientParametersDao.findByParentIds(staticRecipientIds);
        List<NotificationRecipientEmailEntity> emailList = notificationRecipientEmailDao.findByRecipientIds(staticRecipientIds);

        Set<Long> employeeIds = new HashSet<>();
        Set<Long> divisionIds = new HashSet<>();
        for (NotificationRecipientParameterEntity parameter : parameters) {
            if (STATIC_EMPLOYEE.getName().equals(parameter.getParent().getName())) {
                employeeIds.add(parameter.getValue());
            } else if (STATIC_DIVISION.getName().equals(parameter.getParent().getName())) {
                divisionIds.add(parameter.getValue());
            }
        }

        List<EmployeeInfoDto> employees = employeeIds.isEmpty() ? Collections.emptyList() : orgstructureClient.findEmployee(List.copyOf(employeeIds)).getData();

        List<DivisionDto> divisions = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(divisionIds)) {
            DivisionInfoRequestDto divisionInfoRequestDto = new DivisionInfoRequestDto();
            divisionInfoRequestDto.setDivisionIds(List.copyOf(divisionIds));
            divisions = orgstructureClient.getDivisionList(divisionInfoRequestDto)
                .stream()
                .map(DivisionInfoDto::getDivision)
                .collect(Collectors.toList());
        }

        List<NotificationRecipientEmailDto> emailListDto = emailList.isEmpty() ?
            Collections.emptyList() :
            emailList.stream().map(email -> notificationRecipientEmailMapper.convertLazy(email)).collect(Collectors.toList());

        return new NotificationRecipientAggregate(recipients, parameters, employees, divisions, emailListDto);
    }
}
