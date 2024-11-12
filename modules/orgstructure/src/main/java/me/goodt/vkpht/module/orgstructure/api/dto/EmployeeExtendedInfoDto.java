package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.domain.factory.PersonFactory;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonEntity;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeExtendedInfoDto extends EmployeeInfoDto {

    public EmployeeExtendedInfoDto(EmployeeInfoDto employee, List<PositionAssignmentDto> positionAssignments, PersonDto person) {
        super(employee.getId(), employee.getExternalId(), employee.getFirstName(), employee.getLastName(), employee.getMiddleName(), employee.getPhoto(),
              employee.getNumber(), employee.getEmail(), positionAssignments);
        this.person = person;
    }

    private PersonDto person;
}
