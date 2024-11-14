package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import java.util.Map;

import me.goodt.vkpht.module.notification.application.Resolver;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;

public interface TokenGroupResolver extends Resolver {

    String LOG_MESSAGE_GROUP = "There is {} group";
    String LOG_MESSAGE_TOKEN = "There is {} group and {} token";
    String LOG_MESSAGE_RECIPIENT = "There is {} recipient";
    String LOG_ERROR = "An error has occurred, message: {}";

    String TASK_ID_IS = "task_id = {}";
    String YEAR_SYMBOL = "г.";
    String DAYS_DELTA = "days_delta";

    void resolve(ResolverContext context, Map<String, String> resolvedTokenValues);

}
