package me.goodt.vkpht.module.notification.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationReceiverSystemRequestDto {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private Boolean isSystem;
    private Boolean isEditableIfSystem;
}
