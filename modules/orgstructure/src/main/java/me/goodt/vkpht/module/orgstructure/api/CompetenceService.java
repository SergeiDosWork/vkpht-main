package me.goodt.vkpht.module.orgstructure.api;

import java.util.Collection;
import java.util.List;

import com.goodt.drive.rtcore.dto.rostalent.competence.CompetenceDto;
import com.goodt.drive.rtcore.dto.tasksetting.CatalogsByUserDto;
import com.goodt.drive.rtcore.dto.tasksetting.CompetenceDataDto;
import me.goodt.vkpht.common.application.exception.JsonException;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.common.domain.entity.rostalent.entities.CompetenceEntity;
import me.goodt.vkpht.common.domain.entity.rostalent.entities.CompetenceProfilePositionEntity;

public interface CompetenceService {
    List<CompetenceDataDto> getCompetencesData(Long employeeId, Long evaluationEventId)
            throws JsonException, NotFoundException;

    CatalogsByUserDto getCatalogsByUser(Long employeeId, Long selectedEmployeeId, Long evaluationEventId)
            throws JsonException;

    List<CompetenceDto> getCompetencesByCompetenceProfile(Long competenceProfileId);

	CompetenceEntity getCompetenceEntity(Long competenceId) throws NotFoundException;

	List<CompetenceDto> getCompetences(List<Long> competencesIds) throws NotFoundException;

    List<CompetenceDto> getCompetencesByIds(Collection<Long> competencesIds) throws NotFoundException;

	CompetenceDto getCompetence(Long id) throws NotFoundException;

	List<CompetenceProfilePositionEntity> getAllCompetenceProfilePosition(Long positionId);

	CompetenceProfilePositionEntity getCompetenceProfilePosition(Long id) throws NotFoundException;

}
