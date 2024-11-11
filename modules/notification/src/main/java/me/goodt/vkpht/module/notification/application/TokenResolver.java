package me.goodt.vkpht.module.notification.application;

import java.util.Map;

import com.goodt.drive.notify.application.dto.BaseNotificationInputData;
import com.goodt.drive.notify.application.dto.NotificationTemplateContentDto;
import com.goodt.drive.notify.application.dto.orgstructure.RecipientInfoDto;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.notification.application.impl.ResolverServiceContainer;
import me.goodt.vkpht.module.notification.application.impl.TokenResolverImpl;

public interface TokenResolver {
	Map<String, String> resolveTokens(ResolverContext context);

	Map<String, String> resolveEmployeeTokens(RecipientInfoDto employeeInfoDto, ResolverContext context);

	TokenResolverImpl.ResolveRecipitentResult resolveRecipients(NotificationTemplateContentDto ntc, ResolverServiceContainer resolverServiceContainer, BaseNotificationInputData data, boolean isCopy, int substituteLevel);

}
