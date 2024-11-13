package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.notification.api.dto.data.DivisionTeamInfo;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.api.dto.quiz.UserPollDto;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_USER_POLL_EMPLOYEE_HEAD;
import static java.lang.Boolean.TRUE;

@Component
@Slf4j
public class PollIdToUserPollEmployeeHeadRecipientResolver implements RecipientResolver {
    @Override
    public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
        if (!StringUtils.equals(POLL_ID_TO_USER_POLL_EMPLOYEE_HEAD, recipient.getBasicValue())) {
            return;
        }

        log.info(LOG_MESSAGE_RECIPIENT, POLL_ID_TO_USER_POLL_EMPLOYEE_HEAD);
        try {
            List<UserPollDto> userPollList = (List<UserPollDto>) context.getOrResolveObject(SavedObjectNames.USER_POLL_LIST, () ->
                Optional.ofNullable(RecipientResolverUtils.findPollId(context))
                    .map(pollId -> context.getResolverServiceContainer().getQuizServiceClient()
                        .findUserPoll(pollId.longValue(), null, TRUE))
                    .orElse(null)
            );
            if (CollectionUtils.isEmpty(userPollList)) {
                return;
            }

            List<DivisionTeamAssignmentDto> assignments = (List<DivisionTeamAssignmentDto>) context.getOrResolveObject(SavedObjectNames.POLL_ASSIGNMENTS, () ->
                context.getResolverServiceContainer().getOrgstructureServiceAdapter()
                    .getAssignments(null, userPollList.stream().map(UserPollDto::getEmployeeId).collect(Collectors.toList())));
            if (CollectionUtils.isEmpty(assignments)) {
                return;
            }

            Map<Long, DivisionTeamInfo> divisionTeamInfoMap = RecipientResolverUtils.getDivisionTeamInfoMap(assignments);
            divisionTeamInfoMap.forEach((dtId, dtInfo) -> {
                DivisionTeamAssignmentDto headTeam;
                if (dtInfo.getHeadAssignment() != null) {
                    headTeam = dtInfo.getHeadAssignment();
                } else {
                    headTeam = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getEmployeeHead(dtInfo.getEmployees().get(0).getId(), dtId);
                }
                if (headTeam != null) {
                    recipientList.add(headTeam.getEmployee());
                }
            });

        } catch (Exception ex) {
            log.error(LOG_ERROR, ex.getMessage());
        }
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return List.of(
            new RecipientToken(POLL_ID_TO_USER_POLL_EMPLOYEE_HEAD, "Руководили работников, которым назначен опрос")
        );
    }

}
