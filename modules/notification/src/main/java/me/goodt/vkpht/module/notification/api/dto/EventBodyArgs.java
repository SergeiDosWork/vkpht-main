package me.goodt.vkpht.module.notification.api.dto;

import lombok.Builder;
import lombok.Getter;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;

@Getter
@Builder
public class EventBodyArgs {
    private Long userId;
    private Long taskId;
    private String bodyTitle;
    private Long eventType;
    private String buttonLinkOne;
    private String buttonLinkTwo;
    private DivisionTeamAssignmentDto employeeAssignment;
    private DivisionTeamAssignmentDto expertAssignment;
}
