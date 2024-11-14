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
public class PositionCategoryDto {
    private Long id;
    private String fullName;
    private String shortName;
    private String abbreviation;
    private Date dateFrom;
    private Date dateTo;
}
