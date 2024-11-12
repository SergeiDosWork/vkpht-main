package me.goodt.vkpht.module.notification.application.impl.resolver.employee;

import lombok.extern.slf4j.Slf4j;

import me.goodt.vkpht.module.notification.application.impl.resolver.group.NotificationToken;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.notification.api.dto.data.DivisionTeamInfo;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.api.dto.quiz.UserPollDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;
import me.goodt.vkpht.module.notification.application.impl.resolver.recipient.RecipientResolverUtils;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FINISHED;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.NOT_FINISHED;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL_FINISHED;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL_NOT_FINISHED;
import static java.lang.Boolean.TRUE;

@Component
@Slf4j
public class PollIdToCountEmployeeUserPollGroupResolver implements TokenEmployeeResolver {
	@Override
	public void resolve(ResolverContext context, RecipientInfoDto employeeInfoDto, Map<String, String> resolvedTokenValues) {
		// special case: for each recipient their own processed tokens
		log.info(LOG_MESSAGE_GROUP, POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL);
		Integer pollId = (Integer) context.getParameters().get(POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL);
		log.info("poll_id = {}", pollId);
		if (Objects.isNull(pollId)) {
			return;
		}
		try {
			Set<Long> employeeIdsForFilteringUserPollList = new HashSet<>();
			List<UserPollDto> userPollList = (List<UserPollDto>) context.getOrResolveObject(SavedObjectNames.USER_POLL_LIST, () ->
				context.getResolverServiceContainer().getQuizServiceClient().findUserPoll(pollId.longValue(), null, TRUE)
					.stream()
					.filter(up -> !employeeIdsForFilteringUserPollList.contains(up.getEmployeeId()))
					.peek(up -> employeeIdsForFilteringUserPollList.add(up.getEmployeeId()))
					.collect(Collectors.toList()));
			if (CollectionUtils.isEmpty(userPollList)) {
				return;
			}
			List<Long> employeeIds = userPollList.stream().map(UserPollDto::getEmployeeId).collect(Collectors.toList());
			List<DivisionTeamAssignmentDto> assignments = (List<DivisionTeamAssignmentDto>) context.getOrResolveObject(SavedObjectNames.POLL_ASSIGNMENTS, () ->
				context.getResolverServiceContainer().getOrgstructureServiceClient().getAssignments(null, employeeIds));
			if (CollectionUtils.isEmpty(assignments)) {
				return;
			}
			Map<DivisionTeamInfo, Map<String, String>> divisionTeamInfoAndProcessedTokensMap = (Map<DivisionTeamInfo, Map<String, String>>) context.getOrResolveObject(SavedObjectNames.POLL_DIVISION_MAP, () -> {
				Map<DivisionTeamInfo, Map<String, String>> dm = new HashMap<>();
				Map<Long, DivisionTeamInfo> divisionTeamInfoMap = RecipientResolverUtils.getDivisionTeamInfoMap(assignments);
				for (TokenWithValues token : context.getParsedTokens().get(POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL)) {
					if (token.getBasicValue().equals(NOT_FINISHED) || token.getBasicValue().equals(FINISHED)) {
						log.info(LOG_MESSAGE_TOKEN, POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL, token);
						divisionTeamInfoMap.forEach((dt, dtInfo) -> {
							AtomicInteger notFinishedCount = new AtomicInteger();
							userPollList.forEach(up -> {
								if (dtInfo.getEmployeeIds().contains(up.getEmployeeId())) {
									if (up.getDateTo() == null) {
										notFinishedCount.incrementAndGet();
									}
								}
							});
							DivisionTeamAssignmentDto headTeam;
							if (dtInfo.getHeadAssignment() != null) {
								headTeam = dtInfo.getHeadAssignment();
							} else {
								headTeam = context.getResolverServiceContainer().getOrgstructureServiceClient()
									.getEmployeeHead(dtInfo.getEmployees().get(0).getId(), dtInfo.getDivisionTeamId());
							}
							dtInfo.setHeadAssignment(headTeam);

							dm.put(dtInfo,
								   new HashMap<>(
									   Map.of(POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL_FINISHED, String.valueOf(dtInfo.getEmployeeIds().size() - notFinishedCount.get()),
											  POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL_NOT_FINISHED, notFinishedCount.toString()
									   )));
						});
					}
				}
				return dm;
			});

			divisionTeamInfoAndProcessedTokensMap.forEach((dtInfo, map) -> {
				if (((dtInfo.getHeadAssignment() != null) && (dtInfo.getHeadAssignment().getEmployee().getId().equals(employeeInfoDto.getId())))
					|| (dtInfo.getEmployeeIds().contains(employeeInfoDto.getId()))) {
					resolvedTokenValues.put(POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL_FINISHED, map.get(POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL_FINISHED));
					resolvedTokenValues.put(POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL_NOT_FINISHED, map.get(POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL_NOT_FINISHED));
				}
			});
		} catch (Exception ex) {
			log.error(LOG_ERROR, ex.getMessage());
		}
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return Arrays.asList(
			new NotificationToken(POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL, FINISHED, "Кол-во сотрудников, прошедших опрос"),
			new NotificationToken(POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL, NOT_FINISHED, "Кол-во сотрудников, прошедших не опрос")
		);
	}

}
