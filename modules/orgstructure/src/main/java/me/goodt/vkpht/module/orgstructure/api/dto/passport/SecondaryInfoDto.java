package me.goodt.vkpht.module.orgstructure.api.dto.passport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecondaryInfoDto {

    private String inn;
    private String snils;
    private String sex;
    private String postcode;
    private Long citizenshipId;
    private String citizenshipName;
}
