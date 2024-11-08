package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamStatDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionWithDivisionTeamsStatDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionWithParentDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionEntity;

@UtilityClass
public class DivisionFactory {

    public static DivisionDto create(DivisionEntity entity) {
        return new DivisionDto(entity.getId(), entity.getParentId(),
                               entity.getPrecursorId(),
                               entity.getDateFrom(), entity.getDateTo(),
                               entity.getLegalEntityId(),
                               entity.getStructure() != null ? entity.getStructure().getId() : null,
                               entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
                               entity.getStatus() != null ? entity.getStatus().getId().longValue() : null,
                               entity.getGroupId(),
                               entity.getExternalId(), entity.getCostCenter() == null ? null : entity.getCostCenter().getId());
    }

    public static DivisionWithDivisionTeamsStatDto createWithDivisionTeamsStat(DivisionEntity entity, List<DivisionTeamStatDto> divisionTeams) {
        return new DivisionWithDivisionTeamsStatDto(entity.getId(), entity.getParentId(),
                                                    entity.getPrecursorId(),
                                                    entity.getDateFrom(), entity.getDateTo(),
                                                    entity.getLegalEntityId(),
                                                    entity.getStructure() != null ? entity.getStructure().getId() : null,
                                                    entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
                                                    entity.getStatus() != null ? entity.getStatus().getId().longValue() : null,
                                                    entity.getGroupId(),
                                                    entity.getExternalId(), divisionTeams,
                                                    entity.getCostCenter() == null ? null : entity.getCostCenter().getId());
    }

    public DivisionWithParentDto createDivisionWithParent(DivisionEntity entity) {
        DivisionEntity parent = entity.getParent();
        return new DivisionWithParentDto(
            entity.getId(),
            entity.getPrecursorId(),
            entity.getDateFrom(),
            entity.getDateTo(),
            entity.getLegalEntityId(),
            entity.getStructure() != null ? entity.getStructure().getId() : null,
            entity.getFullName(),
            entity.getShortName(),
            entity.getAbbreviation(),
            entity.getStatus() != null ? entity.getStatus().getId().longValue() : null,
            entity.getGroupId(),
            entity.getExternalId(),
            entity.getCostCenter() == null ? null : entity.getCostCenter().getId(),
            parent != null ? createDivisionWithParent(parent) : null,
            entity.getLegalEntityEntity() != null ? LegalEntityFactory.create(entity.getLegalEntityEntity()) : null
        );
    }
}
