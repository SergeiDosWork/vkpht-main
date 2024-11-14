package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import me.goodt.vkpht.module.notification.domain.dao.NotificationRecipientEmailDao;
import me.goodt.vkpht.module.notification.api.dto.StaticEmailRecipientDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;
import me.goodt.vkpht.module.notification.domain.entity.NotificationRecipientEmailEntity;
import me.goodt.vkpht.module.notification.application.impl.Recipient;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;

import static me.goodt.vkpht.module.notification.api.dto.data.NotificationRecipientType.STATIC_EMAIL;

@Component
@Slf4j
@RequiredArgsConstructor
public class StaticEmailRecipientResolver implements RecipientResolver {

    private final NotificationRecipientEmailDao notificationRecipientEmailDao;

    @Override
    public void resolve(ResolverContext context, Recipient recipient, Set<RecipientInfoDto> recipientList) {
        if (!StringUtils.equals(recipient.getBasicValue(), STATIC_EMAIL.getName())) {
            return;
        }

        List<NotificationRecipientEmailEntity> recipientEmailList = notificationRecipientEmailDao.findByRecipientIdAndEmails(
            recipient.getRecipient().getId(),
            recipient.getRecipient().getEmails()
        );

        if (CollectionUtils.isNotEmpty(recipientEmailList)) {
            for (NotificationRecipientEmailEntity recipientEmail : recipientEmailList) {
                StaticEmailRecipientDto resolvedRecipient = new StaticEmailRecipientDto();
                resolvedRecipient.setId(recipientEmail.getId());
                resolvedRecipient.setEmail(recipientEmail.getEmail());
                recipientList.add(resolvedRecipient);
            }
        }
    }

    @Override
    public List<RecipientToken> recipientsRegistration() {
        return Collections.emptyList();
    }
}
