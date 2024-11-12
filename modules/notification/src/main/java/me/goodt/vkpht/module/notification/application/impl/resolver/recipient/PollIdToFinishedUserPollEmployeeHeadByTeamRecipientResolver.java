package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.notification.api.dto.data.DivisionTeamInfo;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.api.dto.quiz.UserPollDto;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_FINISHED_USER_POLL_EMPLOYEE_HEAD_BY_TEAM;
import static java.lang.Boolean.TRUE;

@Component
@Slf4j
public class PollIdToFinishedUserPollEmployeeHeadByTeamRecipientResolver implements RecipientResolver {

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	@Override
	public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
		if (!StringUtils.equals(POLL_ID_TO_FINISHED_USER_POLL_EMPLOYEE_HEAD_BY_TEAM, recipient.getBasicValue())) {
			return;
		}
		log.info(LOG_MESSAGE_RECIPIENT, POLL_ID_TO_FINISHED_USER_POLL_EMPLOYEE_HEAD_BY_TEAM);
		try {

			List<UserPollDto> userPollList = (List<UserPollDto>) context.getOrResolveObject(SavedObjectNames.USER_POLL_LIST, () ->
				Optional.ofNullable(RecipientResolverUtils.findPollId(context))
					.map(pollId -> context.getResolverServiceContainer().getQuizServiceClient()
						.findUserPoll(pollId.longValue(), null, TRUE))
					.map(upl -> upl.stream()
						.filter(distinctByKey(up -> up.getEmployeeId()))
						.collect(Collectors.toList()))
					.orElse(null)
			);
			if (CollectionUtils.isEmpty(userPollList)) {
				return;
			}

			List<DivisionTeamAssignmentDto> assignments = (List<DivisionTeamAssignmentDto>) context.getOrResolveObject(SavedObjectNames.ASSIGNMENTS, () ->
				context.getResolverServiceContainer().getOrgstructureServiceAdapter()
					.getAssignments(userPollList.stream().map(UserPollDto::getEmployeeId).collect(Collectors.toList()), null));
			if (CollectionUtils.isEmpty(assignments)) {
				return;
			}

			Map<Long, DivisionTeamInfo> divisionTeamInfoMap = RecipientResolverUtils.getDivisionTeamInfoMap(assignments);
			for (Map.Entry<Long, DivisionTeamInfo> entry : divisionTeamInfoMap.entrySet()) {
				AtomicInteger closedCount = new AtomicInteger();
				userPollList
					.stream()
					.filter(up -> up.getDateTo() != null && entry.getValue().getEmployeeIds().contains(up.getEmployeeId()))
					.forEach(up -> closedCount.incrementAndGet());
				log.info("division_team_id = {}, count not null user_poll - {}", entry.getKey(), closedCount.get());
				if (closedCount.get() == entry.getValue().getEmployeeIds().size()) {
					DivisionTeamAssignmentDto headTeam;
					if (entry.getValue().getHeadAssignment() != null) {
						headTeam = entry.getValue().getHeadAssignment();
					} else {
						headTeam = context.getResolverServiceContainer().getOrgstructureServiceAdapter()
							.getEmployeeHead(entry.getValue().getEmployees().get(0).getId(), entry.getKey());
					}
					if (headTeam != null) {
						recipientList.add(headTeam.getEmployee());
					}
				}
			}

		} catch (Exception ex) {
			log.error(LOG_ERROR, ex.getMessage());
		}
	}

	@Override
	public List<RecipientToken> recipientsRegistration() {
		return List.of(
			new RecipientToken(POLL_ID_TO_FINISHED_USER_POLL_EMPLOYEE_HEAD_BY_TEAM, "Руководитель, в чьей команде все прошли опросы")
		);
	}
}
