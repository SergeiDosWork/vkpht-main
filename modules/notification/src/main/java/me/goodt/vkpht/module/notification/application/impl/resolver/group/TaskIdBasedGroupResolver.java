package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskFindRequest;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;

public interface TaskIdBasedGroupResolver {
	default Optional<TaskDto> getTaskDto(Integer taskId, ResolverContext context) {
		if (Objects.isNull(taskId)) {
			return Optional.empty();
		}
		TaskDto task = (TaskDto) context.getOrResolveObject(SavedObjectNames.TASK, () -> {
			List<TaskDto> tasks = context.getResolverServiceContainer().getTasksettingServiceClient().taskFind(new TaskFindRequest().setIds(Collections.singletonList(taskId.longValue())));
			if (!CollectionUtils.isEmpty(tasks)) {
				return tasks.getFirst();
			}
			return null;
		});
		return Optional.ofNullable(task);
	}
}
