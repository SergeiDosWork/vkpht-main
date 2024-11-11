package me.goodt.vkpht.common.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.goodt.drive.rtcore.dto.orgstructure.RoleDto;
import com.goodt.drive.rtcore.dto.orgstructure.SystemRoleDto;

@Getter
@Setter
@AllArgsConstructor
public class RoleInfo {
    private RoleDto role;
    private SystemRoleDto systemRole;
}
