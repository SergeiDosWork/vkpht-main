package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DIVISION_TEAM_SUCCESSOR_ID_TO_EMPLOYEE;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DIVISION_TEAM_SUCCESSOR_ID_TO_POSITION_INFO;

@Component
@Slf4j
public class DivisionTeamSuccessorIdToEmployeeRecipientResolver implements RecipientResolver {
    @Override
    public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
        if (!StringUtils.equals(DIVISION_TEAM_SUCCESSOR_ID_TO_EMPLOYEE, recipient.getBasicValue())) {
            return;
        }
        log.info(LOG_MESSAGE_RECIPIENT, DIVISION_TEAM_SUCCESSOR_ID_TO_EMPLOYEE);

        if (Objects.isNull(context.getParameters())) {
            return;
        }

        try {
            DivisionTeamSuccessorDto divisionTeamSuccessor = (DivisionTeamSuccessorDto) context.getOrResolveObject(SavedObjectNames.DIVISION_TEAM_SUCCESSOR, () ->
                Optional.ofNullable((Integer) context.getParameters().get(DIVISION_TEAM_SUCCESSOR_ID_TO_POSITION_INFO))
                    .map(divisionTeamSuccessorId -> context.getResolverServiceContainer().getOrgstructureServiceAdapter().getDivisionTeamSuccessor(divisionTeamSuccessorId.longValue()))
                    .orElse(null));

            if (divisionTeamSuccessor != null) {
                recipientList.add(divisionTeamSuccessor.getEmployee());
            }
        } catch (Exception ex) {
            log.error(LOG_ERROR, ex.getMessage());
        }
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return List.of(
            new RecipientToken(DIVISION_TEAM_SUCCESSOR_ID_TO_EMPLOYEE, "Сотрудник назначенный преемником")
        );
    }
}
