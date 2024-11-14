package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Pavel Khovaylo
 */
@Getter
@Setter
@NoArgsConstructor
public class DivisionTeamAssignmentRotationDto extends DivisionTeamAssignmentRotationShortDto {

    public DivisionTeamAssignmentRotationDto(Long id, Date dateFrom, Date dateTo, Date dateCommitHr, AssignmentRotationDto rotation, String commentHr,
                                             String commentEmployee, DivisionTeamAssignmentDto assignment) {
        super(id, dateFrom, dateTo, dateCommitHr, rotation, commentHr, commentEmployee);
        this.assignment = assignment;
    }

    private DivisionTeamAssignmentDto assignment;
}
