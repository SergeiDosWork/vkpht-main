package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_START;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_START_PARAMS;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_PRACTICE_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_PRACTICE_INFO_DATE_START;

@Component
@Slf4j
public class TaskIdToPracticeInfoGroupResolver implements TokenGroupResolver, TaskIdBasedGroupResolver {

	private static final String TASK_FIELD_BY_TASK_FIELD_TYPE_PARAMS_IS_NOT_FOUND = "task_field with task_field_type.params={} is not found";

	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		log.info(LOG_MESSAGE_GROUP, TASK_ID_TO_PRACTICE_INFO);
		Integer taskId = (Integer) context.getParameters().get(TASK_ID_TO_PRACTICE_INFO);
		Optional<TaskDto> taskDto = getTaskDto(taskId, context);
		if (taskDto.isEmpty()) {
			return;
		}

		List<TokenWithValues> tokens = context.getParsedTokens().get(TASK_ID_TO_PRACTICE_INFO);
		for (TokenWithValues token : tokens) {
			if (token.getBasicValue().equals(DATE_START)) {
				taskDto.get().getFields()
					.stream()
					.filter(tf -> tf.getType().getParams().equals(DATE_START_PARAMS))
					.findFirst()
					.ifPresentOrElse(
						tf -> resolvedTokenValues.put(TASK_ID_TO_PRACTICE_INFO_DATE_START, tf.getValue()),
						() -> log.info(TASK_FIELD_BY_TASK_FIELD_TYPE_PARAMS_IS_NOT_FOUND, DATE_START_PARAMS)
					);
			}
		}
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return List.of(
			new NotificationToken(TASK_ID_TO_PRACTICE_INFO, DATE_START, "Дата старта мероприятия практика")
		);
	}
}
