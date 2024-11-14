package me.goodt.vkpht.module.orgstructure.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PositionAssignmentExtendedDto {
    private Long id;
    private Integer typeId;
    private Long precursorId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateTo;
    private EmployeeExtendedDto employee;
    private Long positionId;
    private Integer statusId;
    private Long placementId;
    private Integer substitutionTypeId;
    private String fullName;
    private String shortName;
    private String abbreviation;
    private Long categoryId;
    private Float stake;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date probationDateTo;
    private PositionDto position;
    private AssignmentTypeDto assignmentType;
    private String externalId;
}
