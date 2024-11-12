package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.notification.application.impl.ResolverContext;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_STATUSCHANGE_COMMENT;

@Slf4j
@Component
public class TaskStatuschangeCommentGroupResolver implements TokenGroupResolver {
	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		log.info(LOG_MESSAGE_GROUP, TASK_STATUSCHANGE_COMMENT);
		String comment = (String) context.getParameters().get(TASK_STATUSCHANGE_COMMENT);
		if (comment != null) {
			resolvedTokenValues.put(TASK_STATUSCHANGE_COMMENT, comment);
		}
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return List.of(
			new NotificationToken(TASK_STATUSCHANGE_COMMENT, null, "Комментарий при обновлении статуса таска")
		);
	}

}
