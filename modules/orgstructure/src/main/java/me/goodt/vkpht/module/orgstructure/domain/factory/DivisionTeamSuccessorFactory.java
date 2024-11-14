package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorReadinessDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorShortDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamSuccessorEntity;

@UtilityClass
public class DivisionTeamSuccessorFactory {
    public static DivisionTeamSuccessorDto create(DivisionTeamSuccessorEntity entity, List<DivisionTeamSuccessorReadinessDto> divisionTeamSuccessorReadiness,
                                                  DivisionTeamDto divisionTeam) {
        return new DivisionTeamSuccessorDto(
            entity.getId(),
            entity.getDateFrom(),
            entity.getDateTo(),
            entity.getDateCommitHr(),
            entity.getEmployee() != null ? EmployeeInfoFactory.create(entity.getEmployee()) : null,
            entity.getDatePriority(),
            entity.getReasonIdInclusion(),
            entity.getReasonIdExclusion(),
            entity.getCommentInclusion(),
            entity.getCommentExclusion(),
            entity.getDocumentUrlInclusion(),
            entity.getDocumentUrlExclusion(),
            divisionTeamSuccessorReadiness, divisionTeam,
            entity.getDivisionTeamRole() != null ? DivisionTeamRoleFactory.create(entity.getDivisionTeamRole()) : null
        );
    }

    public static DivisionTeamSuccessorDto create(DivisionTeamSuccessorEntity entity) {
        return create(entity, null, null);
    }

    public static DivisionTeamSuccessorShortDto createShort(DivisionTeamSuccessorEntity entity,
                                                            List<DivisionTeamSuccessorReadinessDto> divisionTeamSuccessorReadiness,
                                                            DivisionTeamDto divisionTeam) {
        return new DivisionTeamSuccessorShortDto(
            entity.getId(),
            entity.getDateFrom(),
            entity.getDateTo(),
            entity.getDateCommitHr(),
            entity.getEmployee() != null ? EmployeeInfoFactory.create(entity.getEmployee()) : null,
            entity.getDatePriority(),
            entity.getReasonIdInclusion(),
            entity.getReasonIdExclusion(),
            entity.getCommentInclusion(),
            entity.getCommentExclusion(),
            entity.getDocumentUrlInclusion(),
            entity.getDocumentUrlExclusion(),
            divisionTeamSuccessorReadiness, divisionTeam
        );
    }

    public static DivisionTeamSuccessorShortDto createShort(DivisionTeamSuccessorEntity entity) {
        return createShort(entity, null, null);
    }
}
