package me.goodt.vkpht.module.orgstructure.api.dto.passport;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PassportEmployeeDto {

    private Long id;
    private String number;
    private String email;
    private String phone;
    private String telegram;
    private String externalId;
    private List<PassportUnitDto> units;
    private List<PassportLegalEntityDto> legalEntities;
    private List<PassportDivisionDto> divisions;
    private List<PassportPositionDto> positions;
    private List<PassportPositionAssignmentDto> positionAssignments;
    private List<PassportEmployeeDismissalDto> employeeDismissals;
}
