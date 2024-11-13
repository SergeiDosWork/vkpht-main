package me.goodt.vkpht.module.notification.application.impl.resolver.group;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationToken {
    private String groupName;
    private String tokenName;
    private String description;
}
