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
public class PositionImportanceDto {
    private Integer id;
    private String name;
    private Long successorCountMax;
    private Long successorCountRec;
    private String description;
    private Long index;
    private Date dateFrom;
    private Date dateTo;
}
