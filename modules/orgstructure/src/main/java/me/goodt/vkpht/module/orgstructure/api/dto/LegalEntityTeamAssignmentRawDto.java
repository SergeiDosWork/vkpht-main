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
public class LegalEntityTeamAssignmentRawDto {
    private Long id;
    private Long precursorId;
    private Date dateFrom;
    private Date dateTo;
    private Integer typeId;
    private Long employeeId;
    private Long legalEntityTeamId;
    private Long roleId;
    private String fullName;
    private String shortName;
    private String abbreviation;
    private Integer statusId;
}
