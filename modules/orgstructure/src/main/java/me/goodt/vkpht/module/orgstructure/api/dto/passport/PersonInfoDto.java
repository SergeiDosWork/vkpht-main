package me.goodt.vkpht.module.orgstructure.api.dto.passport;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PersonInfoDto {

    private Long id;
    private String fullName;
    private String shortName;
    private String photo;
    private LocalDate dateBirth;
    private String email;
    private String telegram;
    private String city;
    private List<PassportEmployeeDto> employees;
    private SecondaryInfoDto secondaryInfo;
}
