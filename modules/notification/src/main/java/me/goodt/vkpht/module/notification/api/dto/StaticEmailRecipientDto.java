package me.goodt.vkpht.module.notification.api.dto;

import lombok.Getter;
import lombok.Setter;

import me.goodt.vkpht.module.notification.api.dto.orgstructure.RecipientInfoDto;

@Getter
@Setter
public class StaticEmailRecipientDto implements RecipientInfoDto {
    Long id;
    String email;
}
