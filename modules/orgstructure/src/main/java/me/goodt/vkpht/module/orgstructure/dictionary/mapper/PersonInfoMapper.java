package me.goodt.vkpht.module.orgstructure.dictionary.mapper;

import me.goodt.vkpht.module.orgstructure.api.dto.passport.PassportDivisionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.PassportEmployeeDismissalDto;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.PassportLegalEntityDto;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.PassportPositionAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.PassportPositionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.PassportUnitDto;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.goodt.vkpht.common.application.util.PersonUtil;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.PassportEmployeeDto;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.PersonInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.SecondaryInfoDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeDismissalEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.UnitEntity;

@Component
public class PersonInfoMapper {

    public PersonInfoDto toPersonInfoDto(
            PersonEntity person,
            SecondaryInfoDto secondaryInfo,
            List<PassportEmployeeDto> employees
    ) {
        PersonInfoDto personInfo = new PersonInfoDto();
        personInfo.setId(person.getId());
        String fullName = PersonUtil.getFullName(
            person.getSurname(),
            person.getName(),
            person.getPatronymic()
        );
        personInfo.setFullName(fullName);
        personInfo.setShortName(person.getSurname() + " " + person.getName());
        personInfo.setPhoto(person.getPhoto());
        LocalDate dateOfBirth = person.getBirthDate().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        personInfo.setDateBirth(dateOfBirth);
        personInfo.setEmail(person.getEmail());
        personInfo.setTelegram(person.getTelegram());
        personInfo.setCity(person.getCity());

        personInfo.setSecondaryInfo(secondaryInfo);
        personInfo.setEmployees(employees);
        return personInfo;
    }

    public SecondaryInfoDto toSecondaryInfoDto(PersonEntity person, CitizenshipEntity citizenship) {
        SecondaryInfoDto secondaryInfoDto = new SecondaryInfoDto();
        secondaryInfoDto.setInn(person.getInn());
        secondaryInfoDto.setSnils(person.getSnils());
        secondaryInfoDto.setSex(person.getSex());
        secondaryInfoDto.setPostcode(person.getPostcode());
        if (citizenship != null) {
            secondaryInfoDto.setCitizenshipId(person.getCitizenshipId());
            secondaryInfoDto.setCitizenshipName(citizenship.getName());
        }
        return secondaryInfoDto;
    }

    public PassportEmployeeDto toPassportEmployeeDto(
            EmployeeEntity employee,
            Collection<UnitEntity> units,
            Collection<LegalEntityEntity> legalEntities,
            Collection<DivisionEntity> divisions,
            Collection<PositionEntity> positions,
            Collection<PositionAssignmentEntity> posAssignments,
            Collection<EmployeeDismissalEntity> employeeDismissals
    ) {
        PassportEmployeeDto passportEmployee = new PassportEmployeeDto();
        passportEmployee.setId(employee.getId());
        passportEmployee.setNumber(employee.getNumber());
        passportEmployee.setEmail(employee.getEmail());
        passportEmployee.setPhone(employee.getPhone());
        passportEmployee.setTelegram(employee.getTelegram());
        passportEmployee.setExternalId(employee.getExternalId());

        passportEmployee.setUnits(toUnitDtos(units));
        passportEmployee.setLegalEntities(toPassportLegalEntityDtos(legalEntities));
        passportEmployee.setDivisions(toPassportDivisionDtos(divisions));
        passportEmployee.setPositions(toPassportPositionDtos(positions));
        passportEmployee.setPositionAssignments(toPassportPositionAssignmentDtos(posAssignments));
        passportEmployee.setEmployeeDismissals(toPassportEmployeeDismissalDtos(employeeDismissals));
        return passportEmployee;
    }

    private PassportUnitDto toUnitDto(UnitEntity entity) {
        PassportUnitDto passportUnitDto = new PassportUnitDto();
        passportUnitDto.setCode(entity.getCode());
        passportUnitDto.setName(entity.getName());
        passportUnitDto.setDescription(entity.getDescription());
        passportUnitDto.setDateFrom(entity.getDateFrom());
        passportUnitDto.setDateTo(entity.getDateTo());
        return passportUnitDto;
    }

    private List<PassportUnitDto> toUnitDtos(Collection<UnitEntity> entities) {
        List<PassportUnitDto> dtos = new ArrayList<>(entities.size());
        for (UnitEntity entity : entities) {
            dtos.add(toUnitDto(entity));
        }
        return dtos;
    }

