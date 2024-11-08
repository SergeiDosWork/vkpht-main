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
public class WorkplaceDto {
    private Long id;
    private Long precursorId;
    private String code;
    private String hash;
    private Long locationId;
    private Integer number;
    private Integer isBase;
    LocationDto location;
    private Date dateFrom;
    private Date dateTo;
}
