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
public class LegalEntityTeamAssignmentDto {
    private Long id;
    private Long precursorId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateTo;
    private Integer typeId;
    private EmployeePersonDto employee;
    private LegalEntityTeamDto legalEntityTeam;
    private RoleDto role;
    private String fullName;
    private String shortName;
    private String abbreviation;
    private AssignmentStatusDto status;
}
