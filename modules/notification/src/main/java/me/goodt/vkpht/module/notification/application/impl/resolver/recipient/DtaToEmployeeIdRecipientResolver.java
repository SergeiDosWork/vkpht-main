package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DTA_TO_EMPLOYEE_ID;

@Component
@Slf4j
public class DtaToEmployeeIdRecipientResolver implements RecipientResolver {
	@Override
    public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
        if (!StringUtils.equals(DTA_TO_EMPLOYEE_ID, recipient.getBasicValue())) {
            return;
        }
        log.info(LOG_MESSAGE_RECIPIENT, DTA_TO_EMPLOYEE_ID);

        if (Objects.isNull(context.getParameters())) {
            return;
        }

        Integer dtaId = (Integer) context.getParameters().get(DTA_TO_EMPLOYEE_ID);
        log.info("dta_id = {}", dtaId);
        if (dtaId != null) {
            List<DivisionTeamAssignmentDto> assignments = context.getResolverServiceContainer().getOrgstructureServiceAdapter()
                .getAssignments(Collections.singletonList(dtaId.longValue()), null);
            if (assignments != null && !assignments.isEmpty()) {
                recipientList.add(assignments.getFirst().getEmployee());
            }
        }
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return List.of(
            new RecipientToken(DTA_TO_EMPLOYEE_ID, "Сотрудник по его назначению")
        );
    }
}
