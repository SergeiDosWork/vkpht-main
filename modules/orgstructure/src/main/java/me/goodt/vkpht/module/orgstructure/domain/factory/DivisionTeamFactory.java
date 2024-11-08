package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.*;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamShort;

@UtilityClass
public final class DivisionTeamFactory {
    public static DivisionTeamDto create(DivisionTeamEntity entity) {
        DivisionEntity division = entity.getDivision();
        return new DivisionTeamDto(
                entity.getId(),
                entity.getParent() != null ? entity.getParent().getId() : null,
                entity.getPrecursor() != null ? entity.getPrecursor().getId() : null,
                entity.getDateFrom(),
                entity.getDateTo(),
                division != null ? division.getId() : null,
                division != null ? division.getShortName() : null,
                entity.getFullName(),
                entity.getShortName(),
                entity.getAbbreviation(),
                entity.getIsHead(),
                entity.getExternalId()
        );
    }

    public static DivisionTeamStatDto createStat(DivisionTeamEntity entity, Integer assignmentsCount, Integer rotationsCount,
                                                 Long approvedAssignmentsCount, List<AssignmentRotationStatDto> rotations) {
        return new DivisionTeamStatDto(
                entity.getId(),
                entity.getParent() != null ? entity.getParent().getId() : null,
                entity.getPrecursor() != null ? entity.getPrecursor().getId() : null,
                entity.getDateFrom(),
                entity.getDateTo(),
                entity.getDivision() != null ? entity.getDivision().getId() : null,
                entity.getDivision() != null ? entity.getDivision().getShortName() : null,
                entity.getFullName(),
                entity.getShortName(),
                entity.getAbbreviation(),
                entity.getIsHead(),
                entity.getExternalId(),
                assignmentsCount,
                rotationsCount,
                approvedAssignmentsCount,
                rotations
        );
    }

    public DivisionTeamFullDto createFull(DivisionTeamEntity entity) {
        DivisionEntity division = entity.getDivision();
        return new DivisionTeamFullDto(
                entity.getId(),
                entity.getParent() != null ? entity.getParent().getId() : null,
                entity.getPrecursor() != null ? entity.getPrecursor().getId() : null,
                entity.getDateFrom(),
                entity.getDateTo(),
                division != null ? division.getId() : null,
                division != null ? division.getShortName() : null,
                entity.getFullName(),
                entity.getShortName(),
                entity.getAbbreviation(),
                entity.getIsHead(),
                entity.getExternalId(),
                division != null ? DivisionFactory.createDivisionWithParent(division) : null
        );
    }

    public static DivisionTeamShortDto createShort(DivisionTeamShort sh) {
        return new DivisionTeamShortDto(sh.getId(), sh.getParentId(), sh.getPrecursorId(), sh.getDateFrom(), sh.getDateTo(),
        sh.getDivisionId(), sh.getTypeId(), sh.getStatusId(), sh.getFullName(), sh.getShortName(), sh.getAbbreviation(),
        sh.getIsHead(), sh.getExternalId());
    }
}
