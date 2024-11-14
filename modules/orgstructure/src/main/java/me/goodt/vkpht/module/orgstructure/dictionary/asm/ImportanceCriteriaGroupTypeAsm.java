package me.goodt.vkpht.module.orgstructure.dictionary.asm;

import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.orgstructure.dictionary.dto.ImportanceCriteriaGroupTypeDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupTypeEntity;
import me.goodt.vkpht.common.dictionary.core.asm.AbstractAsm;

@Component
public class ImportanceCriteriaGroupTypeAsm extends AbstractAsm<ImportanceCriteriaGroupTypeEntity, ImportanceCriteriaGroupTypeDto> {

    @Override
    public ImportanceCriteriaGroupTypeDto toRes(ImportanceCriteriaGroupTypeEntity entity) {
        ImportanceCriteriaGroupTypeDto dto = new ImportanceCriteriaGroupTypeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    @Override
    public void create(ImportanceCriteriaGroupTypeEntity entity, ImportanceCriteriaGroupTypeDto dto) {
        entity.setName(dto.getName());
    }

    @Override
    public void update(ImportanceCriteriaGroupTypeEntity entity, ImportanceCriteriaGroupTypeDto dto) {
        entity.setName(dto.getName());
    }
}
