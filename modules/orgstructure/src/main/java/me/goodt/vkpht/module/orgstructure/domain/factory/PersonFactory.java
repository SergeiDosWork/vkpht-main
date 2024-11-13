package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.PersonDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonEntity;

@UtilityClass
public class PersonFactory {

    public static PersonDto create(PersonEntity entity) {
        if (entity == null) {
            return null;
        }
        return new PersonDto(
            entity.getId(), entity.getSurname(), entity.getName(),
            entity.getPatronymic(), entity.getBirthDate(),
            entity.getFamilyStatus() == null ? null : entity.getFamilyStatus().getId().longValue(), entity.getSex(),
            entity.getParent() == null ? null : entity.getParent().getId(),
            entity.getSpouse() == null ? null : entity.getSpouse().getId(), entity.getPhoto(),
            entity.getSnils(), entity.getInn(), entity.getAddress(), entity.getPhone(), entity.getEmail(),
            entity.getExternalId(), entity.getCitizenshipId()
        );
    }
}
