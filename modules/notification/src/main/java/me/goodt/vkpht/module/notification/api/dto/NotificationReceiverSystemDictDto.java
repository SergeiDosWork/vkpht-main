package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationReceiverSystemDictDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull
    @Size(max = 256)
    private String name;
    @Size(max = 1024)
    private String description;
    private Boolean isActive;
    private Boolean isSystem;
    private Boolean isEditableIfSystem;
}
