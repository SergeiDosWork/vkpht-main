package me.goodt.vkpht.module.notification.domain.mapper;

import org.springframework.stereotype.Component;

import java.util.Objects;

import me.goodt.vkpht.common.domain.mapper.CrudDtoMapper;
import me.goodt.vkpht.module.notification.api.dto.NotificationReceiverSystemDictDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationReceiverSystemEntity;

@Component
public class NotificationReceiverSystemDictMapper
    implements CrudDtoMapper<NotificationReceiverSystemEntity, NotificationReceiverSystemDictDto> {

    @Override
    public NotificationReceiverSystemEntity toNewEntity(NotificationReceiverSystemDictDto dto) {
        NotificationReceiverSystemEntity entity = new NotificationReceiverSystemEntity();
        return toUpdatedEntity(dto, entity);
    }

    @Override
    public NotificationReceiverSystemEntity toUpdatedEntity(NotificationReceiverSystemDictDto dto, NotificationReceiverSystemEntity entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIsActive(dto.getIsActive());
        entity.setIsSystem(dto.getIsSystem());
        entity.setIsEditableIfSystem(dto.getIsEditableIfSystem());
        return entity;
    }

    @Override
    public NotificationReceiverSystemDictDto toDto(NotificationReceiverSystemEntity entity) {
        NotificationReceiverSystemDictDto dto = new NotificationReceiverSystemDictDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIsActive(entity.getIsActive());
        dto.setIsSystem(entity.getIsSystem());
        dto.setIsEditableIfSystem(entity.getIsEditableIfSystem());

        if (Objects.nonNull(entity.getDescription())) {
            dto.setDescription(entity.getDescription());
        }

        return dto;
    }
}
