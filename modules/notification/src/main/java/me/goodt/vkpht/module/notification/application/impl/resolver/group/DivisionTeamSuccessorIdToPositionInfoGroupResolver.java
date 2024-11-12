package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleContainerDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DIVISION_TEAM_SUCCESSOR_ID_TO_POSITION_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DIVISION_TEAM_SUCCESSOR_ID_TO_POSITION_INFO_FULL_NAME;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FULL_NAME;

@Component
@Slf4j
public class DivisionTeamSuccessorIdToPositionInfoGroupResolver implements TokenGroupResolver {
	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		log.info(LOG_MESSAGE_GROUP, DIVISION_TEAM_SUCCESSOR_ID_TO_POSITION_INFO);
		Integer divisionTeamSuccessorId = (Integer) context.getParameters().get(DIVISION_TEAM_SUCCESSOR_ID_TO_POSITION_INFO);
		log.info("division_team_successor_id = {}", divisionTeamSuccessorId);
		if (divisionTeamSuccessorId != null) {
			try {
				for (TokenWithValues token : context.getParsedTokens().get(DIVISION_TEAM_SUCCESSOR_ID_TO_POSITION_INFO)) {
					if (token.getBasicValue().equals(FULL_NAME)) {
						log.info(LOG_MESSAGE_TOKEN, DIVISION_TEAM_SUCCESSOR_ID_TO_POSITION_INFO, FULL_NAME);
						DivisionTeamSuccessorDto divisionTeamSuccessor = (DivisionTeamSuccessorDto) context.getOrResolveObject(SavedObjectNames.DIVISION_TEAM_SUCCESSOR, () ->
							context.getResolverServiceContainer().getOrgstructureServiceClient().getDivisionTeamSuccessor(divisionTeamSuccessorId.longValue()));
						List<DivisionTeamRoleContainerDto> roleContainer =
							context.getResolverServiceContainer().getOrgstructureServiceClient().findDivisionTeamRoles(null, null, null, null, null, divisionTeamSuccessor.getDivisionTeamRole().getId());
						if (roleContainer != null && !roleContainer.isEmpty()) {
							Long employeeId = roleContainer.getFirst().getDivisionTeamAssignmentDtos().get(0).getEmployee().getId();
							List<PositionDto> positions = context.getResolverServiceContainer().getOrgstructureServiceClient().getPositionByEmployeeIdAndDivisionId(employeeId, null);
							if (!positions.isEmpty()) {
								resolvedTokenValues.put(DIVISION_TEAM_SUCCESSOR_ID_TO_POSITION_INFO_FULL_NAME, positions.getFirst().getFullName());
							}
						}
					}
				}
			} catch (Exception ex) {
				log.error(LOG_ERROR, ex.getMessage());
			}
		}
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return List.of(
			new NotificationToken(DIVISION_TEAM_SUCCESSOR_ID_TO_POSITION_INFO, FULL_NAME, "Наименование позиции")
		);
	}
}
