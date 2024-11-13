package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.goodt.vkpht.module.orgstructure.api.dto.PositionSuccessorDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;

import static me.goodt.vkpht.module.notification.application.utils.DataUtils.getStringDateByPattern;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_FROM;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_PATTERN;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_TO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_TO_POSITION_SUCCESSOR_INFO_DATE_FROM;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_TO_POSITION_SUCCESSOR_INFO_DATE_TO;

@Component
@Slf4j
public class PositionSuccessorIdToPositionSuccessorInfoGroupResolver implements TokenGroupResolver {
    @Override
    public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
        log.info(LOG_MESSAGE_GROUP, POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO);
        Integer positionSuccessorId = (Integer) context.getParameters().get(POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO);
        log.info("position_successor_id = {}", positionSuccessorId);
        if (Objects.isNull(positionSuccessorId)) {
            return;
        }
        try {
            PositionSuccessorDto positionSuccessor = (PositionSuccessorDto) context.getOrResolveObject(SavedObjectNames.POSITION_SUCCESSOR, () ->
                context.getResolverServiceContainer().getOrgstructureServiceAdapter().getPositionSuccessor(positionSuccessorId.longValue()));
            for (TokenWithValues token : context.getParsedTokens().get(POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO)) {
                if (token.getBasicValue().equals(DATE_FROM)) {
                    log.info(LOG_MESSAGE_TOKEN, POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO, DATE_FROM);
                    String dateFrom = getStringDateByPattern(DATE_PATTERN, positionSuccessor.getDateFrom());
                    if (dateFrom != null) {
                        resolvedTokenValues.put(POSITION_SUCCESSOR_TO_POSITION_SUCCESSOR_INFO_DATE_FROM, dateFrom);
                    }
                }
                if (token.getBasicValue().equals(DATE_TO)) {
                    log.info(LOG_MESSAGE_TOKEN, POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO, DATE_TO);
                    String dateTo = getStringDateByPattern(DATE_PATTERN, positionSuccessor.getDateTo());
                    if (dateTo != null) {
                        resolvedTokenValues.put(POSITION_SUCCESSOR_TO_POSITION_SUCCESSOR_INFO_DATE_TO, dateTo);
                    }
                }
            }
        } catch (Exception ex) {
            log.error(LOG_ERROR, ex.getMessage());
        }
    }

    @Override
    public List<NotificationToken> getResolvedTokens() {
        return Arrays.asList(
            new NotificationToken(POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO, DATE_FROM, "Дата создания записи в кадровом резерве"),
            new NotificationToken(POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO, DATE_TO, "Дата удаления из КР")
        );
    }
}
