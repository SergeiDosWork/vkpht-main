package me.goodt.vkpht.module.notification.api.dto.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class KafkaEmployeeInfo {
    @EqualsAndHashCode.Include
    private Long employeeId;
    private List<String> usersKeycloakIds;
}
