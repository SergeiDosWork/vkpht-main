package me.goodt.vkpht.module.notification.application;

import java.util.Collection;
import java.util.List;

public interface NotificationRecipientEmailService {
    List<String> findEmailByEmailIds(Collection<Long> ids);
}
