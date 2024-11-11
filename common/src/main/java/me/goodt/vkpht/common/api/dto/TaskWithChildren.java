package me.goodt.vkpht.common.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.goodt.drive.rtcore.dto.tasksetting.TaskDto;

@Getter
@Setter
@RequiredArgsConstructor
public class TaskWithChildren {
    private final TaskDto task;
    private final List<TaskDto> children;
}
