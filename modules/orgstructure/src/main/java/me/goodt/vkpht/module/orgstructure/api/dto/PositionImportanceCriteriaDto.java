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
public class PositionImportanceCriteriaDto {
    private Long id;
    private Date dateFrom;
    private Date dateTo;
    private Long positionId;
    private Long importanceCriteriaId;
    private float weight;
    private float value;
}
