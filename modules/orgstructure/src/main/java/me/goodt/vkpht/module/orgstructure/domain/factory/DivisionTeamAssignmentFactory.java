package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.List;

import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentRotationShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentWithDivisionTeamFullDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentShort;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;

@UtilityClass
public class DivisionTeamAssignmentFactory {

    public static DivisionTeamAssignmentDto create(DivisionTeamAssignmentEntity entity, List<DivisionTeamAssignmentRotationShortDto> rotations) {
        return new DivisionTeamAssignmentDto(entity.getId(),
            EmployeeInfoFactory.create(entity.getEmployee()),
            entity.getDivisionTeamRole() != null ? DivisionTeamRoleFactory.create(entity.getDivisionTeamRole()) : null,
            entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
            entity.getDateFrom(), entity.getDateTo(),
            entity.getType() != null ? AssignmentTypeFactory.create(entity.getType()) : null,
            entity.getStatus() != null ? AssignmentStatusFactory.create(entity.getStatus()) : null,
            rotations, entity.getExternalId(),
            entity.getDivisionTeamRole().getDivisionTeam() != null ? DivisionTeamFactory.create(entity.getDivisionTeamRole().getDivisionTeam()) : null);
    }

    public static DivisionTeamAssignmentDto create(DivisionTeamAssignmentEntity entity) {
        return create(entity, null);
    }

    public static DivisionTeamAssignmentDto createSuperShort(DivisionTeamAssignmentEntity entity, boolean withEmployee, boolean withDtr) {
        return createWithJobInfoAndFlags(entity, null, null, withEmployee, withDtr);
    }

    public static DivisionTeamAssignmentDto createShortWithJobInfo(DivisionTeamAssignmentShort entity,
                                                                   List<PositionAssignmentEntity> positionAssignments,
                                                                   List<DivisionTeamAssignmentRotationShortDto> rotations) {
        return new DivisionTeamAssignmentDto(entity.getId(),
            EmployeeInfoFactory.createWithJobInfo(new EmployeeEntity(entity.getEmployeeId()), positionAssignments),
            null,
            entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
            entity.getDateFrom(), entity.getDateTo(),
            entity.getType() != null ? AssignmentTypeFactory.create(entity.getType()) : null,
            entity.getStatus() != null ? AssignmentStatusFactory.create(entity.getStatus()) : null,
            rotations, entity.getExternalId(),
            null);
    }

    public static DivisionTeamAssignmentDto createShort(DivisionTeamAssignmentShort entity,
                                                        List<DivisionTeamAssignmentRotationShortDto> rotations) {
        return new DivisionTeamAssignmentDto(entity.getId(),
            new EmployeeInfoDto().setId(entity.getEmployeeId()),
            null,
            entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
            entity.getDateFrom(), entity.getDateTo(),
            //new AssignmentTypeDto().setId(entity.getTypeId())
            entity.getType() != null ? AssignmentTypeFactory.create(entity.getType()) : null,
            entity.getStatus() != null ? AssignmentStatusFactory.create(entity.getStatus()) : null,
            rotations, entity.getExternalId(),
            null);
    }

    public static DivisionTeamAssignmentShortDto createShort(DivisionTeamAssignmentEntity entity,
                                                             List<DivisionTeamAssignmentRotationShortDto> rotations) {
        return new DivisionTeamAssignmentShortDto(entity.getId(),
            EmployeeInfoFactory.create(entity.getEmployee()),
            entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
            entity.getDateFrom(), entity.getDateTo(),
            entity.getType() != null ? AssignmentTypeFactory.create(entity.getType()) : null,
            entity.getStatus() != null ? AssignmentStatusFactory.create(entity.getStatus()) : null,
            rotations, entity.getExternalId());
    }

    public static DivisionTeamAssignmentShortDto createShortWithJobInfo(DivisionTeamAssignmentEntity entity,
                                                                        List<PositionAssignmentEntity> positionAssignments,
                                                                        List<DivisionTeamAssignmentRotationShortDto> rotations) {
        return new DivisionTeamAssignmentShortDto(entity.getId(),
            EmployeeInfoFactory.createWithJobInfo(entity.getEmployee(), positionAssignments),
            entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
            entity.getDateFrom(), entity.getDateTo(),
            entity.getType() != null ? AssignmentTypeFactory.create(entity.getType()) : null,
            entity.getStatus() != null ? AssignmentStatusFactory.create(entity.getStatus()) : null,
            rotations, entity.getExternalId());
    }

