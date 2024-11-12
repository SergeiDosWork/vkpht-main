package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoResponse;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;

import static me.goodt.vkpht.module.notification.api.dto.data.NotificationRecipientType.STATIC_DIVISION;

@Component
@Slf4j
public class StaticDivisionRecipientResolver implements RecipientResolver {
	@Override
	public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
        if (!StringUtils.equals(recipient.getBasicValue(), STATIC_DIVISION.getName())) {
            return;
        }
        List<Long> divisionIds = recipient.getIds();
        try {
            EmployeeInfoResponse employeeInfoResponse = context.getResolverServiceContainer().getOrgstructureServiceAdapter().findEmployeeByDivision(divisionIds);
            recipientList.addAll(employeeInfoResponse.getData());
        } catch (Exception ex) {
            log.error(LOG_ERROR, ex.getMessage());
        }

    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return Collections.emptyList();
    }
}
