package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorReadinessDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.FULL_NAME;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_READINESS_TO_POSITION_INFO_FULL_NAME;

@Component
@Slf4j
public class PositionSuccessorReadinesIdToPositionInfoGroupResolver implements TokenGroupResolver {
	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		log.info(LOG_MESSAGE_GROUP, POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO);
		Integer positionSuccessorReadinessId = (Integer) context.getParameters().get(POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO);
		log.info("position_successor_readiness_id = {}", positionSuccessorReadinessId);
		if (Objects.isNull(positionSuccessorReadinessId)) {
			return;
		}
		try {
			PositionSuccessorReadinessDto positionSuccessorReadiness = (PositionSuccessorReadinessDto) context.getOrResolveObject(SavedObjectNames.POSITION_SUCCESSOR_READINESS, () ->
				context.getResolverServiceContainer().getOrgstructureServiceClient().getPositionSuccessorReadiness(positionSuccessorReadinessId.longValue()));
			for (TokenWithValues token : context.getParsedTokens().get(POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO)) {
				if (token.getBasicValue().equals(FULL_NAME)) {
					log.info(LOG_MESSAGE_TOKEN, POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO, FULL_NAME);
					resolvedTokenValues.put(POSITION_SUCCESSOR_READINESS_TO_POSITION_INFO_FULL_NAME, positionSuccessorReadiness.getPositionSuccessor().getPosition().getFullName());
				}
			}
		} catch (Exception ex) {
			log.error(LOG_ERROR, ex.getMessage());
		}
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return List.of(
			new NotificationToken(POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO, FULL_NAME, "Имя кадрового резерва")
		);
	}
}
