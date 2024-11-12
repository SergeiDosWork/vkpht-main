package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import java.util.List;
import java.util.Set;

import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;

public interface RecipientResolver {

    String LOG_ERROR = "An error has occurred, message: {}";
    String LOG_MESSAGE_RECIPIENT = "There is {} recipient";

    void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList);

    List<RecipientToken> recipientsRegistration();

}
