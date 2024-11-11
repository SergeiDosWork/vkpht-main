package me.goodt.vkpht.module.notification.application;

import java.util.List;
import java.util.Set;

import com.goodt.drive.notify.application.dto.orgstructure.RecipientInfoDto;
import com.goodt.drive.notify.application.services.notification.Recipient;
import com.goodt.drive.notify.application.services.notification.ResolverContext;
import me.goodt.vkpht.module.notification.api.dto.Recipient;
import me.goodt.vkpht.module.notification.api.dto.RecipientToken;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;

public interface RecipientResolver {

    String LOG_ERROR = "An error has occurred, message: {}";
    String LOG_MESSAGE_RECIPIENT = "There is {} recipient";

    void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList);

    List<RecipientToken> recipientsRegistration();

}
