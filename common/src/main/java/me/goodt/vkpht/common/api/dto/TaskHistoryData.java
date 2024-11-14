package me.goodt.vkpht.common.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.goodt.drive.rtcore.model.tasksetting.entities.OldTaskHistoryEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskHistoryData {
	private Long taskId;
	private List<OldTaskHistoryEntity> history;
}
