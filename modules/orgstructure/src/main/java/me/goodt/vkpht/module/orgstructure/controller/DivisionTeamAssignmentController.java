package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentFindRequestDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentWithDivisionTeamFullDto;
import me.goodt.vkpht.module.orgstructure.api.dto.projection.DivisionTeamAssignmentCompactProjection;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamAssignmentFactory;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentEntity;
import com.goodt.drive.rtcore.service.logging.ILoggerService;
import me.goodt.vkpht.module.orgstructure.api.IAssignmentService;
import me.goodt.vkpht.module.orgstructure.api.IDivisionService;

@Slf4j
@GeneralAPIResponses
@RestController
@RequiredArgsConstructor
public class DivisionTeamAssignmentController {

    private final IDivisionService divisionService;
    private final IAssignmentService assignmentService;
    private final ILoggerService loggerService;

    @Operation(summary = "Получение списка назначений c пагинацией", description = "Получение списка назначений c пагинацией по переданным параметрам", tags = {"division_team_assignment"})
    @PostMapping("/api/divisionteamassignment/find")
    public Page<DivisionTeamAssignmentDto> findDivisionTeamAssignmentPage(
        @PageableDefault(page = 0, size = 10)
        @SortDefault.SortDefaults({
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
        }) Pageable pageable,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для поиска", content = @Content(schema = @Schema(implementation = DivisionTeamAssignmentFindRequestDto.class)))
        @RequestBody DivisionTeamAssignmentFindRequestDto request) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/divisionteamassignment/find", null, request);

        Page<DivisionTeamAssignmentEntity> divisionTeamAssignment = divisionService.findAllDivisionTeamAssignment(request, pageable);
        return divisionTeamAssignment.map(DivisionTeamAssignmentFactory::create);
    }

    @Operation(summary = "Получение списка назначений", description = "Получение списка назначений по переданным параметрам", tags = {"division_team_assignment"})
    @PostMapping("/api/divisionteamassignment/findlist")
    public List<DivisionTeamAssignmentDto> findDivisionTeamAssignment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для поиска", content = @Content(schema = @Schema(implementation = DivisionTeamAssignmentFindRequestDto.class)))
            @RequestBody DivisionTeamAssignmentFindRequestDto request) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/divisionteamassignment/findlist", null, request);

        return divisionService.findAllDivisionTeamAssignment(request)
                .stream()
                .map(DivisionTeamAssignmentFactory::create)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Получение информации о назначениях по position_category_id", description = "Получение информации о назначениях по position_category_id", tags = {"employee"})
    @GetMapping("/api/division-team-assignment/by-position-category")
    public List<DivisionTeamAssignmentDto> getAssignmentByPositionCategory(
            @Parameter(name = "position_category_id", description = "ИД position_category")
            @RequestParam(name = "position_category_id") Long positionCategoryId) {
        return assignmentService.getAssignmentByPositionCategory(positionCategoryId);
    }

    @Operation(summary = "Получение списка ИД назначений", description = "Получение списка ИД назначений по переданным параметрам", tags = {"division_team_assignment"})
    @PostMapping("/api/division-team-assignment/find-id-list")
    public List<Long> findDivisionTeamAssignmentIds(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для поиска", content = @Content(schema = @Schema(implementation = DivisionTeamAssignmentFindRequestDto.class)))
            @RequestBody DivisionTeamAssignmentFindRequestDto request) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/division-team-assignment/find-id-list", null, request);

        return divisionService.findAllDivisionTeamAssignmentIds(request);
    }

    @Operation(summary = "Получение списка назначений с полностью заполненными командами и дивизионами", description = "Получение списка назначений с полностью заполненными командами и дивизионами", tags = {"division_team_assignment"})
    @GetMapping("/api/division-team-assignment/find-list-with-division-team-full")
    public List<DivisionTeamAssignmentWithDivisionTeamFullDto> findDivisionTeamAssignmentWithDivisionTeamFull(
            @Parameter(name = "ids", description = "Массив ИД назначений")
            @RequestParam(name = "ids") List<Long> ids) {
        return assignmentService.findDivisionTeamAssignmentWithDivisionTeamFull(ids);
    }

    // Получение минимальной информации о назначениях в команду подразделения (метод используется в сервисе таск2)
    @Hidden
    @GetMapping("/api/division-team-assignment/compact-list")
    public List<DivisionTeamAssignmentCompactProjection> getDivisionTeamAssignmentsCompact(
            @RequestParam(name = "id", required = false) Collection<Long> ids,
            @RequestParam(name = "employee", required = false) Collection<Long> employeeIds) {
        StopWatch sw = StopWatch.createStarted();
        List<DivisionTeamAssignmentCompactProjection> result = assignmentService.getDivisionTeamAssignmentCompactList(ids, employeeIds);
        sw.stop();
        log.info("/api/division-team-assignment/compact-list total {}", sw.getTime());
        return result;
    }
}
