package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitService {

    private final NotificationResolverTableUpdater notificationResolverTableUpdater;
    private final NotificationTokenTableUpdater notificationTokenTableUpdater;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        notificationResolverTableUpdater.updateRecipientTable();
        notificationTokenTableUpdater.updateNotificationTokenTable();
    }

}
