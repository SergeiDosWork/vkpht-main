package me.goodt.vkpht.module.notification.application;

import java.util.Map;

import me.goodt.vkpht.module.notification.api.dto.BaseNotificationInputData;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.ResolverServiceContainer;
import me.goodt.vkpht.module.notification.application.impl.TokenResolverImpl;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;

public interface TokenResolver {
	Map<String, String> resolveTokens(ResolverContext context);

	Map<String, String> resolveEmployeeTokens(RecipientInfoDto employeeInfoDto, ResolverContext context);

	TokenResolverImpl.ResolveRecipitentResult resolveRecipients(NotificationTemplateContentDto ntc, ResolverServiceContainer resolverServiceContainer, BaseNotificationInputData data, boolean isCopy, int substituteLevel);

}
