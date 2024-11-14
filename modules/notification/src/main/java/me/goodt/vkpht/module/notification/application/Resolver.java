package me.goodt.vkpht.module.notification.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.notification.application.impl.TokenWithValues;
import me.goodt.vkpht.module.notification.application.impl.resolver.group.NotificationToken;

public interface Resolver {

    List<NotificationToken> getResolvedTokens();

    default List<String> getResolvedGroups() {
        return getResolvedTokens().stream()
            .map(NotificationToken::getGroupName)
            .distinct()
            .collect(Collectors.toList());
    }

    default boolean tokenMustBeResolved(Map<String, List<TokenWithValues>> parsedTokens) {
        for (String resolvedGroup : getResolvedGroups()) {
            if (parsedTokens.containsKey(resolvedGroup)) {
                return true;
            }
        }
        return false;
    }
}
