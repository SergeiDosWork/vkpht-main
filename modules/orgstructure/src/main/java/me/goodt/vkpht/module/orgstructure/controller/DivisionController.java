package me.goodt.vkpht.module.orgstructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.goodt.drive.auth.sur.unit.IncorrectUnitException;
import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.common.api.annotation.Performance;
import me.goodt.vkpht.module.orgstructure.api.dto.*;
import me.goodt.vkpht.common.api.exception.BadRequestException;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionGroupFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionShortInfoFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.DivisionTeamSuccessorFactory;
import me.goodt.vkpht.module.orgstructure.api.DivisionService;
import me.goodt.vkpht.common.application.util.TextConstants;

import static java.util.stream.Collectors.toList;

@Performance
@RestController
@GeneralAPIResponses
public class DivisionController {

    @Autowired
    private DivisionService divisionService;

    @Operation(summary = "Получение инфорации о подразделении", description = "Получение информации о подразделении и его руководителе", tags = {"division"})
    @GetMapping("/api/division/info")
    public DivisionInfoDto getInfo(
            @Parameter(name = "id", description = "Идентификатор подразделения (таблица division).", example = "112")
            @RequestParam(name = "id") Long id) {
        return divisionService.getDivisionInfoDto(id);
    }

    @Operation(summary = "Получение информации об актуальном списке групп подразделений (в выборку не входят закрытые записи)",
            description = "Получение информации об актуальном списке групп подразделений (в выборку не входят закрытые записи)",
            tags = {"division"})
    @GetMapping("/api/division_group")
    public List<DivisionGroupDto> divisionGroupInfo(
            @Parameter(name = "id", description = "Массив идентификаторов групп подразделений (таблица division_group).", example = "[1,2,3]")
            @RequestParam(name = "id", required = false) List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            return divisionService.getDivisionGroupEntitiesByIds(ids).stream().map(DivisionGroupFactory::create).collect(toList());
        } else {
            return divisionService.getAllDivisionGroupEntities().stream().map(DivisionGroupFactory::create).collect(toList());
        }
    }

    @Deprecated
    @Operation(summary = "Получение инфорации о списке подразделений (является устаревшим, использовать POST)", description = "Получение информации о списке подразделений", tags = {"division"})
    @GetMapping("/api/division/list")
    public List<DivisionInfoDto> listInfoDeprecated(
            @Parameter(name = "division_id", description = "Массив идентификаторов подразделения (таблица division).", example = "[1,2,3]")
            @RequestParam(name = "division_id", required = false) List<Long> divisionIds,
            @Parameter(name = "parent", description = "Идентификатор родительского подразделения (таблица division).", example = "112")
            @RequestParam(name = "parent", required = false) Long parentId,
            @Parameter(name = "legalentity", description = "Идентификатор организации (таблица division).", example = "112")
            @RequestParam(name = "legalentity", required = false) Long legalEntityId,
            @Parameter(name = "withchilds", description = "Необходимо ли получать подчиненных от подчиненных по parent_id (таблица division).", example = "true")
            @RequestParam(name = "withchilds", required = false, defaultValue = "false") boolean withChilds,
            @Parameter(name = "group_id", description = "Массив идентификаторов группы (таблица group).", example = "[1,2,3]")
            @RequestParam(name = "group_id", required = false) List<Long> groupIds) {
        return divisionService.getDivisionInfoByParams(divisionIds, parentId, legalEntityId, groupIds, withChilds);
    }

    @Operation(summary = "Получение инфорации о списке подразделений", description = "Получение информации о списке подразделений", tags = {"division"})
    @PostMapping("/api/division/list")
    public List<DivisionInfoDto> listInfo(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO для получения инфорации о списке подразделений")
            @RequestBody DivisionInfoRequestDto dto) {
        return divisionService.getDivisionInfoByParams(dto.getDivisionIds(), dto.getParentId(), dto.getLegalEntityId(), dto.getGroupIds(), dto.isWithChilds());
    }

    @Operation(summary = "Положение в организационной структуре", description = "Получение информации о положении подразделения в организационной структуре", tags = {"division"})
    @GetMapping("/api/division/path")
    public List<DivisionShortInfoDto> getPath(
            @Parameter(name = "id", description = "Идентификатор подразделения (таблица division).", example = "112")
            @RequestParam(name = "id") Long id) {
        try {
            return divisionService.getPath(id).stream().map(DivisionShortInfoFactory::create).collect(toList());
        } catch (IncorrectUnitException unitException) {
            throw unitException;
        } catch (RuntimeException e) {
            throw new RuntimeException("Loop inside division parent relations");
        }
    }

    @Operation(summary = "Получение информации о должностях и их назначениях в подразделении", description = "Получение информации о должностях и их назначениях в подразделении", tags = {"division"})
    @GetMapping("/api/division/position")
    public List<PositionExtendedDto> getPositions(
            @Parameter(name = "legal_entity", description = "Идентификатор организации (таблица legal_entity).", example = "1")
            @RequestParam(name = "legal_entity", required = false) Long legalEntityId,
            @Parameter(name = "has_position_assignment", description = "По-умолчанию \"true\" - метод вернет только позиции с активными назначениями сотрудников в таблице position_assignments;  при \"false\" метод вернет только незанятые позиции (без назначений ).", example = "true")
            @RequestParam(name = "has_position_assignment", required = false, defaultValue = "true") boolean hasPositionAssignment,
            @Parameter(name = "division", description = "Массив идентификаторов подразделения (таблица division).", example = "[1,2,3]")
            @RequestParam(name = "division", required = false) List<Long> divisionIds,
            @Parameter(name = "position_id", description = "Массив идентификаторов должностей (таблица position).", example = "[6,7,8]")
            @RequestParam(name = "position_id", required = false) List<Long> positionIds) throws BadRequestException {
        if ((divisionIds == null || divisionIds.isEmpty()) && (positionIds == null || positionIds.isEmpty()) && legalEntityId == null) {
            throw new BadRequestException(TextConstants.BAD_REQUEST_MESSAGE_ALL_PARAMETERS_ARE_NULL);
        }
        return divisionService.getExtendedPositions(divisionIds, positionIds, hasPositionAssignment, legalEntityId);
    }

    @PostMapping("/api/division/rebuild-division-tree")
    public String rebuildDivisionTree() {
        divisionService.rebuildDivisionTree();
        return "rebuild success";
    }

    @Operation(summary = "Получение информации о преемнике", description = "Получение информации о преемнике", tags = {"division"})
    @BadRequestAPIResponses
    @GetMapping("/api/divisionteamsuccessor/{id}")
    public DivisionTeamSuccessorDto getDivisionTeamSuccessor(
            @Parameter(name = "id", description = "Идентификатор преемника (таблица division_team_successor).", example = "3")
            @PathVariable("id") Long id) throws NotFoundException {
        return DivisionTeamSuccessorFactory.create(divisionService.getDivisionTeamSuccessor(id));
    }

    @Operation(summary = "Получение подразделения по типу", description = "Получение подразделения по типу", tags = {"division"})
    @BadRequestAPIResponses
    @GetMapping("/api/division/team-by-type")
    public DivisionTeamDto getDivisionTeamByType(
            @Parameter(name = "divisionTeamTypeId", description = "Тип команды подразделения", example = "3")
            @RequestParam(name = "divisionTeamTypeId") Integer divisionTeamTypeId,
            @Parameter(name = "employeeId", description = "Идентификатор сотрудника", example = "3")
            @RequestParam(name = "employeeId") Long employeeId) {
        return DivisionTeamFactory.create(divisionService.getDivisionTeamByTypeAndEmployeeId(divisionTeamTypeId, employeeId));
    }

    @Operation(summary = "Получение информации о подразделениях в зависимости от переданных параметров", description = "Получение информации о подразделениях в зависимости от переданных параметров.", tags = {"division"})
    @PostMapping("/api/division/find")
    public List<DivisionDto> findPost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Объект, содержащий параметров для поиска подразделений",
                    content = @Content(schema = @Schema(implementation = DivisionFindDto.class)))
            @RequestBody DivisionFindDto data) throws NotFoundException {
        return divisionService.findPost(data);
    }
}
