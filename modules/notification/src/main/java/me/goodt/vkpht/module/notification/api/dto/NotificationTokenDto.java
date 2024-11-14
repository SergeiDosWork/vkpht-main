package me.goodt.vkpht.module.notification.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationTokenDto {

    private Long id;
    private String name;
    private String description;
    private String shortName;
    private String groupName;
}
