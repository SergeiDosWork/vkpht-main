package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeExtendedDto;
import com.goodt.drive.rtcore.dto.rostalent.competence.EmployeeCardInEvaluationEvent;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;

@UtilityClass
public class EmployeeFactory {

    public static EmployeeDto create(EmployeeEntity entity) {
        return new EmployeeDto(entity.getId(),
                               entity.getPerson() != null ? entity.getPerson().getId() : null,
                               entity.getNumber(), entity.getPhone(), entity.getEmail(), entity.getFax(),
                               entity.getIsHasMobile(), entity.getIsFreelancer(), entity.getDateFrom(), entity.getDateTo(),
                               entity.getExternalId());
    }

    public static EmployeeExtendedDto createExtended(EmployeeEntity entity) {
        return new EmployeeExtendedDto(entity.getId(),
                                       entity.getPerson() != null ? PersonFactory.create(entity.getPerson()) : null,
                                       entity.getNumber(), entity.getPhone(), entity.getEmail(), entity.getFax(),
                                       entity.getIsHasMobile(), entity.getIsFreelancer(), entity.getDateFrom(), entity.getDateTo(),
                                       entity.getExternalId());
    }

    public static EmployeeCardInEvaluationEvent createForCardInEvaluationEvent(EmployeeEntity entity, List<PositionAssignmentEntity> possAssList, Long taskId){
        return new EmployeeCardInEvaluationEvent(
            entity.getId(),
            entity.getPerson().getSurname(),
            entity.getPerson().getName(),
            entity.getPerson().getPatronymic(),
            entity.getPerson().getPhoto(),
            possAssList.stream().map(x->x.getPosition().getShortName()).collect(Collectors.toList())
        );
    }
}
