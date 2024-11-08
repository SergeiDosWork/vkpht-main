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
public class PositionSuccessorReadinessRawDto {
    private Long id;
    private Long positionSuccessorId;
    private Integer readinessId;
    private Date dateFrom;
    private Date dateTo;
}
