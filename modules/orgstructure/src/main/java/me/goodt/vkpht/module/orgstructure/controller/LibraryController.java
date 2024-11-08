package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentReadinessFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.AssignmentRotationFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentReadinessDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.AssignmentRotationDao;
import me.goodt.vkpht.module.orgstructure.api.dto.AssignmentReadinessDto;
import me.goodt.vkpht.module.orgstructure.api.dto.AssignmentRotationDto;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.AssignmentReadinessFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.AssignmentRotationFactory;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentReadinessEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.AssignmentRotationEntity;
import me.goodt.vkpht.module.orgstructure.api.ILibraryService;

@RestController
@RequiredArgsConstructor
@GeneralAPIResponses
public class LibraryController {

    private final ILibraryService libraryService;
    private final AssignmentReadinessDao assignmentReadinessDao;
    private final AssignmentRotationDao assignmentRotationDao;
    private final UnitAccessService unitAccessService;

    @Operation(summary = "Получение всех ротаций назначений", description = "Получение всех ротаций назначений", tags = {"library"})
    @GetMapping("/api/library/assignmentrotation")
    @SurProtected(operation = SurOperation.UNIT)
    public List<AssignmentRotationDto> getAssignmentRotations() {
        List<AssignmentRotationDto> rotationDtos = new ArrayList<>();
        Iterable<AssignmentRotationEntity> rotationEntities = assignmentRotationDao.findAll(
            AssignmentRotationFilter.builder()
                .unitCode(unitAccessService.getCurrentUnit())
                .build()
        );
        rotationEntities.forEach(entity -> rotationDtos.add(AssignmentRotationFactory.create(entity)));
        return rotationDtos;
    }

    @Operation(summary = "Получение ротации назначения", description = "Получение ротации назначения по идентификатору", tags = {"library"})
    @BadRequestAPIResponses
    @GetMapping("/api/library/assignmentrotation/{id}")
    @SurProtected(operation = SurOperation.UNIT)
    public AssignmentRotationDto getAssignmentRotation(
            @Parameter(name = "id", description = "Идентификатор ротации назначения (таблица assignment_rotation)", example = "1")
            @PathVariable(name = "id") Integer id) throws NotFoundException {
        return AssignmentRotationFactory.create(libraryService.getAssignmentRotation(id));
    }

    @Operation(summary = "Получение всех готовностей к назначениям", description = "Получение всех готовностей к назначениям", tags = {"library"})
    @GetMapping("/api/library/assignmentreadiness")
    @SurProtected(operation = SurOperation.UNIT)
    public List<AssignmentReadinessDto> getAllAssignmentReadiness(@ParameterObject Pageable paging) {
        List<AssignmentReadinessDto> readinessDtos = new ArrayList<>();
        Iterable<AssignmentReadinessEntity> readinessEntities = assignmentReadinessDao.find(
            AssignmentReadinessFilter.builder()
                .unitCode(unitAccessService.getCurrentUnit())
                .build(),
            paging);
        readinessEntities.forEach(entity -> readinessDtos.add(AssignmentReadinessFactory.create(entity)));
        return readinessDtos;
    }

    @Operation(summary = "Получение готовности к назначению", description = "Получение готовности к назначению по идентификатору", tags = {"library"})
    @BadRequestAPIResponses
    @GetMapping("/api/library/assignmentreadiness/{id}")
    @SurProtected(operation = SurOperation.UNIT)
    public AssignmentReadinessDto getAssignmentReadiness(
            @Parameter(name = "id", description = "Идентификатор готовности к назначению (таблица assignment_readiness)", example = "1")
            @PathVariable(name = "id") Integer id) throws NotFoundException {
        return AssignmentReadinessFactory.create(libraryService.getAssignmentReadiness(id));
    }
}
