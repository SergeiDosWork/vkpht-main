package me.goodt.vkpht.common.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.goodt.drive.rtcore.model.tasksetting.entities.OldStatusNameEntity;
import com.goodt.drive.rtcore.model.tasksetting.entities.OldTaskEntity;
import com.goodt.drive.rtcore.model.tasksetting.entities.OldTaskFieldEntity;
import com.goodt.drive.rtcore.model.tasksetting.entities.OldTaskTypeFieldEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskInfo {
	private OldTaskEntity task;
	private List<OldTaskFieldEntity> taskFields;
	private List<OldTaskTypeFieldEntity> taskFieldsTypes;
	private List<OldStatusNameEntity> taskStatusNames;
}
