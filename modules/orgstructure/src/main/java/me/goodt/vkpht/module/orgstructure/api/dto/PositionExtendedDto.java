package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PositionExtendedDto extends PositionDto {

    public PositionExtendedDto() {
    }

    public PositionExtendedDto(Long id, Long precursorId, Date dateFrom, Date dateTo, Long divisionId, Long jobTitleId,
                               Long workplaceId, Long workFunctionId, String fullName, String shortName,
                               String abbreviation, Long categoryId, Long rankId, Integer statusId, Float stake,
                               Integer isKey, Integer isVariable, Integer teamRoleImportance, Integer isKeyManagement,
                               JobTitleDto jobTitle, String externalId,
                               String divisionShortName, DivisionDto division, List<PositionAssignmentExtendedDto> assignments, List<PositionGradeDto> positionGradeDto,
                               List<PositionKrLevelDto> positionKrLevelDto, Long positionTypeId, Long costCenterId, List<DivisionTeamDto> divisionTeams,
                               List<PositionSuccessorReadinessDto> readiness) {
        super(id, precursorId, dateFrom, dateTo, divisionId, jobTitleId, workplaceId, workFunctionId, fullName,
              shortName, abbreviation, categoryId, rankId, statusId, positionTypeId, stake, isKey, isVariable, teamRoleImportance, isKeyManagement,
              jobTitle, externalId, divisionShortName, costCenterId, division);
        this.division = division;
        this.assignments = assignments;
        this.positionGradeDto = positionGradeDto;
        this.positionKrLevelDto = positionKrLevelDto;
        this.positionTypeId = positionTypeId;
        this.divisionTeams = divisionTeams;
        this.readiness = readiness;
    }

    private DivisionDto division;
    private List<PositionAssignmentExtendedDto> assignments;
    private List<PositionGradeDto> positionGradeDto;
    private List<PositionKrLevelDto> positionKrLevelDto;
    private Long positionTypeId;
    private List<DivisionTeamDto> divisionTeams;
    private List<PositionSuccessorReadinessDto> readiness;
}
