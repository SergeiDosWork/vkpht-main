package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionPositionImportanceDto {
    private Long id;
    private Date dateFrom;
    private Date dateTo;

    private Long position;
    private Integer positionImportance;
    private String positionImportanceName;
    private Long authorEmployeeId;
    private Integer systemRoleId;

    private PersonSimpleDto person;
}
