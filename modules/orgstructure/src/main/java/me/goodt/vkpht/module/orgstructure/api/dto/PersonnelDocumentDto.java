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
public class PersonnelDocumentDto {
    private Long id;
    private Long employeeId;
    private Long precursorId;
    private Integer typeId;
    private Integer formId;
    private String name;
    private Date dateFrom;
    private Date dateTo;
    private String data;
    private String externalId;
}
