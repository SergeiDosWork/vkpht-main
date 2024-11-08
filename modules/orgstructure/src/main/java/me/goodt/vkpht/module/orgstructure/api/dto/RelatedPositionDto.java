package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatedPositionDto {
    private Long taskId;
    private String componentFieldCode;
    private String positionShortName;
    private String divisionShortName;
    private String personName;
    private String personSurname;
    private String personPhoto;
    private String positionRelationCommitDate;
    private String statusName;
}
