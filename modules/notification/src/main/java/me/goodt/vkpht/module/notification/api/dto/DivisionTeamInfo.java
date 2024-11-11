package me.goodt.vkpht.module.notification.api.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

import com.goodt.drive.notify.application.dto.orgstructure.DivisionTeamAssignmentDto;
import com.goodt.drive.notify.application.dto.orgstructure.EmployeeInfoDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DivisionTeamInfo {
	@EqualsAndHashCode.Include
	private Long divisionTeamId;
	private DivisionTeamAssignmentDto headAssignment;
	private List<EmployeeInfoDto> employees;
	private Set<Long> employeeIds;

	@Override
	public String toString() {
		if (headAssignment != null) {
			return String.format("DivisionTeamInfo{divisionTeamId=%d, headAssignmentId=%d, employeeIds=%s}", divisionTeamId, headAssignment.getId(), employeeIds);
		} else {
			return "DivisionTeamInfo{divisionTeamId=%d, headAssignmentId=null, employeeIds=%s}".formatted(divisionTeamId, employeeIds);
		}
	}
}
