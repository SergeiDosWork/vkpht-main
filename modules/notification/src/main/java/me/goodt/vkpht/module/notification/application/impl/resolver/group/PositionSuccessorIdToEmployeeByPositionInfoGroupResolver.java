package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static me.goodt.vkpht.module.notification.application.utils.DataUtils.getEmployeeFullName;
import static me.goodt.vkpht.module.notification.application.utils.DataUtils.getEmployeeShortName;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FIO_FULL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FIO_SHORT;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO_FIO_FULL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO_FIO_SHORT;

@Slf4j
@Component
public class PositionSuccessorIdToEmployeeByPositionInfoGroupResolver implements TokenGroupResolver {

	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		log.info(LOG_MESSAGE_GROUP, POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO);
		Integer positionSuccessorId = (Integer) context.getParameters().get(POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO);
		log.info("position_successor_id = {}", positionSuccessorId);
		if (Objects.isNull(positionSuccessorId)) {
			return;
		}

		try {
			PositionSuccessorDto positionSuccessor = (PositionSuccessorDto) context.getOrResolveObject(SavedObjectNames.POSITION_SUCCESSOR,
																									   () -> context.getResolverServiceContainer().getOrgstructureServiceAdapter().getPositionSuccessor(positionSuccessorId.longValue()));
			if (positionSuccessor == null) {
				log.error("Position successor with id = {} not found", positionSuccessorId);
				return;
			}

			PositionAssignmentDto positionAssignment = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getPositionAssignmentByPositionId(positionSuccessor.getPosition().getId());
			if (positionAssignment == null) {
				log.error("Position assignment not found by position_id = {}", positionSuccessor.getPosition().getId());
				return;
			}

			EmployeeInfoDto employee = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getEmployeeInfo(positionAssignment.getEmployeeId());
			if (employee == null) {
				log.error("Employee with id = {} not found for position with id = {}", positionAssignment.getEmployeeId(), positionSuccessor.getPosition().getId());
				return;
			}

			for (TokenWithValues token : context.getParsedTokens().get(POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO)) {
				if (token.getBasicValue().equals(FIO_FULL)) {
					log.info(LOG_MESSAGE_TOKEN, POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO, FIO_FULL);
					resolvedTokenValues.put(POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO_FIO_FULL, getEmployeeFullName(employee));
				} else if (token.getBasicValue().equals(FIO_SHORT)) {
					log.info(LOG_MESSAGE_TOKEN, POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO, FIO_SHORT);
					resolvedTokenValues.put(POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO_FIO_SHORT, getEmployeeShortName(employee));
				}
			}
		} catch (Exception ex) {
			log.error(LOG_ERROR, ex.getMessage());
		}
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return List.of(
			new NotificationToken(POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO, FIO_FULL, "ФИО работника на должности для которой согласовали преемника в формате Фамилия Имя Отчество"),
			new NotificationToken(POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO, FIO_SHORT, "ФИО работника на должности для которой согласовали преемника в формате Фамилия И.О.")
		);
	}
}
