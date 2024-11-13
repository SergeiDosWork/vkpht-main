package me.goodt.vkpht.module.notification.application.impl;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientEmailDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientParametersDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateContentRecipientDao;
import me.goodt.vkpht.module.notification.api.dto.NotificationDynamicRecipientDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationRecipientDto;
import me.goodt.vkpht.module.notification.domain.factory.NotificationRecipientFactory;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEmailEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientParameterEntity;
import me.goodt.vkpht.module.notification.api.NotificationRecipientService;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationRecipientServiceImpl implements NotificationRecipientService {

	private final NotificationRecipientDao notificationRecipientDao;
	private final NotificationRecipientParametersDao notificationRecipientParametersDao;
    private final NotificationRecipientEmailDao notificationRecipientEmailDao;
    private final NotificationTemplateContentRecipientDao notificationTemplateContentRecipientDao;
    private final UnitAccessService unitAccessService;


    @Transactional(readOnly = true)
    @Override
    public List<NotificationRecipientDto> getAllNotificationRecipient() {
        List<NotificationRecipientParameterEntity> allParams = notificationRecipientParametersDao.findAll();
        List<NotificationRecipientEmailEntity> allEmails = notificationRecipientEmailDao.findAll();

        Map<NotificationRecipientEntity, List<Long>> collectAllParams = allParams.stream()
            .collect(Collectors.groupingBy(NotificationRecipientParameterEntity::getParent,
                                           Collectors.collectingAndThen(Collectors.toList(), n -> n.stream().map(NotificationRecipientParameterEntity::getValue).collect(Collectors.toList()))));

        Map<NotificationRecipientEntity, List<String>> collectAllEmails = allEmails.stream()
            .collect(Collectors.groupingBy(NotificationRecipientEmailEntity::getNotificationRecipient,
                                           Collectors.collectingAndThen(Collectors.toList(), n -> n.stream().map(NotificationRecipientEmailEntity::getEmail).collect(Collectors.toList()))));

        List<NotificationRecipientEntity> dbEntity = notificationRecipientDao.findAll(unitAccessService.getCurrentUnit());

        return dbEntity.stream()
            .map(entity -> NotificationRecipientFactory.create(entity, collectAllParams.get(entity), collectAllEmails.get(entity)))
            .collect(Collectors.toList());
    }

	@Transactional(readOnly = true)
	@Override
    public NotificationRecipientDto getNotificationRecipient(Long id) throws NotFoundException {

        return notificationRecipientDao.findById(id)
            .map(entity -> {
                unitAccessService.checkUnitAccess(entity.getUnitCode());
                return NotificationRecipientFactory.create(
                        entity,
                        notificationRecipientParametersDao.findByParent(entity).stream().map(NotificationRecipientParameterEntity::getValue).collect(Collectors.toList()),
                        notificationRecipientEmailDao.findByRecipientId(entity.getId()).stream().map(NotificationRecipientEmailEntity::getEmail).collect(Collectors.toList())
                    );
                }
            )
            .orElseThrow(() -> new NotFoundException("notification_recipient with id=%d is not found".formatted(id)));
    }

    @Override
    public NotificationRecipientDto createNotificationRecipient(NotificationRecipientDto dto) {
        NotificationRecipientEntity entity = new NotificationRecipientEntity(dto.getName(), dto.getDescription());
        entity.setUnitCode(unitAccessService.getCurrentUnit());
        final NotificationRecipientEntity savedEntity = notificationRecipientDao.save(entity);

        List<Long> parameters;
        List<String> emails = Collections.EMPTY_LIST;

        if (CollectionUtils.isNotEmpty(dto.getParameters())) {
            parameters = new ArrayList<>();
            dto.getParameters().forEach(p -> parameters.add(notificationRecipientParametersDao.save(new NotificationRecipientParameterEntity(p, savedEntity, entity.getIsSystem())).getValue()));
        } else {
            parameters = Collections.EMPTY_LIST;
        }

        if (CollectionUtils.isNotEmpty(dto.getEmails())) {
            emails = new ArrayList<>();
            for (String email : dto.getEmails()) {
                NotificationRecipientEmailEntity newEmailEntity = new NotificationRecipientEmailEntity();
                newEmailEntity.setEmail(email);
                newEmailEntity.setNotificationRecipient(savedEntity);
                newEmailEntity.setIsSystem(entity.getIsSystem());
                emails.add(notificationRecipientEmailDao.save(newEmailEntity).getEmail());
            }
        }

        return NotificationRecipientFactory.create(savedEntity, parameters, emails);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public NotificationRecipientDto updateNotificationRecipient(NotificationRecipientDto dto) throws NotFoundException {
        NotificationRecipientEntity entity = notificationRecipientDao.findById(dto.getId()).orElseThrow(() -> new NotFoundException(String.format("notification_recipient with id=%d is not found", dto.getId())));
        unitAccessService.checkUnitAccess(entity.getUnitCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        List<NotificationRecipientParameterEntity> parameters = notificationRecipientParametersDao.findByParent(entity);
        List<NotificationRecipientParameterEntity> parametersToRemove = parameters.stream()
            .filter(e -> dto.getParameters().stream().noneMatch(p -> p.equals(e.getValue()))).collect(Collectors.toList());
        parameters.removeIf(e -> dto.getParameters().stream().noneMatch(p -> p.equals(e.getValue())));
        dto.getParameters().removeIf(l -> parameters.stream().anyMatch(e -> l.equals(e.getValue())));
        List<NotificationRecipientParameterEntity> parametersToAdd = dto.getParameters()
            .stream().map(aLong -> new NotificationRecipientParameterEntity(aLong, entity, entity.getIsSystem()))
            .collect(Collectors.toList());
        parameters.addAll(dto.getParameters()
                              .stream().map(aLong -> new NotificationRecipientParameterEntity(aLong, entity, entity.getIsSystem()))
                              .collect(Collectors.toList()));
        notificationRecipientParametersDao.deleteAllInBatch(parametersToRemove);
        notificationRecipientParametersDao.saveAll(parametersToAdd);

        List<NotificationRecipientEmailEntity> emails = notificationRecipientEmailDao.findByRecipientId(entity.getId());

        if (CollectionUtils.isNotEmpty(emails)) {
            List<NotificationRecipientEmailEntity> emailsToRemove = emails.stream()
                .filter(e -> dto.getEmails().stream().noneMatch(n -> n.equals(e.getEmail())))
                .collect(Collectors.toList());

            emails.removeIf(e -> dto.getEmails().stream().noneMatch(n -> n.equals(e.getEmail())));

            dto.getEmails().removeIf(n -> emails.stream().anyMatch(e -> n.equals(e.getEmail())));

            List<NotificationRecipientEmailEntity> emailsToAdd = Collections.EMPTY_LIST;
            for (String email : dto.getEmails()) {
                NotificationRecipientEmailEntity newEmail = new NotificationRecipientEmailEntity();
                newEmail.setEmail(email);
                newEmail.setNotificationRecipient(entity);
                newEmail.setIsSystem(entity.getIsSystem());
                emailsToAdd.add(newEmail);
            }

            if (CollectionUtils.isNotEmpty(emailsToRemove)) {
                notificationRecipientEmailDao.deleteAllInBatch(emailsToRemove);
            }

            if (CollectionUtils.isNotEmpty(emailsToAdd)) {
                emails.addAll(emailsToAdd);
                notificationRecipientEmailDao.saveAll(emailsToAdd);
            }

        }

        return NotificationRecipientFactory.create(
            notificationRecipientDao.save(entity),
            parameters.stream().map(NotificationRecipientParameterEntity::getValue).collect(Collectors.toList()),
            emails.stream().map(NotificationRecipientEmailEntity::getEmail).collect(Collectors.toList())
        );
    }

	@Override
	public boolean deleteNotificationRecipient(Long id)  {
		Optional<NotificationRecipientEntity> byId = notificationRecipientDao.findById(id);

		if (byId.isEmpty()) {
			return false;
		}
        unitAccessService.checkUnitAccess(byId.get().getUnitCode());
        notificationTemplateContentRecipientDao.deleteAll(notificationTemplateContentRecipientDao.findByRecipient(byId.get()));
        notificationRecipientParametersDao.deleteAll(notificationRecipientParametersDao.findByParent(byId.get()));
        notificationRecipientEmailDao.deleteAll(notificationRecipientEmailDao.findByRecipient(byId.get()));
		notificationRecipientDao.delete(byId.get());
		return true;
	}

	@Override
	public List<NotificationDynamicRecipientDto> getDynamicRecipients() {
		return notificationRecipientDao.getDynamicRecipients(unitAccessService.getCurrentUnit()).stream()
			.map(recipientToken -> {
				NotificationDynamicRecipientDto dto = new NotificationDynamicRecipientDto();
				dto.setRecipientId(recipientToken.getId());
				dto.setToken(recipientToken.getName());
				dto.setDescription(recipientToken.getDescription());
				return dto;
			})
			.collect(Collectors.toList());
	}
}
