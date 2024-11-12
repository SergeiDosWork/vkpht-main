package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.goodt.vkpht.module.notification.api.dto.tasksetting2.ProcessDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.ProcessTaskDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static com.goodt.drive.notify.application.utils.DataUtils.extractNumberOfDays;
import static com.goodt.drive.notify.application.utils.DataUtils.getChangedDate;
import static com.goodt.drive.notify.application.utils.DataUtils.getStringDateByPattern;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_END;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_PATTERN;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_START;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.NAME;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_PROCESS_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_PROCESS_INFO_DATE_END;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_PROCESS_INFO_DATE_START;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_PROCESS_INFO_NAME;

@Component
@Slf4j
public class TaskIdToProcessInfoGroupResolver implements TokenGroupResolver {

	private static final String PROCESS_TASK_BY_TASK_ID_IS_NOT_FOUND = "process_task by task_id={} is not found";
	private static final String PROCESS_TASK_BY_TASK_ID_IS_FOUND = "process_task by task_id={} is found";

	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		Integer taskId = (Integer) context.getParameters().get(TASK_ID_TO_PROCESS_INFO);
		log.debug(TASK_ID_IS, taskId);
		if (Objects.isNull(taskId)) {
			return;
		}

		List<TokenWithValues> tokens = context.getParsedTokens().get(TASK_ID_TO_PROCESS_INFO);
		List<ProcessTaskDto> processTaskList = null;
		try {
			processTaskList = context.getResolverServiceContainer().getTasksettingServiceClient().getProcessTask(null, taskId.longValue());
		} catch (Exception ex) {
			log.error("process_task list by task_id={} is not found", taskId);
		}
		if (CollectionUtils.isEmpty(processTaskList)) {
			log.debug(PROCESS_TASK_BY_TASK_ID_IS_NOT_FOUND, taskId);
			return;
		}
		log.debug(PROCESS_TASK_BY_TASK_ID_IS_FOUND, taskId);
		ProcessDto process = processTaskList.getFirst().getProcess();
		for (TokenWithValues token : tokens) {
			if (token.getBasicValue().equals(NAME)) {
				log.debug(LOG_MESSAGE_TOKEN, TASK_ID_TO_PROCESS_INFO, NAME);
				resolvedTokenValues.put(TASK_ID_TO_PROCESS_INFO_NAME, process.getName());
			}
			if (token.getBasicValue().equals(DATE_START)) {
				if (token.getConstAndValue() == null) {
					log.debug(LOG_MESSAGE_TOKEN, TASK_ID_TO_PROCESS_INFO, DATE_START);
					String dateStart = getStringDateByPattern(DATE_PATTERN, process.getDateStart());
					if (dateStart != null) {
						resolvedTokenValues.put(TASK_ID_TO_PROCESS_INFO_DATE_START, dateStart.concat(YEAR_SYMBOL));
					}
				} else {
					if (token.getConstAndValue()[0].equals(DAYS_DELTA)) {
						String dateStart = getStringDateByPattern(DATE_PATTERN, getChangedDate(process.getDateStart(), Calendar.DATE, extractNumberOfDays(token.getConstAndValue()[1])));
						if (dateStart != null) {
							log.info("There is {} group and {}.{}.{} token", TASK_ID_TO_PROCESS_INFO, DATE_START, token.getConstAndValue()[0], token.getConstAndValue()[1]);
							resolvedTokenValues.put(
								String.format("%s.%s.%s", TASK_ID_TO_PROCESS_INFO_DATE_START, token.getConstAndValue()[0], token.getConstAndValue()[1]),
								dateStart.concat(YEAR_SYMBOL)
							);
						}
					}
				}
			}
			if (token.getBasicValue().equals(DATE_END)) {
				if (token.getConstAndValue() == null) {
					log.debug(LOG_MESSAGE_TOKEN, TASK_ID_TO_PROCESS_INFO, DATE_END);
					String dateEnd = getStringDateByPattern(DATE_PATTERN, process.getDateEnd());
					if (dateEnd != null) {
						resolvedTokenValues.put(TASK_ID_TO_PROCESS_INFO_DATE_END, dateEnd.concat(YEAR_SYMBOL));
					}
				} else {
					if (token.getConstAndValue()[0].equals(DAYS_DELTA)) {
						String dateEnd = getStringDateByPattern(DATE_PATTERN, getChangedDate(process.getDateEnd(), Calendar.DATE, extractNumberOfDays(token.getConstAndValue()[1])));
						if (dateEnd != null) {
							log.debug("There is {} group and {}.{}.{} token", TASK_ID_TO_PROCESS_INFO, DATE_END, token.getConstAndValue()[0], token.getConstAndValue()[1]);
							resolvedTokenValues.put(
								String.format("%s.%s.%s", TASK_ID_TO_PROCESS_INFO_DATE_END, token.getConstAndValue()[0], token.getConstAndValue()[1]),
								dateEnd.concat(YEAR_SYMBOL)
							);
						}
					}
				}
			}
		}
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return Arrays.asList(
			new NotificationToken(TASK_ID_TO_PROCESS_INFO, NAME, "Название процесса"),
			new NotificationToken(TASK_ID_TO_PROCESS_INFO, DATE_START, "Дата старта процесса"),
			new NotificationToken(TASK_ID_TO_PROCESS_INFO, DATE_START + "." + DAYS_DELTA + ".N", "Дата старта процесса +/- N дней"),
			new NotificationToken(TASK_ID_TO_PROCESS_INFO, DATE_END, "Дата окончания процесса"),
			new NotificationToken(TASK_ID_TO_PROCESS_INFO, DATE_END + "." + DAYS_DELTA + ".N", "Дата окончания процесса +/- N дней")
		);
	}
}
