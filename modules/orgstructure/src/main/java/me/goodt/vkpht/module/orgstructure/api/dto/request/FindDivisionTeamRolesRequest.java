package me.goodt.vkpht.module.orgstructure.api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FindDivisionTeamRolesRequest {

    private Integer page;
    private Integer size;
    private Boolean successorReadinessDateFromPlusYear;
    private Date successorReadinessDateFromStart;
    private Date successorReadinessDateFromEnd;
    private Date assignmentRotationDateFromStart;
    private Date assignmentRotationDateFromEnd;
    private Date successorDateFrom;
    private List<Long> divisionTeamRoleIds;
    private List<Long> divisionTeamIds;
    private Long divisionId;
    private String searchingValue;
    private Long employeeSuccessorId;
    private List<Long> legalEntityIds;
    private boolean isDivisionTeamRoleIdOnlyParam;

}
