package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.api.exception.BadRequestException;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.domain.dao.filter.PositionAssignmentFilter;
import me.goodt.vkpht.module.orgstructure.api.CalculationRiskService;
import me.goodt.vkpht.module.orgstructure.api.PositionService;
import me.goodt.vkpht.module.orgstructure.domain.dao.ImportanceCriteriaDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionImportanceCriteriaDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionSuccessorDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionSuccessorReadinessDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.PositionSuccessorFilter;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionImportanceCriteriaEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionSuccessorReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionTypeEntity;

@RequiredArgsConstructor
@Service
@Transactional
public class CalculationRiskServiceImpl implements CalculationRiskService {

    private static final String FEMALE = "f";
    private static final String MALE = "m";
    private static final String PRE_PENSION_AGE = "pre_pension_age";
    private static final String PENSION_AGE = "pension_age";
    private static final String VACANCY_ALL = "vacancy_all";
    private static final String VACANCY_EXCEPT_OUT = "vacancy_except_out";
    private static final String RESERV_ALL = "reserv_all";
    private static final String RESERV_EXCEPT_OPERATIVE = "reserv_except_operative";

    private final ImportanceCriteriaDao importanceCriteriaDao;
    private final PositionService positionService;
    private final PositionSuccessorDao positionSuccessorDao;
    private final PositionSuccessorReadinessDao positionSuccessorReadinessDao;
    private final PositionAssignmentDao positionAssignmentDao;
    private final PositionDao positionDao;
    private final PositionImportanceCriteriaDao positionImportanceCriteriaDao;
    private final UnitAccessService unitAccessService;

