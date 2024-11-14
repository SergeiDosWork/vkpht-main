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
public class PlacementDto {
    private Long id;
    private Long precursorId;
    private Date dateFrom;
    private Date dateTo;
    private Long workplaceId;
    private String code;
    private String name;

}
