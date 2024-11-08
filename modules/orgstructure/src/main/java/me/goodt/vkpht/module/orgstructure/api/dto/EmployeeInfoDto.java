package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmployeeInfoDto {

    public EmployeeInfoDto(Long id, String externalId, String firstName, String lastName, String middleName,
                           String photo, String number, String email) {
        this.id = id;
        this.externalId = externalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.photo = photo;
        this.number = number;
        this.email = email;
    }

    public EmployeeInfoDto(Long id, String externalId, String firstName, String lastName, String middleName,
                           String photo, String number, String email, List<PositionAssignmentDto> positionAssignments) {
        this(id, externalId, firstName, lastName, middleName, photo, number, email);
        this.positionAssignments = positionAssignments;
    }

    @EqualsAndHashCode.Include
    private Long id;
    private String externalId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String photo;
    private String number;
    private String email;
    private List<PositionAssignmentDto> positionAssignments;
}
