package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeStatusDto {
    private Long id;
    private String shortName;
    private String fullName;
    private String systemName;
    private Boolean isFreeStake;
    private String externalId;
}
