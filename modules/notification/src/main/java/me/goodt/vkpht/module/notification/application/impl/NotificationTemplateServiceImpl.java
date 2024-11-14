package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.NonUniqueObjectException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.NotificationTemplateService;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateFilter;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientEmailDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientParametersDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateContentDao;
import me.goodt.vkpht.module.notification.domain.dao.NotificationTemplateDao;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEmailEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientParameterEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateEntity;
import me.goodt.vkpht.module.notification.domain.factory.NotificationTemplateContentFactory;
import me.goodt.vkpht.module.notification.domain.repository.NotificationTemplateRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private static final String PREFIX_MANUAL_MAILING = "manualmailing";

    private final NotificationTemplateDao notificationTemplateDao;

    private final NotificationTemplateRepository notificationTemplateRepository;

    private final NotificationTemplateContentDao notificationTemplateContentDao;

    private final NotificationRecipientParametersDao notificationRecipientParametersDao;

    private final NotificationRecipientEmailDao notificationRecipientEmailDao;

    private final UnitAccessService unitAccessService;

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateEntity getById(Long id) throws NotFoundException {
        NotificationTemplateEntity dbEntity = notificationTemplateDao.findById(id)
            .orElseThrow(() -> new NotFoundException("notification_template with id=%d not found".formatted(id)));
        unitAccessService.checkUnitAccess(dbEntity.getUnitCode());
        return dbEntity;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationTemplateEntity> getAll(NotificationTemplateFilter notificationTemplateFilter, Pageable paging) {
        var filter = notificationTemplateFilter.builder()
            .unitCode(unitAccessService.getCurrentUnit())
            .build();
        return notificationTemplateDao.find(filter, paging);
    }

    @Override
    @Transactional(rollbackFor = {NotFoundException.class, NonUniqueObjectException.class})
    public NotificationTemplateEntity create(NotificationTemplateDto dto, Long employeeId) throws NotFoundException, NonUniqueObjectException {
        NotificationTemplateEntity byCode = notificationTemplateDao.findByCode(dto.getCode(), unitAccessService.getCurrentUnit());
        if (!Objects.isNull(byCode)) {
            throw new NonUniqueObjectException("Невозможно создать второе действующее уведомление с тем-же кодом", byCode.getId(), byCode.getName());
        }
        NotificationTemplateEntity entity = new NotificationTemplateEntity();
        entity.setDateFrom(new Date());
        entity.setAuthorEmployeeId(employeeId);
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIsEnabled(dto.getIsEnabled());
        entity.setDateTo(dto.getDateTo());
        entity.setUnitCode(unitAccessService.getCurrentUnit());

        if (Objects.nonNull(dto.getCode()) && dto.getCode().startsWith(PREFIX_MANUAL_MAILING)) {
            Long id = notificationTemplateRepository.incrementCodeManualMailing();
            entity.setCode(PREFIX_MANUAL_MAILING + "_" + id);
        } else {
            entity.setCode(dto.getCode());
        }

        return notificationTemplateDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = {NotFoundException.class, NonUniqueObjectException.class})
    public NotificationTemplateEntity update(Long id, NotificationTemplateDto dto, Long employeeId) throws NotFoundException, NonUniqueObjectException {
        NotificationTemplateEntity dbEntity = notificationTemplateDao.findByCode(dto.getCode(), unitAccessService.getCurrentUnit());
        if (!Objects.isNull(dbEntity) && !Objects.requireNonNull(dbEntity.getId()).equals(id)) {
            throw new NonUniqueObjectException("Невозможно создать второе действующее уведомление с тем-же кодом", dbEntity.getId(), dbEntity.getName());
        }

        NotificationTemplateEntity entity = getById(id);
        entity.setName(dto.getName());
        entity.setDateUpdate(new Date());
        entity.setAuthorUpdateEmployeeId(employeeId);
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setIsEnabled(dto.getIsEnabled());
        return notificationTemplateDao.save(entity);
    }

    @Override
    @Transactional(rollbackFor = NotFoundException.class)
    public void delete(Long id, Long employeeId) throws NotFoundException {
        NotificationTemplateEntity entity = getById(id);
        entity.setDateTo(new Date());
        entity.setDateUpdate(new Date());
        entity.setAuthorUpdateEmployeeId(employeeId);
        notificationTemplateDao.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateEntity getNotificationTemplateByCode(String code) throws NotFoundException {
        NotificationTemplateEntity dbEntity = notificationTemplateDao.findByCode(code, unitAccessService.getCurrentUnit());
        unitAccessService.checkUnitAccess(dbEntity.getUnitCode());
        if (dbEntity == null) {
            throw new NotFoundException("notification_template with code=%s is not found".formatted(code));
        }
        return dbEntity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateContentEntity> getNotificationTemplateContentByNotificationTemplateId(Long notificationTemplateId) {
        return notificationTemplateContentDao.findByNotificationTemplateId(notificationTemplateId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateContentDto> findNotificationTemplateContentByCode(String code) {
        return notificationTemplateContentDao.findNotificationTemplateContentByCode(code)
            .stream()
            .map(entity -> NotificationTemplateContentFactory.create(
                    entity,
                    (recipient) -> notificationRecipientParametersDao.findByParent(recipient).stream().map(NotificationRecipientParameterEntity::getValue).collect(Collectors.toList()),
                    (recipient) -> notificationRecipientEmailDao.findByRecipientId(recipient.getId()).stream().map(NotificationRecipientEmailEntity::getEmail).collect(Collectors.toList())
                )
            )
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<NotificationRecipientEntity, List<NotificationTemplateContentEntity>> findRecipientNameAndNotTempContentByCode(String code) {
        return notificationTemplateContentDao.findRecipientNameAndNotTempContentByCode(code);
    }

    @Override
    public List<NotificationTemplateContentEntity> findBySubstituteId(Long substituteId) {
        return notificationTemplateContentDao.findBySubstituteId(substituteId);
    }
}
