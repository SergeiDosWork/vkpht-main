package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;
import me.goodt.vkpht.module.notification.config.NotificationConfig;

import static me.goodt.vkpht.module.notification.application.utils.TextConstants.ROOT_URL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.SERVICE;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.SERVICE_ROOT_URL;

@Component
@Slf4j
public class ServiceGroupResolver implements TokenGroupResolver {

    private final NotificationConfig notificationConfig;

    public ServiceGroupResolver(NotificationConfig notificationConfig) {
        this.notificationConfig = notificationConfig;
    }

    @Override
    public void resolve(ResolverContext context, Map<String, String> resolvedTokenValues) {
        log.info(LOG_MESSAGE_GROUP, SERVICE);
        List<TokenWithValues> tokens = context.getParsedTokens().get(SERVICE);
        tokens.forEach(token -> {
            if (token.getBasicValue().equals(ROOT_URL)) {
                log.info(LOG_MESSAGE_TOKEN, SERVICE, ROOT_URL);
                resolvedTokenValues.put(SERVICE_ROOT_URL, notificationConfig.getRootUrl());
            }
        });
    }

    @Override
    public List<NotificationToken> getResolvedTokens() {
        return List.of(
            new NotificationToken(SERVICE, ROOT_URL, "WEB ссылка на приложение")

        );
    }
}
