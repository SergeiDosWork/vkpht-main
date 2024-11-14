package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoResponse;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.api.dto.quiz.UserPollDto;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_USER_POLL_EMPLOYEE_NOT_FINISHED;
import static java.lang.Boolean.FALSE;

@Component
@Slf4j
public class PollIdToUserPollEmployeeNotFinishedRecipientResolver implements RecipientResolver {
    @Override
    public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
        if (!StringUtils.equals(POLL_ID_TO_USER_POLL_EMPLOYEE_NOT_FINISHED, recipient.getBasicValue())) {
            return;
        }
        log.info(LOG_MESSAGE_RECIPIENT, POLL_ID_TO_USER_POLL_EMPLOYEE_NOT_FINISHED);
        try {
            Integer pollId = RecipientResolverUtils.findPollId(context);
            log.info("poll_id = {}", pollId);
            if (pollId != null) {
                List<UserPollDto> userPollList = context.getResolverServiceContainer().getQuizServiceClient().findUserPoll(pollId.longValue(), null, FALSE);
                EmployeeInfoResponse employeeInfoResponse = context.getResolverServiceContainer().getOrgstructureServiceAdapter().findEmployee(
                    userPollList.stream().map(UserPollDto::getEmployeeId).collect(Collectors.toList()));
                recipientList.addAll(employeeInfoResponse.getData());
            }
        } catch (Exception ex) {
            log.error(LOG_ERROR, ex.getMessage());
        }
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return List.of(
            new RecipientToken(POLL_ID_TO_USER_POLL_EMPLOYEE_NOT_FINISHED, "Работники, которым назначен опрос, но они его не завершили")
        );
    }
}