    public static DivisionTeamAssignmentShortDto createShort(DivisionTeamAssignmentEntity entity) {
        return createShort(entity, null);
    }

    public static DivisionTeamAssignmentDto createWithJobInfo(DivisionTeamAssignmentEntity entity,
                                                              List<PositionAssignmentEntity> positionAssignments,
                                                              List<DivisionTeamAssignmentRotationShortDto> rotations) {
        return new DivisionTeamAssignmentDto(entity.getId(),
            EmployeeInfoFactory.createWithJobInfo(entity.getEmployee(), positionAssignments),
            entity.getDivisionTeamRole() != null ? DivisionTeamRoleFactory.create(entity.getDivisionTeamRole()) : null,
            entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
            entity.getDateFrom(), entity.getDateTo(),
            entity.getType() != null ? AssignmentTypeFactory.create(entity.getType()) : null,
            entity.getStatus() != null ? AssignmentStatusFactory.create(entity.getStatus()) : null,
            rotations, entity.getExternalId(),
            entity.getDivisionTeamRole().getDivisionTeam() != null ? DivisionTeamFactory.create(entity.getDivisionTeamRole().getDivisionTeam()) : null);
    }

    public static DivisionTeamAssignmentDto createWithJobInfoAndFlags(DivisionTeamAssignmentEntity entity,
                                                                      List<PositionAssignmentEntity> positionAssignments,
                                                                      List<DivisionTeamAssignmentRotationShortDto> rotations,
                                                                      boolean withEmployee, boolean withDtr) {

        return new DivisionTeamAssignmentDto(entity.getId(),
            withEmployee ? (positionAssignments == null ? EmployeeInfoFactory.create(entity.getEmployee()) : EmployeeInfoFactory.createWithJobInfo(entity.getEmployee(), positionAssignments)) : null,
            withDtr && entity.getDivisionTeamRole() != null ? DivisionTeamRoleFactory.create(entity.getDivisionTeamRole()) : null,
            entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
            entity.getDateFrom(), entity.getDateTo(),
            entity.getType() != null ? AssignmentTypeFactory.create(entity.getType()) : null,
            entity.getStatus() != null ? AssignmentStatusFactory.create(entity.getStatus()) : null,
            rotations, entity.getExternalId(),
            entity.getDivisionTeamRole().getDivisionTeam() != null ? DivisionTeamFactory.create(entity.getDivisionTeamRole().getDivisionTeam()) : null);
    }

    public static DivisionTeamAssignmentShortDto createShortWithJobInfoAndFlags(DivisionTeamAssignmentShort entity,
                                                                                List<PositionAssignmentEntity> positionAssignments,
                                                                                List<DivisionTeamAssignmentRotationShortDto> rotations,
                                                                                EmployeeEntity employee, boolean withDtr) {
        return new DivisionTeamAssignmentShortDto(entity.getId(),
            employee != null ? EmployeeInfoFactory.createWithJobInfo(employee, positionAssignments) : null,
            entity.getFullName(), entity.getShortName(), entity.getAbbreviation(),
            entity.getDateFrom(), entity.getDateTo(),
            entity.getType() != null ? AssignmentTypeFactory.create(entity.getType()) : null,
            entity.getStatus() != null ? AssignmentStatusFactory.create(entity.getStatus()) : null,
            rotations, entity.getExternalId());
    }

    public static DivisionTeamAssignmentDto createWithJobInfo(DivisionTeamAssignmentEntity entity,
                                                              List<PositionAssignmentEntity> positionAssignments) {
        return createWithJobInfo(entity, positionAssignments, null);
    }

    public static DivisionTeamAssignmentWithDivisionTeamFullDto createWithDivisionTeamFull(DivisionTeamAssignmentEntity entity) {
        return new DivisionTeamAssignmentWithDivisionTeamFullDto(
            entity.getId(),
            EmployeeInfoFactory.create(entity.getEmployee()),
            entity.getFullName(),
            entity.getShortName(),
            entity.getAbbreviation(),
            entity.getDateFrom(),
            entity.getDateTo(),
            entity.getType() != null ? AssignmentTypeFactory.create(entity.getType()) : null,
            entity.getStatus() != null ? AssignmentStatusFactory.create(entity.getStatus()) : null,
            null,
            entity.getExternalId(),
            entity.getDivisionTeamRole() != null ? DivisionTeamFactory.createFull(entity.getDivisionTeamRole().getDivisionTeam()) : null
        );
    }
}
