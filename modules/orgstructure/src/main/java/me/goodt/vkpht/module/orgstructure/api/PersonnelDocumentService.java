package me.goodt.vkpht.module.orgstructure.api;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.PersonnelDocumentDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonnelDocumentEntity;

public interface PersonnelDocumentService {
    PersonnelDocumentEntity get(Long id) throws NotFoundException;

    List<PersonnelDocumentEntity> getAllActual();

    PersonnelDocumentEntity create(PersonnelDocumentDto dto) throws NotFoundException;

    PersonnelDocumentEntity update(Long id, PersonnelDocumentDto dto) throws NotFoundException;

    void delete(Long id) throws NotFoundException;
}
