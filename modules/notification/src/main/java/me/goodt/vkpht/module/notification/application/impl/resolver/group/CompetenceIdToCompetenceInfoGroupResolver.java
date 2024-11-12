package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.goodt.vkpht.module.notification.api.dto.rtcore.CompetenceNotificationDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.COMPETENCE_FULL_NAME;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.COMPETENCE_ID_TO_COMPETENCE_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.COMPETENCE_ID_TO_COMPETENCE_INFO_COMPETENCE_FULL_NAME;

@Component
@Slf4j
public class CompetenceIdToCompetenceInfoGroupResolver implements TokenGroupResolver {

	@Override
	public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
		log.debug(LOG_MESSAGE_GROUP, COMPETENCE_ID_TO_COMPETENCE_INFO);
		Integer competenceId = (Integer) context.getParameters().get(COMPETENCE_ID_TO_COMPETENCE_INFO);
		log.debug("competence_id = {}", competenceId);
		if (Objects.isNull(competenceId)) {
			return;
		}

		for (TokenWithValues token : context.getParsedTokens().get(COMPETENCE_ID_TO_COMPETENCE_INFO)) {
			if (token.getBasicValue().equals(COMPETENCE_FULL_NAME)) {
				log.debug(LOG_MESSAGE_TOKEN, COMPETENCE_ID_TO_COMPETENCE_INFO, COMPETENCE_FULL_NAME);
				try {
					CompetenceNotificationDto competence = context.getResolverServiceContainer().getCompetenceService().findCompetenceById(competenceId.longValue());
					resolvedTokenValues.put(COMPETENCE_ID_TO_COMPETENCE_INFO_COMPETENCE_FULL_NAME, competence.getName());
				} catch (Exception ex) {
					log.error(LOG_ERROR, ex.getMessage());
				}
			}
		}
	}

	@Override
	public List<NotificationToken> getResolvedTokens() {
		return List.of(
			new NotificationToken(COMPETENCE_ID_TO_COMPETENCE_INFO, COMPETENCE_FULL_NAME, "Полное наименование компетенций")
		);
	}
}
