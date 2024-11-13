package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static me.goodt.vkpht.module.notification.application.utils.DataUtils.extractNumberOfDays;
import static me.goodt.vkpht.module.notification.application.utils.DataUtils.getChangedDate;
import static me.goodt.vkpht.module.notification.application.utils.DataUtils.getStringDateByPattern;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.CALENDAR_DATES_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.CALENDAR_DATES_INFO_CURRENT_YEAR;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.CALENDAR_DATES_INFO_NEXT_YEAR;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.CALENDAR_DATES_INFO_TODAY;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.CALENDAR_DATES_INFO_TOKEN_20_DAY_OF_MONTH;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.CURRENT_YEAR;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_PATTERN;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.MONTH_YEAR_PATTERN;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.NEXT_YEAR;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TODAY;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TOKEN_20_DAY_OF_MONTH;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.YEAR_PATTERN;

@Component
@Slf4j
public class CalendarDatesGroupResolver implements TokenGroupResolver {

	private static final String DAYS_DELTA = "days_delta";

	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		log.debug(LOG_MESSAGE_GROUP, CALENDAR_DATES_INFO);
		List<TokenWithValues> tokens = context.getParsedTokens().get(CALENDAR_DATES_INFO);
		Date now = new Date();
		tokens.forEach(token -> {
			if (token.getBasicValue().equals(TODAY)) {
				if (token.getConstAndValue() == null) {
					log.info(LOG_MESSAGE_TOKEN, CALENDAR_DATES_INFO, TODAY);
					resolvedTokenValues.put(CALENDAR_DATES_INFO_TODAY, getStringDateByPattern(DATE_PATTERN, now));
				} else {
					if (token.getConstAndValue()[0].equals(DAYS_DELTA)) {
						String date = getStringDateByPattern(DATE_PATTERN, getChangedDate(now, Calendar.DATE, extractNumberOfDays(token.getConstAndValue()[1])));
						if (date != null) {
							log.info("There is {} group and {}.{}.{} token", CALENDAR_DATES_INFO, TODAY, token.getConstAndValue()[0], token.getConstAndValue()[1]);
							resolvedTokenValues.put(
								String.format("%s.%s.%s", CALENDAR_DATES_INFO_TODAY, token.getConstAndValue()[0], token.getConstAndValue()[1]),
								date
							);
						}
					}
				}
			}
			if (token.getBasicValue().equals(CURRENT_YEAR)) {
				log.info(LOG_MESSAGE_TOKEN, CALENDAR_DATES_INFO, CURRENT_YEAR);
				resolvedTokenValues.put(CALENDAR_DATES_INFO_CURRENT_YEAR, getStringDateByPattern(YEAR_PATTERN, now));
			}
			if (token.getBasicValue().equals(NEXT_YEAR)) {
				log.info(LOG_MESSAGE_TOKEN, CALENDAR_DATES_INFO, NEXT_YEAR);
				resolvedTokenValues.put(CALENDAR_DATES_INFO_NEXT_YEAR, getStringDateByPattern(YEAR_PATTERN, getChangedDate(now, Calendar.YEAR, 1)));
			}
			if (token.getBasicValue().equals(TOKEN_20_DAY_OF_MONTH)) {
				log.info(LOG_MESSAGE_TOKEN, CALENDAR_DATES_INFO, TOKEN_20_DAY_OF_MONTH);
				String date = getStringDateByPattern(MONTH_YEAR_PATTERN, now);
				resolvedTokenValues.put(CALENDAR_DATES_INFO_TOKEN_20_DAY_OF_MONTH, "%d.%s".formatted(20, date));
			}
		});
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return Arrays.asList(
			new NotificationToken(CALENDAR_DATES_INFO, TODAY, "Сегодняшняя дата в формате \"ДД.ММ.ГГГГ\""),
			new NotificationToken(CALENDAR_DATES_INFO, CURRENT_YEAR, "Текущий год в формате \"ГГГГ\""),
			new NotificationToken(CALENDAR_DATES_INFO, NEXT_YEAR, "Следующий год в формате \"ГГГГ\""),
			new NotificationToken(CALENDAR_DATES_INFO, TOKEN_20_DAY_OF_MONTH, "20 число текущего месяца \"ДД.ММ.ГГГГ\"")
		);
	}

}
