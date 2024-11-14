package me.goodt.vkpht.module.orgstructure.api.dto.passport;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PassportPositionDto {

    private Long id;
    private String fullName;
    private String shortName;
    private Date dateFrom;
    private Date dateTo;
}
