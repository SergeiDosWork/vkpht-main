package me.goodt.vkpht.module.orgstructure.api;

import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;

public interface CalculationRiskService {

    void calculationRisk(Long positionId) throws NotFoundException;

    boolean preRetirementAge(PersonEntity person);

    boolean retirementAge(PersonEntity person);

    boolean vacancyAll(PositionAssignmentEntity positionAssignment);

    boolean vacancyExceptOut(PositionAssignmentEntity positionAssignment);

    boolean reservAll(PositionAssignmentEntity positionAssignment);

    boolean reservExceptOperative(PositionAssignmentEntity positionAssignment);

}
