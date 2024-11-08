package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmployeeFlatInfoDto {
    @EqualsAndHashCode.Include
    private Long id;
    private String fio;
    private String photo;
    private String position;
}
