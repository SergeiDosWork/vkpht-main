package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecalculatePositionImportanceCriteriaDto {
    private Long importanceCriteriaId;
    private Long positionId;
    private float value;
}
