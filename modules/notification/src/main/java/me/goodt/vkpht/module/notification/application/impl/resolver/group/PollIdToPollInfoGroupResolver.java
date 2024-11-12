package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.goodt.vkpht.module.notification.api.dto.quiz.PollRawDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static com.goodt.drive.notify.application.utils.DataUtils.getStringDateByPattern;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_END;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_PATTERN;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_START;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.NAME;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_POLL_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_POLL_INFO_DATE_END;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_POLL_INFO_DATE_START;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_POLL_INFO_NAME;

@Component
@Slf4j
public class PollIdToPollInfoGroupResolver implements TokenGroupResolver {

	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		log.info(LOG_MESSAGE_GROUP, POLL_ID_TO_POLL_INFO);
		Integer pollId = (Integer) context.getParameters().get(POLL_ID_TO_POLL_INFO);
		log.info("poll_id = {}", pollId);
		if (Objects.isNull(pollId)) {
			return;
		}
		try {
			PollRawDto poll = context.getResolverServiceContainer().getQuizServiceClient().getPoll(pollId.longValue());
			for (TokenWithValues token : context.getParsedTokens().get(POLL_ID_TO_POLL_INFO)) {
				if (token.getBasicValue().equals(NAME)) {
					log.info(LOG_MESSAGE_TOKEN, POLL_ID_TO_POLL_INFO, NAME);
					resolvedTokenValues.put(POLL_ID_TO_POLL_INFO_NAME, poll.getName());
				}
				if (token.getBasicValue().equals(DATE_START)) {
					log.info(LOG_MESSAGE_TOKEN, POLL_ID_TO_POLL_INFO, DATE_START);
					String dateStart = getStringDateByPattern(DATE_PATTERN, poll.getDateStart());
					if (dateStart != null) {
						resolvedTokenValues.put(POLL_ID_TO_POLL_INFO_DATE_START, dateStart.concat(YEAR_SYMBOL));
					}
				}
				if (token.getBasicValue().equals(DATE_END)) {
					log.info(LOG_MESSAGE_TOKEN, POLL_ID_TO_POLL_INFO, DATE_END);
					String dateEnd = getStringDateByPattern(DATE_PATTERN, poll.getDateEnd());
					if (dateEnd != null) {
						resolvedTokenValues.put(POLL_ID_TO_POLL_INFO_DATE_END, dateEnd.concat(YEAR_SYMBOL));
					}
				}
			}
		} catch (Exception ex) {
			log.error(LOG_ERROR, ex.getMessage());
		}
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return Arrays.asList(
			new NotificationToken(POLL_ID_TO_POLL_INFO, NAME, "Наименование опроса"),
			new NotificationToken(POLL_ID_TO_POLL_INFO, DATE_START, "Дата окончания опроса"),
			new NotificationToken(POLL_ID_TO_POLL_INFO, DATE_END, "Дата старта опроса")
		);
	}
}
