package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PersonnelDocumentDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentEntity;

@UtilityClass
public class PersonnelDocumentFactory {

    public static PersonnelDocumentDto create(PersonnelDocumentEntity entity) {
        return new PersonnelDocumentDto(
                entity.getId(), entity.getEmployee() != null ? entity.getEmployee().getId() : null,
                entity.getPrecursor() != null ? entity.getPrecursor().getId() : null,
                entity.getType() != null ? entity.getType().getId() : null,
                entity.getForm() != null ? entity.getForm().getId() : null,
                entity.getName(), entity.getDateFrom(), entity.getDateTo(), entity.getData(),
                entity.getExternalId()
        );
    }
}
