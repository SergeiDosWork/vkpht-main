package me.goodt.vkpht.module.notification.application.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.goodt.vkpht.module.notification.api.dto.BaseNotificationInputData;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentDto;

@Getter
@Setter
@Slf4j
public class ResolverContext {
    private static final String DOT = ".";
    private final ResolverServiceContainer resolverServiceContainer;
    private Map<String, Object> parameters = new HashMap<>();
    private Map<String, Optional<Object>> savedObjects = new HashMap<>();
    private Map<String, List<TokenWithValues>> parsedTokens;
    private NotificationTemplateContentDto notificationTemplateContent;
    private int substituteLevel;

    public ResolverContext(ResolverServiceContainer resolverServiceContainer) {
        this.resolverServiceContainer = resolverServiceContainer;
    }

    private static Map<String, List<TokenWithValues>> parseTokensToGroupAndTokenWithValue(Set<String> tokens) {
        Map<String, List<TokenWithValues>> parsedTokensFromJson = new HashMap<>();
        tokens.forEach(token -> {
            String tokenGroup = StringUtils.substringBefore(token, DOT);
            TokenWithValues tokenWithValues = new TokenWithValues(StringUtils.substringAfter(token, DOT));
            if (parsedTokensFromJson.containsKey(tokenGroup)) {
                parsedTokensFromJson.get(tokenGroup).add(tokenWithValues);
            } else {
                parsedTokensFromJson.put(tokenGroup, Stream.of(tokenWithValues).collect(Collectors.toList()));
            }
        });
        return parsedTokensFromJson;
    }

    public ResolverContext prepareFromBaseNotificationInputData(BaseNotificationInputData data) {
        this.parameters = data.getParameters();
        return this;
    }

    public Object getOrResolveObject(String objectName, Supplier<Object> objectSupplier) {
        if (!savedObjects.containsKey(objectName) || savedObjects.get(objectName).isEmpty()) {
            Object o = null;
            try {
                o = objectSupplier.get();
            } catch (Exception e) {
                log.error("Error to resolve " + objectName, e);
            }
            if (!Objects.isNull(o)) {
                savedObjects.put(objectName, Optional.of(o));
            }
            return o;
        }
        return savedObjects.get(objectName).get();
    }

    public ResolverContext notificationTemplateContent(NotificationTemplateContentDto ntc) {
        this.notificationTemplateContent = ntc;
        return this;
    }

    public ResolverContext substituteLevel(int i) {
        this.substituteLevel = i;
        return this;
    }

    public ResolverContext prepareTokens(Set<String> tokens) {
        this.setParsedTokens(parseTokensToGroupAndTokenWithValue(tokens));
        return this;
    }
}
