package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoResponse;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.api.dto.quiz.UserPollDto;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_USER_POLL_EMPLOYEE;
import static java.lang.Boolean.TRUE;

@Component
@Slf4j
public class PollIdToUserPollEmployeeRecipientResolver implements RecipientResolver {
	@Override
	public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
        if (!StringUtils.equals(POLL_ID_TO_USER_POLL_EMPLOYEE, recipient.getBasicValue())) {
            return;
        }

        log.info(LOG_MESSAGE_RECIPIENT, POLL_ID_TO_USER_POLL_EMPLOYEE);
        try {
            List<UserPollDto> userPollList = (List<UserPollDto>) context.getOrResolveObject(SavedObjectNames.USER_POLL_LIST, () ->
                Optional.ofNullable(RecipientResolverUtils.findPollId(context))
                    .map(pollId -> context.getResolverServiceContainer().getQuizServiceClient()
                        .findUserPoll(pollId.longValue(), null, TRUE))
                    .orElse(null)
            );

            if (userPollList != null) {
                EmployeeInfoResponse employeeInfo = context.getResolverServiceContainer().getOrgstructureServiceClient().findEmployee(
                    userPollList.stream().map(UserPollDto::getEmployeeId).collect(Collectors.toList()));
                recipientList.addAll(employeeInfo.getData());
            }
        } catch (Exception ex) {
            log.error(LOG_ERROR, ex.getMessage());
        }
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return List.of(
            new RecipientToken(POLL_ID_TO_USER_POLL_EMPLOYEE, "Работники, которым назначен опрос")
        );
    }
}
