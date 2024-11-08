package me.goodt.vkpht.module.orgstructure.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.goodt.drive.rtcore.api.orgstructure.filter.UnitFilter;
import com.goodt.drive.rtcore.dictionary.orgstructure.mapper.PersonInfoMapper;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.entity.DomainObject;
import me.goodt.vkpht.module.orgstructure.api.EmployeePassportService;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.PassportEmployeeDto;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.PersonInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.passport.SecondaryInfoDto;
import me.goodt.vkpht.module.orgstructure.domain.dao.CitizenshipDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDismissalDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.UnitDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeDismissalEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.LegalEntityEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.UnitEntity;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class EmployeePassportServiceImpl implements EmployeePassportService {

    private final CitizenshipDao citizenshipDao;
    private final UnitDao unitDao;
    private final EmployeeDao employeeDao;
    private final EmployeeDismissalDao employeeDismissalDao;
    private final PositionAssignmentDao positionAssignmentDao;

    private final PersonInfoMapper personInfoMapper;

    @Override
    @Transactional
    public PersonInfoDto getPersonInfo(Long employeeId, boolean includeSecondaryInfo) {
        EmployeeEntity employee = employeeDao.findById(employeeId)
            .orElseThrow(() -> new NotFoundException("Сотрудник с id = " + employeeId + " не найден"));
        return buildPersonInfo(employee.getPerson(), includeSecondaryInfo);
    }

    @Override
    @Transactional
    public List<PersonInfoDto> getPersonInfo(String snils, boolean includeSecondaryInfo) {
        return employeeDao.findAllBySnils(snils)
            .stream()
            .map(EmployeeEntity::getPerson)
            .distinct()
            .map(person -> buildPersonInfo(person, includeSecondaryInfo))
            .collect(toList());
    }

    private PersonInfoDto buildPersonInfo(PersonEntity person, boolean includeSecondaryInfo) {
        List<EmployeeEntity> employees = employeeDao.findAllByPersonId(person.getId());
        List<Long> employeeIds = employees.stream()
            .map(DomainObject::getId)
            .collect(toList());

        Map<Long, List<PositionAssignmentEntity>> positionAssignments = positionAssignmentDao
            .findAllByEmployeeId(employeeIds)
            .stream()
            .collect(groupingBy(
                PositionAssignmentEntity::getEmployeeId,
                mapping(Function.identity(), toList())
            ));

        Map<Long, List<EmployeeDismissalEntity>> employeeDismissals = employeeDismissalDao
            .findAllByEmployeeId(employeeIds)
            .stream()
            .collect(groupingBy(
                el -> el.getEmployee().getId(),
                mapping(Function.identity(), toList())
            ));

        List<PassportEmployeeDto> passports = new ArrayList<>();
        for (EmployeeEntity employee : employees) {
            PassportEmployeeDto passportEmployee = buildEmployeePassport(
                employee,
                positionAssignments.getOrDefault(employee.getId(), List.of()),
                employeeDismissals.getOrDefault(employee.getId(), List.of())
            );

            passports.add(passportEmployee);
        }

        SecondaryInfoDto secondaryInfoDto = includeSecondaryInfo ? getSecondaryInfo(person) : null;

        return personInfoMapper.toPersonInfoDto(person, secondaryInfoDto, passports);
    }

    private PassportEmployeeDto buildEmployeePassport(
        EmployeeEntity employee,
        List<PositionAssignmentEntity> positionAssignments,
        List<EmployeeDismissalEntity> employeeDismissals
    ) {

        List<PositionEntity> positions = positionAssignments.stream()
            .map(PositionAssignmentEntity::getPosition)
            .collect(toList());

        List<DivisionEntity> divisions = positions
            .stream()
            .map(PositionEntity::getDivision)
            .collect(toList());

        List<LegalEntityEntity> legalEntities = divisions.stream()
            .map(DivisionEntity::getLegalEntityEntity)
            .collect(toList());

        List<UnitEntity> units = Collections.emptyList();
        if (!legalEntities.isEmpty()) {
            List<String> unitCodes = legalEntities.stream()
                .map(LegalEntityEntity::getUnitCode)
                .collect(toList());

            var unitFilter = UnitFilter.builder()
                .codes(unitCodes)
                .actual(null)
                .build();
            units = unitDao.find(unitFilter);
        }

        return personInfoMapper.toPassportEmployeeDto(
            employee,
            units,
            legalEntities,
            divisions,
            positions,
            positionAssignments,
            employeeDismissals
        );
    }

    private SecondaryInfoDto getSecondaryInfo(PersonEntity person) {
        CitizenshipEntity citizenship = null;
        if (person.getCitizenshipId() != null) {
            citizenship = citizenshipDao.getById(person.getCitizenshipId());
        }

        return personInfoMapper.toSecondaryInfoDto(person, citizenship);
    }
}
