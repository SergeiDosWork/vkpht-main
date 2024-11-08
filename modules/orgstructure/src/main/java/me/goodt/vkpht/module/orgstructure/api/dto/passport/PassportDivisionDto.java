package me.goodt.vkpht.module.orgstructure.api.dto.passport;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PassportDivisionDto {

    private Long id;
    private String fullName;
    private String shortName;
    private String description;
    private Date dateFrom;
    private Date dateTo;
    private Date dateStart;
    private Date dateEnd;
    private Date dateStartConfirm;
    private Date dateEndConfirm;
}
