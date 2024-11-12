package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;
import me.goodt.vkpht.module.notification.application.utils.TextConstants;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_EMPLOYEE;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_EMPLOYEE_HEAD;

@Component
@Slf4j
public class PositionSuccessorEmployeeRecipientResolver implements RecipientResolver {
	@Override
	public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
		if (!StringUtils.equalsAny(recipient.getBasicValue(), POSITION_SUCCESSOR_EMPLOYEE, POSITION_SUCCESSOR_EMPLOYEE_HEAD)) {
			return;
        }
        log.info(LOG_MESSAGE_RECIPIENT, recipient.getBasicValue());
        try {
            PositionSuccessorDto positionSuccessor = (PositionSuccessorDto) context.getOrResolveObject(SavedObjectNames.POSITION_SUCCESSOR, () -> Optional.ofNullable(RecipientResolverUtils.findPositionSuccessorId(context))
                .map(positionSuccessorId -> context.getResolverServiceContainer().getOrgstructureServiceClient().getPositionSuccessor(positionSuccessorId.longValue()))
                .orElse(null));
            if (Objects.isNull(positionSuccessor)) {
                return;
            }

            if (recipient.getBasicValue().equals(POSITION_SUCCESSOR_EMPLOYEE)) {
                recipientList.add(positionSuccessor.getEmployee());
            } else {
                List<DivisionTeamAssignmentDto> assignmentsByEmployeeId = context.getResolverServiceContainer().getOrgstructureServiceClient().getAssignments(null, Collections.singletonList(positionSuccessor.getEmployee().getId()));
                if (assignmentsByEmployeeId != null && !assignmentsByEmployeeId.isEmpty()) {
                    DivisionTeamAssignmentDto assignment = assignmentsByEmployeeId.getFirst();
                    DivisionTeamAssignmentDto employeeHead = context.getResolverServiceContainer().getOrgstructureServiceClient().getEmployeeHead(assignment.getEmployee().getId(), assignment.getDivisionTeam().getId());
                    recipientList.add(employeeHead.getEmployee());
                }
            }
        } catch (Exception ex) {
            log.error(LOG_ERROR, ex.getMessage());
        }
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return List.of(
            new RecipientToken(TextConstants.POSITION_SUCCESSOR_EMPLOYEE, "Сотрудник, назначенный преемником"),
            new RecipientToken(TextConstants.POSITION_SUCCESSOR_EMPLOYEE_HEAD, "Руководитель замещающего на позицию")
        );
    }
}