    @Override
    public void calculationRisk(Long legalEntityId) throws NotFoundException {
        List<ImportanceCriteriaEntity> importanceCriteria = importanceCriteriaDao.findAllEnabledWithCalcMethod();
        List<PositionAssignmentEntity> positionAssignmentList = positionAssignmentDao.findAll(
            PositionAssignmentFilter.builder()
                .legalEntityId(legalEntityId)
                .unitCode(unitAccessService.getCurrentUnit())
                .build()
        );
        if (importanceCriteria.isEmpty() || positionAssignmentList.isEmpty()) {
            throw new BadRequestException(String.format("List ImportanceCriteria(size:%d) or PositionAssignment(size:%d) is empty", importanceCriteria.size(), positionAssignmentList.size()));
        }

        for (PositionAssignmentEntity positionAssignment : positionAssignmentList) {
            for (ImportanceCriteriaEntity item : importanceCriteria) {
                switch (item.getCalculationMethod().getCode()) {
                    case PRE_PENSION_AGE: {
                        PersonEntity person = positionAssignment.getEmployee().getPerson();
                        boolean preRetirementAge = preRetirementAge(person);
                        createOrUpdate(positionAssignment.getPositionId(), item, preRetirementAge, 0);
                        break;
                    }
                    case PENSION_AGE: {
                        PersonEntity person = positionAssignment.getEmployee().getPerson();
                        boolean retirementAge = retirementAge(person);
                        createOrUpdate(positionAssignment.getPositionId(), item, retirementAge, 0);
                        break;
                    }
                    case VACANCY_ALL: {
                        boolean vacancyAll = vacancyAll(positionAssignment);
                        createOrUpdate(positionAssignment.getPositionId(), item, vacancyAll, 0);
                        break;
                    }
                    case VACANCY_EXCEPT_OUT: {
                        boolean vacancyExceptOut = vacancyExceptOut(positionAssignment);
                        createOrUpdate(positionAssignment.getPositionId(), item, vacancyExceptOut, 0);
                        break;
                    }
                    case RESERV_ALL: {
                        boolean reservAll = reservAll(positionAssignment);
                        createOrUpdate(positionAssignment.getPositionId(), item, reservAll, 0);
                        break;
                    }
                    case RESERV_EXCEPT_OPERATIVE: {
                        boolean reservExceptOperative = reservExceptOperative(positionAssignment);
                        createOrUpdate(positionAssignment.getPositionId(), item, reservExceptOperative, 1);
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

    private void createOrUpdate(Long positionId, ImportanceCriteriaEntity item, boolean checkResult, float value) throws NotFoundException {
        Optional<PositionImportanceCriteriaEntity> optionalPositionImportanceCriteria = positionImportanceCriteriaDao.findAllByPositionIdAndImportanceCriteriaId(positionId, item.getId())
            .stream()
            .findFirst();

        if (optionalPositionImportanceCriteria.isPresent() && !checkResult) {
            PositionImportanceCriteriaEntity positionImportanceCriteriaEntity = optionalPositionImportanceCriteria.get();
            positionImportanceCriteriaEntity.setDateTo(new Date());
            positionImportanceCriteriaDao.save(positionImportanceCriteriaEntity);
        } else if (optionalPositionImportanceCriteria.isEmpty() && checkResult) {
            PositionImportanceCriteriaEntity entity = new PositionImportanceCriteriaEntity();
            entity.setDateFrom(new Date());
            PositionEntity position = positionDao.getById(positionId);
            checkUnit(position);
            entity.setPosition(position);
            entity.setImportanceCriteria(item);
            entity.setValue(value);
            entity.setWeight(item.getWeight());
            positionImportanceCriteriaDao.save(entity);
        }
    }

    @Override
    public boolean vacancyAll(PositionAssignmentEntity positionAssignment) {
        Date dateTo = positionAssignment.getDateTo();
        return dateTo != null;
    }

    @Override
    public boolean vacancyExceptOut(PositionAssignmentEntity positionAssignment) {
        PositionTypeEntity positionType = positionAssignment.getPosition().getPositionType();
        if (positionType != null && positionType.getId().equals(3L)) {
            return false;
        } else {
            Date dateTo = positionAssignment.getDateTo();
            return dateTo != null;
        }
    }

    @Override
    public boolean reservAll(PositionAssignmentEntity positionAssignment) {
        if (positionAssignment.getDateTo() != null) {
            return false;
        }

        Long employeeId = positionAssignment.getEmployeeId();
        List<PositionSuccessorEntity> positionSuccessors = positionSuccessorDao.findAll(
            PositionSuccessorFilter.builder()
                .actual(false)
                .employeeId(employeeId)
                .build()
        );
        if (positionSuccessors.isEmpty()) {
            return false;
        }

        List<Long> openPositionSuccessorIds = new ArrayList<>();
        int closedPositionSuccessorCount = 0;

        for (PositionSuccessorEntity positionSuccessor : positionSuccessors) {
            if (positionSuccessor.getDateTo() == null) {
                openPositionSuccessorIds.add(positionSuccessor.getId());
            } else {
                closedPositionSuccessorCount++;
            }
        }
        if (positionSuccessors.size() == closedPositionSuccessorCount) {
            return false;
        }

        List<PositionSuccessorReadinessEntity> positionSuccessorReadiness = positionSuccessorReadinessDao.getByPositionSuccessorIds(openPositionSuccessorIds, unitAccessService.getCurrentUnit());
        for (PositionSuccessorReadinessEntity psr : positionSuccessorReadiness) {
            if (psr.getDateTo() != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean reservExceptOperative(PositionAssignmentEntity positionAssignment) {
        if (positionAssignment.getDateTo() != null) {
            return false;
        }

        Long employeeId = positionAssignment.getEmployeeId();
        List<PositionSuccessorEntity> positionSuccessors = positionSuccessorDao.findAll(PositionSuccessorFilter.builder()
            .actual(false)
            .employeeId(employeeId)
            .build()
        );
        if (positionSuccessors.isEmpty()) {
            return false;
        }

        List<Long> openPositionSuccessorIds = new ArrayList<>();
        int closedPositionSuccessorCount = 0;

        for (PositionSuccessorEntity positionSuccessor : positionSuccessors) {
            if (positionSuccessor.getDateTo() == null) {
                openPositionSuccessorIds.add(positionSuccessor.getId());
            } else {
                closedPositionSuccessorCount++;
            }
        }
        if (positionSuccessors.size() == closedPositionSuccessorCount) {
            return false;
        }

        List<PositionSuccessorReadinessEntity> positionSuccessorReadiness = positionSuccessorReadinessDao.getByPositionSuccessorIds(openPositionSuccessorIds, unitAccessService.getCurrentUnit());
        for (PositionSuccessorReadinessEntity psr : positionSuccessorReadiness) {
            if (psr.getDateTo() != null) {
                return false;
            } else if (psr.getReadiness() != null && List.of(1, 2).contains(psr.getReadiness().getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean retirementAge(PersonEntity person) {
        if (person.getSex() == null || person.getBirthDate() == null) {
            throw new BadRequestException(String.format("Person gender or date of birth is null, gender:%s, DOB:%s", person.getSex(), person.getBirthDate()));
        }
        String sex = person.getSex().toLowerCase().trim();
        LocalDateTime birthDate = convertToLocalDateTime(person.getBirthDate());
        int birthYear = birthDate.getYear();
        int currentYear = LocalDateTime.now().getYear();
        int age = currentYear - birthYear;

        if (sex.equals(MALE)) {
            if (birthYear <= 1960) {
                return true;

            } else if (birthYear == 1961) {
                int result = 63 - age;
                return result <= 0;

            } else if (birthYear == 1962) {
                int result = 64 - age;
                return result <= 0;

            } else {
                int result = 65 - age;
                return result <= 0;
            }

        } else if (sex.equals(FEMALE)) {
            if (birthYear <= 1965) {
                return true;

            } else if (birthYear == 1966) {
                int result = 58 - age;
                return result <= 0;

            } else if (birthYear == 1967) {
                int result = 59 - age;
                return result <= 0;

            } else {
                int result = 60 - age;
                return result <= 0;
            }
        }
        return false;
    }

    @Override
    public boolean preRetirementAge(PersonEntity person) {
        if (person.getSex() == null || person.getBirthDate() == null) {
            throw new BadRequestException(String.format("Person gender or date of birth is null, gender:%s, DOB:%s", person.getSex(), person.getBirthDate()));
        }
        String sex = person.getSex().toLowerCase().trim();
        LocalDateTime birthDate = convertToLocalDateTime(person.getBirthDate());
        int birthYear = birthDate.getYear();
        int currentYear = LocalDateTime.now().getYear();
        int age = currentYear - birthYear;

        if (sex.equals(MALE)) {
            if (birthYear >= 1963) {
                int result = 65 - age;
                return result > 0 && result < 5;

            } else if (birthYear == 1962) {
                int result = 64 - age;
                return result > 0 && result < 5;

            } else if (birthYear == 1961) {
                int result = 63 - age;
                return result > 0 && result < 5;

            } else {
                return false;
            }

        } else if (sex.equals(FEMALE)) {
            if (birthYear >= 1968) {
                int result = 60 - age;
                return result > 0 && result < 5;

            } else if (birthYear == 1967) {
                int result = 59 - age;
                return result > 0 && result < 5;

            } else if (birthYear == 1966) {
                int result = 58 - age;
                return result > 0 && result < 5;

            } else {
                return false;
            }
        }
        return false;
    }

    public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(dateToConvert.toInstant(), ZoneId.systemDefault());
    }

    private void checkUnit(PositionEntity entity) {
        if (!entity.getDivision().getLegalEntityEntity().getUnitCode().equals(unitAccessService.getCurrentUnit())) {
            throw new NotFoundException(String.format("Сущность Position c id = %d не была найдена", entity.getId()));
        }
    }
}
