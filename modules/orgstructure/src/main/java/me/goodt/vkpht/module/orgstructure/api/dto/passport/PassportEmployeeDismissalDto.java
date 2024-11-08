package me.goodt.vkpht.module.orgstructure.api.dto.passport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassportEmployeeDismissalDto {

    private Long id;
    private Long dismissalTypeId;
    private Long dismissalReasonId;
    private String commentHr;
    private String commentRuk;
}
