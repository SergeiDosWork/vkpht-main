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
public class PositionGroupPositionDto {
    private Long id;
    private Long positionId;
    private Long positionGroupId;
    private Date dateFrom;
    private Date dateTo;
}
