package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class WorkFunctionDto extends FullShortNameAbbreviationDto {

    public WorkFunctionDto(Long id, Long precursorId, Date dateFrom, Date dateTo, String fullName, String shortName,
                           String abbreviation, Long functionId, Integer isRequiredCertificate, Integer statusId) {
        super(id, fullName, shortName, abbreviation);
        this.precursorId = precursorId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.functionId = functionId;
        this.isRequiredCertificate = isRequiredCertificate;
        this.statusId = statusId;
    }

    private Long precursorId;
    private Date dateFrom;
    private Date dateTo;
    private Long functionId;
    private Integer isRequiredCertificate;
    private Integer statusId;
}
