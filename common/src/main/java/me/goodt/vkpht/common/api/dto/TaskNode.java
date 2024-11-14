package me.goodt.vkpht.common.api.dto;

import com.goodt.drive.rtcore.model.tasksetting.entities.OldStatusNameEntity;
import com.goodt.drive.rtcore.model.tasksetting.entities.OldTaskEntity;
import com.goodt.drive.rtcore.model.tasksetting.entities.OldTaskFieldEntity;
import com.goodt.drive.rtcore.model.tasksetting.entities.OldTaskTypeFieldEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskNode {
	private OldTaskEntity parentNode;
	private OldTaskEntity node;
	private List<OldStatusNameEntity> statusNames;
	private List<OldTaskFieldEntity> nodeFields;
	private List<OldTaskTypeFieldEntity> taskFieldsTypes;
	private Integer nodeLevel;
	private List<OldTaskEntity> childrenNodes;
}
