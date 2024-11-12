package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;
import me.goodt.vkpht.module.notification.application.utils.TextConstants;

@Slf4j
@Component
public class PositionSuccessIdToEmployeeHeadByPositionRecipientResolver implements RecipientResolver {

	@Override
	public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
		if (!StringUtils.equals(TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_HEAD_BY_POSITION, recipient.getBasicValue())) {
            return;
        }

		log.info(LOG_MESSAGE_RECIPIENT, recipient.getBasicValue());
		try {
            PositionSuccessorDto positionSuccessor = (PositionSuccessorDto) context.getOrResolveObject(SavedObjectNames.POSITION_SUCCESSOR, () -> Optional.ofNullable(RecipientResolverUtils.findPositionSuccessorId(context))
                .map(positionSuccessorId -> context.getResolverServiceContainer().getOrgstructureServiceAdapter().getPositionSuccessor(positionSuccessorId.longValue()))
                .orElse(null));
            if (positionSuccessor == null) {
                log.error("Position assignment not found for recipient {}", TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_HEAD_BY_POSITION);
                return;
            }

            PositionAssignmentDto positionAssignment = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getPositionAssignmentByPositionId(positionSuccessor.getPosition().getId());
            if (positionAssignment == null) {
                log.error("Position assignment not found by position_id = {}", positionSuccessor.getPosition().getId());
                return;
            }

            List<DivisionTeamAssignmentDto> assignments = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getAssigmentByEmployeeIds(List.of(positionAssignment.getEmployeeId()));
            DivisionTeamAssignmentDto headAssignment = RecipientResolverHelper.getHeadByAssignments(context, assignments);
            if (headAssignment == null) {
                log.error("Head employee not found for employee with id = {}", positionAssignment.getEmployeeId());
                return;
            }

            recipientList.add(headAssignment.getEmployee());
        } catch (Exception ex) {
            log.error(LOG_ERROR, ex.getMessage());
        }
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return List.of(
            new RecipientToken(TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_HEAD_BY_POSITION, "Руководитель работника, на позицию которого согласовали преемника")
        );
    }
}
