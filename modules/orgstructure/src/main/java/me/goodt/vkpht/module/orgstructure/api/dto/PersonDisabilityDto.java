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
public class PersonDisabilityDto {
    private Long id;
    private Long personId;
    private String disabilityName;
    private String disabilityDescription;
    private Date dateFrom;
    private Date dateTo;
    private Long disabilityCategoryId;
    private Date dateStart;
    private Date dateEnd;
}
