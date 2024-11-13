package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionExtendedDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.*;

@UtilityClass
public class PositionFactory {

    public static PositionDto create(PositionEntity position/*, JobTitleEntity jobTitle*/) {
        return new PositionDto(
            position.getId(),
            position.getPrecursor() == null ? null : position.getPrecursor().getId(),
            position.getDateFrom(),
            position.getDateTo(),
            position.getDivision() == null ? null : position.getDivision().getId(),
            position.getJobTitle() == null ? null : position.getJobTitle().getId(),
            position.getWorkplace() == null ? null : position.getWorkplace().getId(),
            position.getWorkFunction() == null ? null : position.getWorkFunction().getId(),
            position.getFullName(),
            position.getShortName(),
            position.getAbbreviation(),
            position.getCategory() == null ? null : position.getCategory().getId(),
            position.getRank() == null ? null : position.getRank().getId(),
            position.getStatus() == null ? null : position.getStatus().getId(),
            position.getPositionType() == null ? null : position.getPositionType().getId(),
            position.getStake(),
            position.getIsKey(),
            position.getIsVariable(),
            position.getPositionImportance() == null ? null : position.getPositionImportance().getId(),
            position.getIsKeyManagement(),
            position.getJobTitle() == null ? null : JobTitleFactory.create(position.getJobTitle()),
            position.getExternalId(),
            position.getDivision() == null ? null : position.getDivision().getShortName(),
            position.getCostCenter() == null ? null : position.getCostCenter().getId(),
            position.getDivision() == null ? null : DivisionFactory.create(position.getDivision()));
    }

    public static PositionExtendedDto createExtended(PositionEntity position, List<PositionAssignmentEntity> assignments,
                                                     List<PositionGradeEntity> positionGrades, List<PositionKrLevelEntity> positionKrLevels, List<DivisionTeamEntity> divisionTeams,
                                                     List<PositionSuccessorReadinessEntity> readiness) {
        return new PositionExtendedDto(
            position.getId(),
            position.getPrecursor() == null ? null : position.getPrecursor().getId(),
            position.getDateFrom(),
            position.getDateTo(),
            position.getDivision() == null ? null : position.getDivision().getId(),
            position.getJobTitle() == null ? null : position.getJobTitle().getId(),
            position.getWorkplace() == null ? null : position.getWorkplace().getId(),
            position.getWorkFunction() == null ? null : position.getWorkFunction().getId(),
            position.getFullName(),
            position.getShortName(),
            position.getAbbreviation(),
            position.getCategory() == null ? null : position.getCategory().getId(),
            position.getRank() == null ? null : position.getRank().getId(),
            position.getStatus() == null ? null : position.getStatus().getId(),
            position.getStake(),
            position.getIsKey(),
            position.getIsVariable(),
            position.getPositionImportance() == null ? null : position.getPositionImportance().getId(),
            position.getIsKeyManagement(),
            position.getJobTitle() == null ? null : JobTitleFactory.create(position.getJobTitle()),
            position.getExternalId(),
            position.getDivision() == null ? null : position.getDivision().getShortName(),
            position.getDivision() == null ? null : DivisionFactory.create(position.getDivision()),
            assignments.stream().map(PositionAssignmentFactory::createExtended).collect(Collectors.toList()),
            positionGrades.stream().map(PositionGradeFactory::create).collect(Collectors.toList()),
            positionKrLevels.stream().map(PositionKrLevelFactory::create).collect(Collectors.toList()),
            position.getPositionType() == null ? null : position.getPositionType().getId(),
            position.getCostCenter() == null ? null : position.getCostCenter().getId(),
            divisionTeams.stream().map(DivisionTeamFactory::create).collect(Collectors.toList()),
            readiness.stream().map(PositionSuccessorReadinessFactory::create).collect(Collectors.toList())
            //successors.stream().map(PositionSuccessorFactory::create).collect(Collectors.toList())
        );
    }
}