    private PassportLegalEntityDto toPassportLegalEntityDto(LegalEntityEntity entity) {
        PassportLegalEntityDto legalEntity = new PassportLegalEntityDto();
        legalEntity.setId(entity.getId());
        legalEntity.setShortName(entity.getShortName());
        legalEntity.setFullName(entity.getFullName());
        legalEntity.setDateFrom(entity.getDateFrom());
        legalEntity.setDateTo(entity.getDateTo());
        legalEntity.setManagementStructureTypeId(entity.getManagementStructureTypeId());
        return legalEntity;
    }

    private List<PassportLegalEntityDto> toPassportLegalEntityDtos(Collection<LegalEntityEntity> entities) {
        List<PassportLegalEntityDto> dtos = new ArrayList<>(entities.size());
        for (LegalEntityEntity entity : entities) {
            dtos.add(toPassportLegalEntityDto(entity));
        }
        return dtos;
    }

    private PassportDivisionDto toPassportDivisionDto(DivisionEntity entity) {
        PassportDivisionDto division = new PassportDivisionDto();
        division.setId(entity.getId());
        division.setFullName(entity.getFullName());
        division.setShortName(entity.getShortName());
        division.setDescription(entity.getDescription());
        division.setDateFrom(entity.getDateFrom());
        division.setDateTo(entity.getDateTo());
        division.setDateStart(entity.getDateStart());
        division.setDateEnd(entity.getDateEnd());
        division.setDateStartConfirm(entity.getDateStartConfirm());
        division.setDateEndConfirm(entity.getDateEndConfirm());
        return division;
    }

    private List<PassportDivisionDto> toPassportDivisionDtos(Collection<DivisionEntity> entities) {
        List<PassportDivisionDto> dtos = new ArrayList<>(entities.size());
        for (DivisionEntity entity : entities) {
            dtos.add(toPassportDivisionDto(entity));
        }
        return dtos;
    }

    private PassportPositionDto toPassportPositionDto(PositionEntity entity) {
        var position = new PassportPositionDto();
        position.setId(entity.getId());
        position.setFullName(entity.getFullName());
        position.setShortName(entity.getShortName());
        position.setDateFrom(entity.getDateFrom());
        position.setDateTo(entity.getDateTo());
        return position;
    }

    private List<PassportPositionDto> toPassportPositionDtos(Collection<PositionEntity> entities) {
        List<PassportPositionDto> dtos = new ArrayList<>(entities.size());
        for (PositionEntity entity : entities) {
            dtos.add(toPassportPositionDto(entity));
        }
        return dtos;
    }

    private PassportPositionAssignmentDto toPassportPositionAssignmentDto(PositionAssignmentEntity entity) {
        var posAssignment = new PassportPositionAssignmentDto();
        posAssignment.setId(entity.getId());
        posAssignment.setFullName(entity.getFullName());
        posAssignment.setShortName(entity.getShortName());
        posAssignment.setDateFrom(entity.getDateFrom());
        posAssignment.setDateTo(entity.getDateTo());
        return posAssignment;
    }

    private List<PassportPositionAssignmentDto> toPassportPositionAssignmentDtos(
            Collection<PositionAssignmentEntity> entities) {
        List<PassportPositionAssignmentDto> dtos = new ArrayList<>(entities.size());
        for (PositionAssignmentEntity entity : entities) {
            dtos.add(toPassportPositionAssignmentDto(entity));
        }
        return dtos;
    }

    private PassportEmployeeDismissalDto toPassportEmployeeDismissalDto(EmployeeDismissalEntity entity) {
        var employeeDismissal = new PassportEmployeeDismissalDto();
        employeeDismissal.setId(entity.getId());
        employeeDismissal.setCommentHr(entity.getCommentHr());
        employeeDismissal.setCommentRuk(entity.getCommentRuk());
        employeeDismissal.setDismissalReasonId(entity.getDismissalReason().getId());
        employeeDismissal.setDismissalTypeId(entity.getDismissalType().getId());
        return employeeDismissal;
    }

    private List<PassportEmployeeDismissalDto> toPassportEmployeeDismissalDtos(
            Collection<EmployeeDismissalEntity> entities) {
        List<PassportEmployeeDismissalDto> dtos = new ArrayList<>(entities.size());
        for (EmployeeDismissalEntity entity : entities) {
            dtos.add(toPassportEmployeeDismissalDto(entity));
        }
        return dtos;
    }
}
