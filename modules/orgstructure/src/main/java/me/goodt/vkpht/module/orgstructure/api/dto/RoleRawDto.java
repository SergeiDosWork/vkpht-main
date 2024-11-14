package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RoleRawDto extends RoleDto {

    public RoleRawDto(Long id, String fullName, String shortName, String abbreviation,
                      SystemRoleDto systemRole, String code, Date dateFrom, Date dateTo) {
        super(id, fullName, shortName, abbreviation, systemRole, code, dateFrom, dateTo);
        this.systemRoleId = getSystemRole().getId().longValue();
    }

    private Long systemRoleId;
}
