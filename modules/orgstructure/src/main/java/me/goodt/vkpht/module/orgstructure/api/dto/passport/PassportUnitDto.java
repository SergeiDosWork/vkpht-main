package me.goodt.vkpht.module.orgstructure.api.dto.passport;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PassportUnitDto {

    private String name;
    private String description;
    private String code;
    private Date dateFrom;
    private Date dateTo;
}
