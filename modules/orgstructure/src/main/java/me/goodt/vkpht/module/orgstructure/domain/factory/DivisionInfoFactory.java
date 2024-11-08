package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;

@UtilityClass
public class DivisionInfoFactory {

    public static DivisionInfoDto create(DivisionEntity division, EmployeeEntity headEmployee,
                                         List<PositionAssignmentEntity> assignments, List<DivisionTeamEntity> divisionTeams) {

        List<DivisionTeamDto> divisionTeamDtoList = divisionTeams.stream().map(DivisionTeamFactory::create).collect(Collectors.toList());

        return new DivisionInfoDto(division != null ? DivisionFactory.create(division) : null,
                                   headEmployee != null ? EmployeeExtendedInfoFactory.create(headEmployee, assignments) : null, divisionTeamDtoList);
    }
}
