package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PositionSuccessorRawDto {
    private Date dateCommitHr;
    private Date datePriority;
    private Long employeeId;
    private Long positionId;
    private Long positionGroupId;
    private Long reasonInclusionId;
    private Long reasonExclusionId;
    private String commentInclusion;
    private String commentExclusion;
    private String documentUrlInclusion;
    private String documentUrlExclusion;
    private Date dateFrom;
    private Date dateTo;
}
