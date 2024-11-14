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
public class StructureDto {
    private Long id;
    private Long typeId;
    private Long parentId;
    private Long precursorId;
    private Date dateFrom;
    private Date dateTo;
    public Long locationId;
    private String fullName;
    private String shortName;
    private String abbreviation;
    private Long statusId;
}
