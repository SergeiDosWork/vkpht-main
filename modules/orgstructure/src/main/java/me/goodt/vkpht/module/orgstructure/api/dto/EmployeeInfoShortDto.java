package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeInfoShortDto {
    private Integer id;
    private String fullName;
    private Long divisionId;
    private String name;
    private String surname;
    private String patronymic;
    private String photo;
    private String positionShortName;
}
