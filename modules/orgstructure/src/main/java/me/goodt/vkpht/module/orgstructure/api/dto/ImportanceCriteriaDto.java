package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportanceCriteriaDto {
    private Long id;
    private Long groupId;
    private String name;
    private String description;
    private float weight;
    private Boolean isEnabled;
    private CalculationMethodDto calculationMethod;
}
