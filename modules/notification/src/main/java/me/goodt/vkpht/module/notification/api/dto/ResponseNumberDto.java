package me.goodt.vkpht.module.notification.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseNumberDto {
    private List<String> confirmed;
    private List<String> unconfirmed;
}
