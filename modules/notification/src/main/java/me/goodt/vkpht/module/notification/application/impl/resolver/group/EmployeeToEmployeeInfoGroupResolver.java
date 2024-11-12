package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static com.goodt.drive.notify.application.utils.DataUtils.getEmployeeFullName;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.EMPLOYEE_TO_EMPLOYEE_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.EMPLOYEE_TO_EMPLOYEE_INFO_FIO_FULL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FIO_FULL;

@Component
@Slf4j
public class EmployeeToEmployeeInfoGroupResolver implements TokenGroupResolver {
	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		log.info(LOG_MESSAGE_GROUP, EMPLOYEE_TO_EMPLOYEE_INFO);
		Integer employeeId = (Integer) context.getParameters().get(EMPLOYEE_TO_EMPLOYEE_INFO);
		log.info("employee_id = {}", employeeId);
		if (Objects.isNull(employeeId)) {
			return;
		}
		EmployeeInfoDto employeeInfo = (EmployeeInfoDto) context.getOrResolveObject(SavedObjectNames.EMPLOYEE_INFO, () ->
			context.getResolverServiceContainer().getOrgstructureServiceClient().getEmployeeInfo(employeeId.longValue()));
		if (Objects.isNull(employeeInfo)) {
			return;
		}
		for (TokenWithValues token : context.getParsedTokens().get(EMPLOYEE_TO_EMPLOYEE_INFO)) {
			if (token.getBasicValue().equals(FIO_FULL)) {
				log.info(LOG_MESSAGE_TOKEN, EMPLOYEE_TO_EMPLOYEE_INFO, FIO_FULL);
				resolvedTokenValues.put(EMPLOYEE_TO_EMPLOYEE_INFO_FIO_FULL, getEmployeeFullName(employeeInfo));
			}
		}
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return List.of(
			new NotificationToken(EMPLOYEE_TO_EMPLOYEE_INFO, FIO_FULL, "ФИО - Фамилия Имя Отчество сотрудника")
		);
	}
}
