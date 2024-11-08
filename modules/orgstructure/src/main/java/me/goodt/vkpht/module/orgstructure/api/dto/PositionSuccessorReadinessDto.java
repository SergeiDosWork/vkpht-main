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
public class PositionSuccessorReadinessDto {
    private Long id;
    private PositionSuccessorDto positionSuccessor;
    private AssignmentReadinessDto readiness;
    private Date dateFrom;
    private Date dateTo;
}
