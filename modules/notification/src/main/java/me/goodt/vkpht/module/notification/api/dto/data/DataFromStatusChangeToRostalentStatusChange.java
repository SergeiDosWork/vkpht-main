package me.goodt.vkpht.module.notification.api.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataFromStatusChangeToRostalentStatusChange {
	private Long statusId;
	private Long userId;
	private Long employeeId;
	private Long employeeDivisionTeamId;
	private String fioEmployee;
	private Long headTaskUserId;
	private Long headTaskEmployeeId;
	private Long headTaskDivisionTeamId;
	private Long buddyTaskUserId;
	private Long buddyTaskEmployeeId;
	private Long buddyTaskDivisionTeamId;
	private List<Long> hrTaskEmployeeIds;
	private String comment;
}
