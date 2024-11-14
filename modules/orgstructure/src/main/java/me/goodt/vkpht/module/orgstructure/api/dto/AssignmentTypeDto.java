package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentTypeDto {
    private Integer id;
    private String fullName;
    private String shortName;
    private String abbreviation;
    private Date dateFrom;
    private Date dateTo;
}
