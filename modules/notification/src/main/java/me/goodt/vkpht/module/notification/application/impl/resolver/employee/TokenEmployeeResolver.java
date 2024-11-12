package me.goodt.vkpht.module.notification.application.impl.resolver.employee;

import java.util.Map;

import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.application.Resolver;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;

public interface TokenEmployeeResolver extends Resolver {

	String LOG_MESSAGE_GROUP = "There is {} group";
	String LOG_MESSAGE_TOKEN = "There is {} group and {} token";
	String LOG_ERROR = "An error has occurred, message: {}";

	void resolve(ResolverContext context, RecipientInfoDto employeeInfoDto, Map<String, String> resolvedTokenValues);

}
