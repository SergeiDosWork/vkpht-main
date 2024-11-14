package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DivisionTeamStatDto extends DivisionTeamDto {

    public DivisionTeamStatDto(Long id, Long parentId, Long precursorId, Date dateFrom, Date dateTo, Long divisionId, String divisionShortName,
                               String fullName, String shortName, String abbreviation, Integer isHead, String externalId,
                               Integer assignmentsCount, Integer rotationsCount, Long approvedAssignmentsCount,
                               List<AssignmentRotationStatDto> rotations) {
        super(id, parentId, precursorId, dateFrom, dateTo, divisionId, divisionShortName, fullName, shortName, abbreviation, isHead, externalId);
        this.assignmentsCount = assignmentsCount;
        this.rotationsCount = rotationsCount;
        this.approvedAssignmentsCount = approvedAssignmentsCount;
        this.rotations = rotations;
    }

    private Integer assignmentsCount;
    private Integer rotationsCount;
    private Long approvedAssignmentsCount;
    private List<AssignmentRotationStatDto> rotations;
}
