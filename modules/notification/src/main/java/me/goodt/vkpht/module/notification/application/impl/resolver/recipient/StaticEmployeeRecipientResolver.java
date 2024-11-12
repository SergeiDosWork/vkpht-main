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

import static me.goodt.vkpht.module.notification.api.dto.data.NotificationRecipientType.STATIC_EMPLOYEE;

@Component
@Slf4j
public class StaticEmployeeRecipientResolver implements RecipientResolver {
	@Override
	public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
        if (!StringUtils.equals(recipient.getBasicValue(), STATIC_EMPLOYEE.getName())) {
            return;
        }
        List<Long> employeeIds = recipient.getIds();
        try {
            EmployeeInfoResponse employeeInfoResponse = context.getResolverServiceContainer().getOrgstructureServiceClient().findEmployee(employeeIds);
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
