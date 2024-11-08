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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.auth.sur.service.SurProtectedAttr;
import com.goodt.drive.auth.sur.unit.UnitAccessService;
import me.goodt.vkpht.common.api.annotation.ApiPageable;
import me.goodt.vkpht.common.api.annotation.BadRequestAPIResponses;
import me.goodt.vkpht.common.api.annotation.GeneralAPIResponses;
import me.goodt.vkpht.common.api.annotation.Performance;
import me.goodt.vkpht.common.domain.dao.filter.PositionAssignmentFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.filter.DivisionFilter;
import me.goodt.vkpht.module.orgstructure.domain.dao.*;
import com.goodt.drive.rtcore.data.EmployeeInfo;
import com.goodt.drive.rtcore.data.EmployeeSearchResult;
import com.goodt.drive.rtcore.data.OperationResult;
import com.goodt.drive.rtcore.dto.DtoTagConstants;
import me.goodt.vkpht.module.orgstructure.api.dto.*;
import com.goodt.drive.rtcore.dto.rostalent.IdDto;
import com.goodt.drive.rtcore.dto.rostalent.ResponseNumberDto;
import com.goodt.drive.rtcore.dto.tasksetting2.EmployeesByStatusDto;
import com.goodt.drive.rtcore.dto.tasksetting2.FilterAwarePageResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.PageResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.ProcessEmployeeDto;
import com.goodt.drive.rtcore.dto.tasksetting2.goalsetting.EmployeeDto;
import com.goodt.drive.rtcore.dto.tasksetting2.goalsetting.input.CycleSubordinatesRequest;
import com.goodt.drive.rtcore.dto.tasksetting2.goalsetting.input.EmployeeByStatusRequest;
import com.goodt.drive.rtcore.dto.tasksetting2.goalsetting.input.ProcessEmployeeRequest;
import me.goodt.vkpht.common.application.exception.NotFoundException;
import me.goodt.vkpht.module.orgstructure.domain.factory.*;
import me.goodt.vkpht.module.orgstructure.domain.entity.*;
import com.goodt.drive.rtcore.security.AuthService;
import com.goodt.drive.rtcore.service.logging.ILoggerService;
import me.goodt.vkpht.module.orgstructure.api.*;
import me.goodt.vkpht.common.application.util.CoreUtils;

@Slf4j
@Performance
@GeneralAPIResponses
@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final IAssignmentService assignmentService;
    private final ILegalEntityTeamAssignmentService legalEntityTeamAssignmentService;
    private final IEmployeeService employeeService;
    private final IDivisionService divisionService;
    private final IDivisionTeamAssignmentRotationService divisionTeamAssignmentRotationService;
    private final ILoggerService loggerService;
    private final DivisionDao divisionDao;
    private final EmployeeDao employeeDao;
    private final DivisionTeamAssignmentDao divisionTeamAssignmentDao;
    private final DivisionTeamAssignmentRotationDao divisionTeamAssignmentRotationDao;
    private final DivisionTeamRoleDao divisionTeamRoleDao;
    private final PositionAssignmentDao positionAssignmentDao;
    private final AuthService authService;
    private final UnitAccessService unitAccessService;

    @Operation(summary = "Получение информации о сотруднике", description = "Получение информации о сотруднике", tags = {"employee"})
    @BadRequestAPIResponses
    @GetMapping("/api/employee/info")
    public EmployeeExtendedInfoDto getInfo(
        @Parameter(name = "id", description = "Идентификатор сотрудника (таблица employee).", example = "112")
        @RequestParam(name = "id", required = false) Long id,
        @Parameter(name = "external_employee", description = "Внешний идентификатор сотрудника (таблица employee).", example = "112")
        @RequestParam(name = "external_employee", required = false) String externalId) throws NotFoundException {
        if (id == null && externalId == null) {
            id = authService.getUserEmployeeId();
        }

        List<EmployeeEntity> employee = employeeDao.findByIdAndExternalId(id, externalId);
        if (employee.isEmpty()) {
            throw new NotFoundException(String.format("Employee with id=%d and external_employee=%s is not found", id, externalId));
        } else if (employee.size() > 1) {
            throw new NotFoundException(String.format("Found several employees with id=%d and external_employee=%s. " +
                                                          "Perhaps there is no record in the employee table. ", id, externalId));
        }

        EmployeeEntity uniqueEmployee = employee.get(0);
        EmployeeInfo info = employeeService.getEmployeeInfo(uniqueEmployee.getId());

        return EmployeeExtendedInfoFactory.create(uniqueEmployee, info.getAssignments());
    }

    @Operation(summary = "Получение информации о сотруднике", description = "Получение информации о сотруднике без вложенных объектов. " +
        "Если есть несколько назначений, то выводится первое найденное", tags = {"employee"})
    @BadRequestAPIResponses
    @GetMapping("/api/employee/dictionary")
    public List<EmployeeFlatInfoDto> getShortInfo(
        @Parameter(name = "id", description = "Идентификатор сотрудника (таблица employee).", example = "112")
        @RequestParam(name = "id", required = false) Long id,
        @Parameter(name = "external_employee", description = "Внешний идентификатор сотрудника (таблица employee).", example = "112")
        @RequestParam(name = "external_employee", required = false) String externalId) throws NotFoundException {

        return employeeService.getEmployeeFlatInfo(id, externalId);
    }

    @Operation(summary = "Получение информации о сотрудниках. Метод GET будет удаляться, используйте POST /api/employee/find", description = "Получение информации о сотрудниках. Метод GET будет удаляться, используйте POST /api/employee/find", tags = {"employee"})
    @GetMapping("/api/employee/find")
    public List<EmployeeInfoDto> findInfo(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @Parameter(name = "id", description = "Массив идентификаторов сотрудника (таблица employee).", example = "[1, 2, 3]")
            @RequestParam(name = "id", required = false) List<Long> employeeIds,
            @Parameter(name = "division", description = "Идентификатор подразделения (таблица employee).", example = "112")
            @RequestParam(name = "division", required = false) List<Long> divisionIds,
            @Parameter(name = "function", description = "Идентификатор выполняемой функции (таблица employee).", example = "112")
            @RequestParam(name = "function", required = false) List<Long> functionIds,
            @Parameter(name = "search", description = "Строка поиска по фамилии, имени, отчеству или номеру сотрудника (таблица employee).", example = "Ива Иванович")
            @RequestParam(name = "search", required = false) String searchingValue) {
        List<EmployeeEntity> employees = employeeService.findEmployee(employeeIds, divisionIds, functionIds, searchingValue, null, true, null, CoreUtils.getPageable(page, size)).getContent();
        return employeeService.getEmployeeInfoList(employees, false);
    }

    @ApiPageable
    @Operation(summary = "Получение информации о сотрудниках", description = "Получение информации о сотрудниках", tags = {"employee"})
    @PostMapping("/api/employee/find")
    public EmployeeInfoResponse findInfoPost(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) Pageable pageable,
            @Parameter(name = "id", description = "Массив идентификаторов сотрудника (таблица employee).", example = "[1, 2, 3]")
            @RequestParam(name = "id", required = false) List<Long> employeeIds,
            @Parameter(name = "division", description = "Идентификатор подразделения (таблица employee).", example = "112")
            @RequestParam(name = "division", required = false) List<Long> divisionIds,
            @Parameter(name = "function", description = "Идентификатор выполняемой функции (таблица employee).", example = "112")
            @RequestParam(name = "function", required = false) List<Long> functionIds,
            @Parameter(name = "legal_entity_id", description = "Идентификатор юридического лица (таблица legal_entity).", example = "1")
            @RequestParam(name = "legal_entity_id", required = false) Long legalEntityId,
            @Parameter(name = "job_title_id", description = "Идентификатор должности (таблица job_title).", example = "1")
            @RequestParam(name = "job_title_id", required = false) Long jobTitleId,
            @Parameter(name = "position_short_name", description = "Краткое наименование позиции.", example = "Менеджер")
            @RequestParam(name = "position_short_name", required = false) String positionShortName,
            @Parameter(name = "search", description = "Строка поиска по фамилии, имени, отчеству или номеру сотрудника (таблица employee).", example = "Ива Иванович")
            @RequestParam(name = "search", required = false) String searchingValue,
            @Parameter(name = "with_patronymic", description = "Нужно ли выполнять поиск по отчествам сотрудников", example = "true")
            @RequestParam(name = "with_patronymic", required = false, defaultValue = "true") Boolean withPatronymic,
            @Parameter(name = "has_position_assignment", description = "Нужно ли возвращать только сотрудников, имеющих назначение в таблице position_assignment", example = "true")
            @RequestParam(name = "has_position_assignment", required = false, defaultValue = "false") boolean hasPositionAssignment,
            @Parameter(name = "with_closed", description = "Флаг, означающий возвращаем всех сотрудников или только открытые записи", example = "true")
            @RequestParam(name = "with_closed", required = false, defaultValue = "true") boolean withClosed,
            @Parameter(name = "employee_number", description = "Табельный номер сотрудника из поля number таблицы org_employee.", example = "[1111111, 2222222, 3333333]")
            @RequestParam(name = "employee_number", required = false) List<String> employeeNumber,
            @Parameter(name = "emails", description = "Массив email сотрудников (таблица org_employee).", example = "test1@mail.ru,test2@mail.ru,test3@mail.ru")
            @RequestParam(name = "emails", required = false) List<String> emails,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для поиска",
                    content = @Content(schema = @Schema(implementation = EmployeeInfoInputDto.class)))
            @RequestBody(required = false) EmployeeInfoInputDto data) {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("pageable", pageable);
        getParams.put("id", employeeIds);
        getParams.put("division", divisionIds);
        getParams.put("function", functionIds);
        getParams.put("search", searchingValue);
        getParams.put("legal_entity_id", legalEntityId);
        getParams.put("job_title_id", jobTitleId);
        getParams.put("position_short_name", positionShortName);
        getParams.put("with_patronymic", withPatronymic);
        getParams.put("has_position_assignment", hasPositionAssignment);
        getParams.put("with_closed", withClosed);
        getParams.put("employee_number", employeeNumber);
        getParams.put("emails", emails);
        loggerService.createLog(hash, "POST /api/employee/find", getParams, data);

        if (employeeIds == null && data != null) {
            employeeIds = data.getEmployeeIds();
        }
        if (divisionIds == null && data != null) {
            divisionIds = data.getDivisionIds();
        }

        Page<EmployeeEntity> employees = employeeService.findEmployeeNew(employeeIds, divisionIds, functionIds, jobTitleId, positionShortName, legalEntityId, searchingValue, withPatronymic, withClosed, employeeNumber, emails, pageable);
        List<EmployeeInfoDto> employeeInfoDtoList = employeeService.getEmployeeInfoList(employees.getContent(), hasPositionAssignment);

        EmployeeInfoResponse response = new EmployeeInfoResponse();
        response.setPage(employees.getNumber());
        response.setTotalElements(employees.getTotalElements());
        response.setTotalPages(employees.getTotalPages());
        response.setData(employeeInfoDtoList);
        return response;
    }

    @Operation(summary = "Получение информации о назначениях в команду подразделения", description = "Получение информации о назначениях в команду подразделения", tags = {"employee"})
    @GetMapping("/api/employee/teamdivisionassignmentsteam")
    public List<DivisionTeamAssignmentDto> getTeamDivisionTeamAssignments(
            @Parameter(name = "team", description = "Идентификатор команды подразделения (таблица division_team_assignment).", example = "112")
            @RequestParam(name = "team") Long teamDivisionId) {
        return assignmentService.getTeamDivisionTeamAssignments(teamDivisionId);
    }

    @Hidden
    @PostMapping("/api/employee/teamdivisionassignments")
    @SurProtected(operation = SurOperation.EXEC, entityName = "api/employee/post-teamdivisionassignments")
    public List<DivisionTeamAssignmentDto> getDivisionTeamAssignments(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для поиска",
                    content = @Content(schema = @Schema(implementation = IdDto.class)))
            @RequestBody IdDto dto) {
        StopWatch sw = StopWatch.createStarted();
        List<DivisionTeamAssignmentDto> rez = assignmentService.getDivisionTeamAssignmentsFull(dto.getId(), null, null, null, false);
        sw.stop();
        log.info("api/employee/teamdivisionassignments total {}", sw.getTime());
        return rez;
    }

    @Operation(summary = "Получение информации о назначениях в команду подразделения", description = "Получение информации о назначениях в команду подразделения", tags = {"employee"})
    @GetMapping("/api/employee/teamdivisionassignments")
    @SurProtected(operation = SurOperation.EXEC_ATTR)
    public List<DivisionTeamAssignmentDto> getDivisionTeamAssignments(
            @Parameter(name = "id", description = "Массив идентификаторов назначений в команду подразделения (таблица division_team_assignment).", example = "[1, 2, 3]")
            @RequestParam(name = "id", required = false) List<Long> divisionTeamAssignmentIds,
            @Parameter(name = "employee", description = "Идентификатор сотрудника (таблица division_team_assignment).", example = "1")
            @RequestParam(name = "employee", required = false) @SurProtectedAttr(attributeName = "employee_id") List<Long> employeeIds,
            @Parameter(name = "external_employee", description = "Внешний идентификатор сотрудника (таблица employee).", example = "1")
            @RequestParam(name = "external_employee", required = false) String externalId,
            @Parameter(name = "team", description = "Идентификатор команды подразделения (таблица division_team_assignment).", example = "1")
            @RequestParam(name = "team", required = false) @SurProtectedAttr(attributeName = "division_team_id") Long divisionTeamId,
            @Parameter(name = "with_closed", description = "Запрашиваются ли закрытые записи..", example = "true")
            @RequestParam(name = "with_closed", required = false, defaultValue = "false") Boolean withClosed) {
        StopWatch sw = StopWatch.createStarted();
        List<DivisionTeamAssignmentDto> rez = assignmentService.getDivisionTeamAssignmentsFull(divisionTeamAssignmentIds, employeeIds, externalId, divisionTeamId, withClosed);
        sw.stop();
        log.info("api/employee/teamdivisionassignments total {}", sw.getTime());
        return rez;
    }

    @Hidden// создано по мотивам getDivisionTeamAssignments
    @GetMapping("/api/employee/first-team-division-assignment")
    public DivisionTeamAssignmentDto getFirstDivisionTeamAssignment() {
        return assignmentService.getFirstDivisionTeamAssignment();
    }

    @Operation(summary = "Получение информации о назначениях в команду организации", description = "Получение информации о назначениях в команду организации", tags = {"employee"})
    @GetMapping("/api/employee/teamlegalentityassignments")
    @SurProtected(operation = SurOperation.EXEC_ATTR)
    public List<LegalEntityTeamAssignmentDto> getLegalEntityTeamAssignments(
            @Parameter(name = "id", description = "Идентификатор назначения в организацию (таблица legal_entity_team_assignment).", example = "1")
            @RequestParam(name = "id", required = false) Long legalEntityTeamAssignmentId,
            @Parameter(name = "employee", description = "Идентификатор сотрудника (таблица legal_entity_team_assignment).", example = "1")
            @RequestParam(name = "employee", required = false) @SurProtectedAttr(attributeName = "employee_id") Long employeeId,
            @Parameter(name = "external_employee", description = "Внешний идентификатор сотрудника (таблица employee).", example = "1")
            @RequestParam(name = "external_employee", required = false) String externalEmployeeId,
            @Parameter(name = "team", description = "Идентификатор команды в организации (таблица legal_entity_team_assignment).", example = "1")
            @RequestParam(name = "team", required = false)  @SurProtectedAttr(attributeName = "division_team_id") Long legalEntityTeamId) {
        return legalEntityTeamAssignmentService.getLegalEntityTeamAssignments(legalEntityTeamAssignmentId, employeeId, externalEmployeeId, legalEntityTeamId);
    }

	@Operation(summary = "Получение информации о всех родительских назначениях в команду организации", description = "Получение информации о всех родительских назначениях в команду организации", tags = {"employee"})
	@GetMapping("/api/employee/parentsLegalEntityTeamAssignments")
	public List<LegalEntityTeamAssignmentDto> getParentsLegalEntityTeamAssignments(
		@Parameter(name = "employee", description = "Идентификатор сотрудника (таблица legal_entity_team_assignment).", example = "1")
		@RequestParam(name = "employee") Long employeeId,
		@Parameter(name = "task_owner_id", description = "Идентификатор сотрудника (таблица task.user_id).", example = "1")
		@RequestParam(name = "task_owner_id") Long taskOwnerId,
		@Parameter(name = "user_type_id", description = "Тип пользователя (таблица task_type.user_type_id).", example = "1")
		@RequestParam(name = "user_type_id") Long userTypeId) {
		return legalEntityTeamAssignmentService.getParentsLegalEntityTeamAssignments(employeeId, taskOwnerId, userTypeId);
	}

    @Operation(summary = "Получение информации о текущем статусе сотрудника", description = "Получение информации о текущем статусе сотрудника", tags = {"employee"})
    @BadRequestAPIResponses
    @GetMapping("/api/employee/condition")
    public EmployeeConditionInfoDto getCondition(
            @Parameter(name = "id", description = "Идентификатор сотрудника (таблица employee).", example = "1")
            @RequestParam(name = "id", required = false) Long id,
            @Parameter(name = "external_employee", description = "Внешний идентификатор сотрудника (таблица employee).", example = "1")
            @RequestParam(name = "external_employee", required = false) String externalId) {
        Long employeeId = getEmployeeId(id, externalId);
        if (employeeId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "employee id is null, no condition info to return");
        }
        Optional<EmployeeEntity> optional = employeeDao.findById(employeeId);
        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("employee status with id: %d was not found", id));
        }

        return new EmployeeConditionInfoDto(employeeId, optional.get().getDateFrom(), optional.get().getDateTo());
    }

    @Operation(summary = "Получение информации о руководителе команды подразделения", description = "Получение информации о руководителе команды подразделения", tags = {"employee"})
    @BadRequestAPIResponses
    @GetMapping("/api/employee/teamdivisionhead")
    public DivisionTeamAssignmentDto getTeamDivisionHead(
            @Parameter(name = "employee", description = "Идентификатор сотрудника (таблица division_team_assignment).", example = "1")
            @RequestParam(name = "employee", required = false) Long employeeId,
            @Parameter(name = "external_employee", description = "Внешний идентификатор сотрудника (таблица employee).", example = "1")
            @RequestParam(name = "external_employee", required = false) String externalId,
            @Parameter(name = "team", description = "Идентификатор команды подразделения (таблица division_team_assignment).", example = "1")
            @RequestParam(name = "team") Long divisionTeamId,
            @Parameter(name = "head_level", description = "Уровень начальника, которого нужно найти (непосредственный начальник имеет head_level = 1).", example = "1")
            @RequestParam(name = "head_level", required = false) Long headLevel) {
        return assignmentService.getTeamDivisionHead(employeeId, externalId, divisionTeamId, headLevel);
    }

    @Operation(summary = "Получение информации о руководителе руководителя команды подразделения", description = "Получение информации о руководителе руководителя команды подразделения", tags = {"employee"})
    @GetMapping("/api/employee/teamdivisionheadhead")
    public DivisionTeamAssignmentDto getTeamDivisionHeadHead(
            @Parameter(name = "employee", description = "Идентификатор сотрудника (таблица division_team_assignment).", example = "1")
            @RequestParam(name = "employee", required = false) Long employeeId,
            @Parameter(name = "external_employee", description = "Внешний идентификатор сотрудника (таблица employee).", example = "1")
            @RequestParam(name = "external_employee", required = false) String externalId,
            @Parameter(name = "team", description = "Идентификатор команды подразделения (таблица division_team_assignment).", example = "1")
            @RequestParam(name = "team", required = false) Long divisionTeamId) {
        Long id = getEmployeeId(employeeId, externalId);
        EmployeeSearchResult headSearchResult = employeeService.getDivisionTeamHead(id, divisionTeamId);
        if (!headSearchResult.getSearchStatus()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "search result is empty, therefore we could't not return info");
        }
        if (headSearchResult.getEmployee() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "employee head not found, therefore we could't not return info");
        }
        EmployeeSearchResult headHeadSearchResult = employeeService.getDivisionTeamHead(headSearchResult.getEmployee().getId(), headSearchResult.getDivisionTeamId());
        if (headHeadSearchResult.getEmployee() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "head employee head not found, therefore we could't not return info");
        }
        DivisionTeamAssignmentEntity assignment = divisionTeamAssignmentDao.findById(headHeadSearchResult.getDivisionTeamAssignmentId()).get();
        List<DivisionTeamAssignmentRotationShortDto> rotations = divisionTeamAssignmentRotationDao
                .findByAssignmentId(assignment.getId())
                .stream()
                .map(DivisionTeamAssignmentRotationFactory::createShort)
                .collect(Collectors.toList());
        List<PositionAssignmentEntity> positionAssignments = positionAssignmentDao.findAll(
            PositionAssignmentFilter.builder()
                .unitCode(unitAccessService.getCurrentUnit())
                .employeeId(assignment.getEmployeeId())
                .build()
        );
        return DivisionTeamAssignmentFactory.createWithJobInfo(assignment, positionAssignments, rotations);
    }

    @Operation(summary = "Получение информации о подчиненных в команде подразделения", description = "Получение информации о подчиненных в команде подразделения", tags = {"employee"})
    @GetMapping({"/api/employee/teamdivisionsubordinates", "/api/employee/teamdivisionsubordinates/old"})
    @SurProtected(operation = SurOperation.EXEC_ATTR)
    public List<DivisionTeamAssignmentShortDto> getTeamDivisionSubordinatesNew(
            @Parameter(name = "id", description = "Идентификатор сотрудника (таблица employee).", example = "1")
            @RequestParam(name = "id", required = false) @SurProtectedAttr(attributeName = "employee_id") Long employeeId,
            @Parameter(name = "external_employee", description = "Внешний идентификатор сотрудника (таблица employee).", example = "1")
            @RequestParam(name = "external_employee", required = false) String externalId,
            @Parameter(name = "team", description = "Идентификатор команды подразделения (таблица division_team).", example = "1")
            @RequestParam(name = "team", required = false) @SurProtectedAttr(attributeName = "division_team_id") Long divisionTeamId,
            @Parameter(name = "withchilds", description = "Необходимо ли получать подчиненных от подчиненных (таблица employee).", example = "true")
            @RequestParam(name = "withchilds", required = false, defaultValue = "false") Boolean withChilds,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "50") int pageSize) {
        List<DivisionTeamAssignmentShortDto> fullList = assignmentService.getDivisionTeamSubordinates(employeeId, externalId, divisionTeamId, withChilds);
        fullList.sort(Comparator.comparing(DivisionTeamAssignmentShortDto::getFullName));
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, fullList.size());
        if (startIndex > fullList.size()) {
            return List.of();
        }
        return fullList.subList(startIndex, endIndex);
    }

    @Operation(summary = "Получение информации о подчиненных в команде подразделения по ИД команды",
            description = "Получение информации о подчиненных в команде подразделения по ИД команды.\n" +
                    "1) Обязательный параметр team -  нужно искать подчинённых (system_role =2) в запрашиваемом подразделении + руководителя в нижестоящем (system_role = 1). " +
                    "Если руководителя в нижестоящей команде нет, то выдавать всех остальных работников в команде. И опускаться в нижестоящие подразделения (если оно/они есть) и искать там руководителя и выводить его тоже. " +
                    "Цикл заканчивается на найденном руководителе.\n" +
                    "Нижестоящие подразделения ищем по division_team.parent_id.\n" +
                    "2) Параметр withchilds. По умолчанию = false. В таком случае поиск производим как в п.1.\n" +
                    "Если withchilds = true - то искать всевозможные дочерние подразделения и выдавать всех сотрудников по ним.",
            tags = {"employee"})
    @GetMapping("/api/employee/teamdivisionsubordinatesteam")
    public List<DivisionTeamAssignmentShortDto> getTeamDivisionSubordinatesTeam(
            @Parameter(name = "team", description = "Идентификатор команды подразделения (таблица division_team).", example = "1")
            @RequestParam(name = "team") Long divisionTeamId,
            @Parameter(name = "withchilds", description = "Необходимо ли получать подчиненных от подчиненных (таблица employee).", example = "true")
            @RequestParam(name = "withchilds", required = false, defaultValue = "false") Boolean withChilds) {
        return assignmentService.getDivisionTeamSubordinatesTeam(divisionTeamId, withChilds);
    }

    @Operation(summary = "Получение информации о сотрудниках. Метод будет удаляться, используйте POST /api/employee/find", description = "Получение информации о сотрудниках. Метод будет удаляться, используйте POST /api/employee/find", tags = {"employee"})
    @GetMapping("/api/employee/list")
    public List<EmployeeExtendedInfoDto> getEmployeeList(
            @Parameter(name = "employees", description = "Массив идентификаторов сотрудников (таблица employee).", example = "[1, 2, 3]")
            @RequestParam(name = "employees", required = false) Long[] employeeIds) {
        Iterable<EmployeeEntity> employeeEntities;
        if (employeeIds == null || employeeIds.length == 0) {
            employeeEntities = employeeDao.findAll();
        } else {
            employeeEntities = employeeDao.findAllById(Arrays.asList(employeeIds));
        }
        List<EmployeeExtendedInfoDto> employees = new ArrayList<>();
        for (EmployeeEntity entity : employeeEntities) {
            EmployeeInfo info = employeeService.getEmployeeInfo(entity.getId());
            employees.add(EmployeeExtendedInfoFactory.create(entity, info.getAssignments()));
        }
        return employees;
    }

    @Operation(summary = "Получение назначений в команду по идентификатору организации", description = "Получение назначений в команду по идентификатору организации", tags = {"employee"})
    @GetMapping("/api/employee/teamdivisionassignmentslegalentity")
    public List<DivisionTeamAssignmentDto> getTeamDivisionAssignmentsByLegalEntity(
            @Parameter(name = "legal_entity", description = "Идентификаторов организации", example = "1")
            @RequestParam(name = "legal_entity") List<Long> legalEntityIds,
            @Parameter(name = "is_head", description = "Флаг, означающий будет возвращать метод только назначения руководителей или все назначения", example = "false")
            @RequestParam(name = "is_head", required = false, defaultValue = "false") boolean isHead,
            @Parameter(name = "with_assignments", description = "Отключение поля position_assignments", example = "true")
            @RequestParam(name = "with_assignments", required = false, defaultValue = "true") boolean withAssignments,
            @Parameter(name = "with_rotation", description = "Отлючение поля division_team_assignment_rotations", example = "true")
            @RequestParam(name = "with_rotation", required = false, defaultValue = "true") boolean withRotation,
            @Parameter(name = "with_employee", description = "Отлючение поля division_team_assignment_employee", example = "true")
            @RequestParam(name = "with_employee", required = false, defaultValue = "true") boolean withEmployee,
            @Parameter(name = "with_dtr", description = "Отлючение поля division_team_role", example = "true")
            @RequestParam(name = "with_dtr", required = false, defaultValue = "true") boolean withDtr) {
        return assignmentService.getTeamDivisionAssignmentsByLegalEntity(legalEntityIds, isHead, withAssignments, withRotation, withEmployee, withDtr);
    }

    @Operation(summary = "Получение информации о назначениях вызывающего сотрудника и его преемниках", description = "Получение информации о назначениях сотрудника и его преемниках", tags = {"employee"})
    @BadRequestAPIResponses
    @GetMapping("/api/employee/teamdivisionroles")
    public DivisionTeamRoleContainerDto getTeamDivisionRoles(
            @Parameter(name = "id", description = "Идентификаторов роли в команде", example = "7")
            @RequestParam(name = "id", required = false) Long divisionTeamRoleId,
            @Parameter(name = "team", description = "Идентификатор команды", example = "7")
            @RequestParam(name = "team") Long divisionTeamId,
            @Parameter(name = "employee", description = "Идентификатор сотрудника", example = "25")
            @RequestParam(name = "employee", required = false) Long employeeId) throws NotFoundException {
        Long id = getEmployeeId(employeeId, null);
        boolean isPassedHrOrHeadTeamCheckingResult;
        if (employeeId != null) {
            long userEmployeeId = authService.getUserEmployeeId();
            isPassedHrOrHeadTeamCheckingResult = isPassedHrOrHeadTeamChecking(userEmployeeId, divisionTeamId);
        } else {
            isPassedHrOrHeadTeamCheckingResult = isPassedHrOrHeadTeamChecking(id, divisionTeamId);
        }

        if (isPassedHrOrHeadTeamCheckingResult) {
            DivisionTeamRoleEntity divisionTeamRole;
            if (divisionTeamRoleId == null) {
                divisionTeamRole = divisionTeamRoleDao.findByEmployeeIdAndDivisionTeamId(id, divisionTeamId);
                if (divisionTeamRole == null) {
                    throw new NotFoundException(String.format("division team role for employee %d and division team %d is not found", id, divisionTeamId));
                }
            } else {
                divisionTeamRole = divisionTeamRoleDao.findById(divisionTeamRoleId)
                        .orElseThrow(() -> new NotFoundException(String.format("division team role %d is not found", divisionTeamRoleId)));
            }
            return divisionService.createRoleContainer(DivisionTeamRoleFactory.create(divisionTeamRole), false);
        } else {
            throw new NotFoundException(String.format("employee %d is not head of team all parent levels or is not HR", id));
        }
    }

    @Operation(summary = "Получение информации о всех ролях в команде", description = "Получение информации о всех ролях в команде", tags = {"employee"})
    @BadRequestAPIResponses
    @GetMapping("/api/employee/teamdivisionrolesteam")
    public List<DivisionTeamRoleContainerDto> getTeamDivisionRolesTeam(
            @Parameter(name = "team", description = "Идентификатор команды", example = "7")
            @RequestParam(name = "team") Long divisionTeamId) throws NotFoundException {
        Long employeeId = authService.getUserEmployeeId();

        if (isPassedHrOrHeadTeamChecking(employeeId, divisionTeamId)) {
            List<DivisionTeamRoleContainerDto> roleContainers = new ArrayList<>();
            List<DivisionTeamRoleDto> divisionTeamRoles = divisionTeamRoleDao
                    .findAllByDivisionTeam(divisionTeamId)
                    .stream()
                    .map(DivisionTeamRoleFactory::create)
                    .collect(Collectors.toList());

            divisionTeamRoles.forEach(divisionTeamRole -> roleContainers.add(divisionService.createRoleContainer(divisionTeamRole, false)));
            return roleContainers;
        } else {
            throw new NotFoundException(String.format("employee %d is not head of team all parent levels or is not HR", employeeId));
        }
    }

    @Operation(summary = "Добавление преемника", description = "Добавление преемника", tags = {"employee"})
    @PostMapping("/api/employee/addteamdivisionsuccessor")
    @SurProtected(operation = SurOperation.EXEC)
    public DivisionTeamSuccessorDto createDivisionTeamSuccessorEntity(
            @Parameter(name = "employee_id", description = "Идентификатор сотрудника (таблица employee).", example = "1")
            @RequestParam(name = "employee_id") Long employeeId,
            @Parameter(name = "division_team_role_id", description = "Роль для преемника (таблица division_team_role).", example = "1")
            @RequestParam(name = "division_team_role_id") Long divisionTeamRoleId,
            @Parameter(name = "date_priority", description = "Приоритет даты", example = "2022-03-20_13:00:00")
            @RequestParam(name = "date_priority", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm:ss") Date datePriority,
            @Parameter(name = "reason_id_inclusion", description = "Идентификатор причины включения (таблица reason)", example = "1")
            @RequestParam(name = "reason_id_inclusion", required = false) Integer reasonIdInclusion,
            @Parameter(name = "comment_inclusion", description = "Комментарий включения", example = "комментарий")
            @RequestParam(name = "comment_inclusion", required = false) String commentInclusion,
            @Parameter(name = "document_url_inclusion", description = "URL документа включения", example = "http://url")
            @RequestParam(name = "document_url_inclusion", required = false) String documentUrlInclusion) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("employee_id", employeeId);
        getParams.put("division_team_role_id", divisionTeamRoleId);
        getParams.put("date_priority", datePriority);
        getParams.put("reason_id_inclusion", reasonIdInclusion);
        getParams.put("comment_inclusion", commentInclusion);
        getParams.put("document_url_inclusion", documentUrlInclusion);
        loggerService.createLog(hash, "POST /api/employee/addteamdivisionsuccessor", getParams, null);

        return divisionService.createDivisionTeamSuccessor(
                divisionTeamRoleId, employeeId, authService.getUserEmployeeId(),
                datePriority, reasonIdInclusion, commentInclusion, documentUrlInclusion
        );
    }

    @Operation(summary = "Удаление преемника", description = "Удаление преемника", tags = {"employee"})
    @PostMapping("/api/employee/deleteteamdivisionsuccessor")
    @SurProtected(operation = SurOperation.EXEC)
    public OperationResult deleteDivisionTeamSuccessorEntity(
            @Parameter(name = "reason_id_exclusion", description = "Идентификатор причины исключения (таблица reason)", example = "1")
            @RequestParam(name = "reason_id_exclusion", required = false) Integer reasonIdExclusion,
            @Parameter(name = "comment_exclusion", description = "Комментарий исключения", example = "комментарий")
            @RequestParam(name = "comment_exclusion", required = false) String commentExclusion,
            @Parameter(name = "document_url_exclusion", description = "URL документа исключения", example = "http://url")
            @RequestParam(name = "document_url_exclusion", required = false) String documentUrlExclusion,
            @Parameter(name = "division_team_successor_id", description = "Идентификатор сущности-преемника (таблица division_team_successor).", example = "1")
            @RequestParam(name = "division_team_successor_id", required = false) Long divisionTeamSuccessorId) throws NotFoundException, AccessDeniedException {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put(DtoTagConstants.DIVISION_TEAM_SUCCESSOR_ID_TAG, divisionTeamSuccessorId);
        getParams.put("reason_id_exclusion", reasonIdExclusion);
        getParams.put("comment_exclusion", commentExclusion);
        getParams.put("document_url_exclusion", documentUrlExclusion);
        loggerService.createLog(hash, "POST /api/employee/deleteteamdivisionsuccessor", getParams, null);

        try {
            long employeeId = authService.getUserEmployeeId();
            Boolean deleted = divisionService.deleteDivisionTeamSuccessor(
                divisionTeamSuccessorId, employeeId, reasonIdExclusion, commentExclusion, documentUrlExclusion
            );
            return new OperationResult(deleted, "");
        } catch (NotFoundException | AccessDeniedException ex) {
            return new OperationResult(false, ex.getMessage());
        }
    }

    @Operation(summary = "Метод создания сущности DivisionTeamSuccessorReadiness", description = "Метод создания сущности DivisionTeamSuccessorReadiness", tags = {"employee"})
    @PostMapping("/api/employee/setteamdivisionsuccessorreadiness")
    @SurProtected(operation = SurOperation.EXEC)
    public DivisionTeamSuccessorReadinessDto createDivisionTeamSuccessorReadinessEntity(
            @Parameter(name = "division_team_successor_id", description = "Идентификатор сущности-преемника (таблица division_team_successor).", example = "1")
            @RequestParam(name = "division_team_successor_id", required = false) Long divisionTeamSuccessorId,
            @Parameter(name = "assignment_readiness_id", description = "Идентификатор готовности из справочника (таблица assignment_readiness).", example = "1")
            @RequestParam(name = "assignment_readiness_id", required = false) Integer assignmentReadinessId) throws NotFoundException, AccessDeniedException {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put(DtoTagConstants.DIVISION_TEAM_SUCCESSOR_ID_TAG, divisionTeamSuccessorId);
        getParams.put("assignment_readiness_id", assignmentReadinessId);
        loggerService.createLog(hash, "POST /api/employee/setteamdivisionsuccessorreadiness", getParams, null);

        long employeeId = authService.getUserEmployeeId();

        return divisionService.createDivisionTeamSuccessorReadiness(divisionTeamSuccessorId, assignmentReadinessId, employeeId);
    }

    @Operation(summary = "Удаление преемников", description = "Удаление преемников, которые находятся на ролях, для которых они являются преемниками", tags = {"employee"})
    @BadRequestAPIResponses
    @PostMapping("/api/employee/verifyteamdivisionsuccessors")
    @SurProtected(operation = SurOperation.EXEC)
    public OperationResult deletedDivisionTeamSuccessors() throws NotFoundException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/employee/verifyteamdivisionsuccessors", null, null);
        divisionService.deletedDivisionTeamSuccessors();
        return new OperationResult(true, "");
    }

    @Operation(summary = "Подтверждение готовности к ротации", description = "Подтверждение готовности к ротации (установка даты подтверждения от HR)", tags = {"employee"})
    @BadRequestAPIResponses
    @PostMapping("/api/employee/teamdivisionassignmentrotationcommit")
    @SurProtected(operation = SurOperation.EXEC)
    public OperationResult commitTeamDivisionAssignmentRotation(
            @Parameter(name = "id", description = "Идентификатор готовности к ротации назначения в команде подразделения (таблица division_team_assignment_rotation)", example = "3")
            @RequestParam(name = "id") Long divisionTeamAssignmentRotationId) throws AccessDeniedException, NotFoundException {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("id", divisionTeamAssignmentRotationId);
        loggerService.createLog(hash, "POST /api/employee/teamdivisionassignmentrotationcommit", getParams, null);

        long employeeId = authService.getUserEmployeeId();
        divisionService.commitTeamDivisionAssignmentRotation(divisionTeamAssignmentRotationId, employeeId);
        return new OperationResult(true, "");
    }

    @Operation(summary = "Метод добавления готовности к ротации.", description = "Метод добавления готовности к ротации.", tags = {"employee"})
    @PostMapping("/api/employee/addteamdivisionassignmentrotation")
    @SurProtected(operation = SurOperation.EXEC)
    public DivisionTeamAssignmentRotationDto createDivisionTeamAssignmentRotationEntity(
            @Parameter(name = "division_team_assignment_id", description = "Идентификатор назначения (таблица division_team_assignment).", example = "1")
            @RequestParam(name = "division_team_assignment_id", required = false) Long divisionTeamAssignmentId,
            @Parameter(name = "assignment_rotation_id", description = "Идентификатор готовности из справочника (таблица assignment_rotation).", example = "1")
            @RequestParam(name = "assignment_rotation_id", required = false) Integer assignmentRotationId) throws NotFoundException, AccessDeniedException {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("division_team_assignment_id", divisionTeamAssignmentId);
        getParams.put("assignment_rotation_id", assignmentRotationId);
        loggerService.createLog(hash, "POST /api/employee/addteamdivisionassignmentrotation", getParams, null);

        long employeeId = authService.getUserEmployeeId();
        return divisionService.createDivisionTeamAssignmentRotation(divisionTeamAssignmentId, assignmentRotationId, employeeId, authService.isTechUser());
    }

    @Operation(summary = "Проверка на руководителя по идентификатору назначения", description = "Проверка на руководителя по идентификатору назначения. Если идентификатор назначения null, то будет проверяться назначение пользователя из сессии.", tags = {"employee"})
    @GetMapping("/api/employee/checkemployeeheadteam")
    public Boolean checkEmployeeHeadTeamByAssignment(
            @Parameter(name = "division_team_assignment_id", description = "Идентификатор назначения (таблица division_team_assignment).", example = "1")
            @RequestParam(name = "division_team_assignment_id", required = false) Long divisionTeamAssignmentId) {
        return divisionService.checkEmployeeHeadTeamByAssignment(divisionTeamAssignmentId);
    }

    @Operation(summary = "Обнуление согласования", description = "Обнуление согласования", tags = {"employee"})
    @PostMapping("/api/employee/teamdivisionassignmentrotationwithdraw")
    @SurProtected(operation = SurOperation.EXEC)
    public OperationResult teamDivisionAssignmentRotationWithDraw(
            @Parameter(name = "id", description = "Идентификатор готовности к ротации назначения в команде подразделения (таблица division_team_assignment_rotation)", example = "3")
            @RequestParam(name = "id") Long divisionTeamAssignmentRotationId) throws AccessDeniedException, NotFoundException {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("id", divisionTeamAssignmentRotationId);
        loggerService.createLog(hash, "POST /api/employee/teamdivisionassignmentrotationwithdraw", getParams, null);

        long employeeId = authService.getUserEmployeeId();
        divisionService.teamDivisionAssignmentRotationWithDraw(divisionTeamAssignmentRotationId, employeeId);
        return new OperationResult(true, "");
    }

    @Operation(summary = "Получение статистики по подразделениям и видам ротации", description = "Получение статистики по подразделениям и видам ротации", tags = {"employee"})
    @GetMapping("/api/employee/teamassignmentrotationstat")
    public List<DivisionWithDivisionTeamsStatDto> teamAssignmentRotationStat(
            @Parameter(name = "legal_entity", description = "Идентификатор организации (таблица legal_entity)", example = "1")
            @RequestParam(name = "legal_entity") Long legalEntityId) {
        return divisionService.teamAssignmentRotationStatDto(legalEntityId);
    }

    @Operation(summary = "Получение статистики по подразделениям и преемникам", description = "Получение статистики по подразделениям и преемникам", tags = {"employee"})
    @GetMapping("/api/employee/teamdivisionsuccessorstat")
    public List<DivisionWithDivisionTeamsWithDivisionTeamRolesAndDivisionTeamSuccessorsDto> divisionTeamRolesAndSuccessorsStat(
            @Parameter(name = "legal_entity", description = "Идентификатор организации (таблица legal_entity)", example = "1")
            @RequestParam(name = "legal_entity") Long legalEntityId) {
        return divisionService.getDivisionTeamSuccessorStat(legalEntityId);
    }

    @Operation(summary = "Добавление или обнуление записи подтверждения преемника hr-ом", description = "Добавление или обнуление записи подтверждения преемника hr-ом", tags = {"employee"})
    @BadRequestAPIResponses
    @PutMapping("/api/employee/divisionteamsuccessorupdatehr")
    public OperationResult divisionTeamSuccessorUpdateHr(
            @Parameter(name = "division_team_successor_id", description = "Идентификатор преемника (таблица division_team_successor)", example = "12")
            @RequestParam(name = "division_team_successor_id") Long divisionTeamSuccessorId,
            @Parameter(name = "date_commit_hr", description = "Дата подтверждения", example = "2021-07-10_12:00:00")
            @RequestParam(name = "date_commit_hr", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm:ss") Date dateCommitHr) {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put(DtoTagConstants.DIVISION_TEAM_SUCCESSOR_ID_TAG, divisionTeamSuccessorId);
        getParams.put("date_commit_hr", dateCommitHr);
        loggerService.createLog(hash, "PUT /api/employee/divisionteamsuccessorupdatehr", getParams, null);
        try {
            divisionService.divisionTeamSuccessorUpdateHr(divisionTeamSuccessorId, dateCommitHr);
            return new OperationResult(true, "");
        } catch (Exception ex) {
            return new OperationResult(false, ex.getMessage());
        }
    }

    @Operation(summary = "Получение информации о сотруднике по назначению", description = "Получение информации о сотруднике по назначению", tags = {"employee"})
    @BadRequestAPIResponses
    @GetMapping("/api/employee/employeeinfobyassignment")
    public EmployeeInfoDto getEmployeeInfoByAssignment(
            @Parameter(name = "division_team_assignment_id", description = "Идентификатор назначения (таблица division_team_assignment).", example = "1")
            @RequestParam(name = "division_team_assignment_id", required = false) Long divisionTeamAssignmentId) throws NotFoundException {
        Optional<DivisionTeamAssignmentEntity> entityOptional = divisionTeamAssignmentDao.findById(divisionTeamAssignmentId);
        if (entityOptional.isEmpty()) {
            log.error("division team assignment not found, assignment id={}", divisionTeamAssignmentId);
            throw new NotFoundException(String.format("division team assignment not found, assignment id=%s", divisionTeamAssignmentId));
        }
        EmployeeEntity employee = entityOptional.get().getEmployee();
        List<PositionAssignmentEntity> positionAssignments = positionAssignmentDao.findAll(
            PositionAssignmentFilter.builder()
                .unitCode(unitAccessService.getCurrentUnit())
                .employeeId(employee.getId())
                .build()
        );
        return EmployeeInfoFactory.createWithJobInfo(employee, positionAssignments);
    }

    @Operation(summary = "Обновление комментариев", description = "Обновление комментариев", tags = {"employee"})
    @BadRequestAPIResponses
    @PutMapping("/api/employee/teamdivisionassignmentrotationcomment")
    public OperationResult updateCommentForDivisionTeamAssignmentRotation(
            @Parameter(name = "id", description = "Идентификатор готовности к ротации назначения в команде подразделения (таблица division_team_assignment_rotation)", example = "3")
            @RequestParam(name = "id") Long divisionTeamAssignmentRotationId,
            @Parameter(name = "comment_hr", description = "Комментрий HR", example = "Комментрий")
            @RequestParam(name = "comment_hr", required = false) String commentHr,
            @Parameter(name = "comment_employee", description = "Комментрий Employee", example = "Комментрий")
            @RequestParam(name = "comment_employee", required = false) String commentEmployee) throws NotFoundException {
        UUID hash = UUID.randomUUID();
        Map<String, Object> getParams = new HashMap<>();
        getParams.put("id", divisionTeamAssignmentRotationId);
        getParams.put("comment_hr", commentHr);
        getParams.put("comment_employee", commentEmployee);
        loggerService.createLog(hash, "PUT /api/employee/teamdivisionassignmentrotationcomment", getParams, null);
        divisionTeamAssignmentRotationService.updateDivisionTeamAssignmentRotationComment(divisionTeamAssignmentRotationId, commentHr, commentEmployee);
        return new OperationResult(true, "");
    }

    @Operation(summary = "Получение идентификатора сотрудника из сессии", description = "Получение идентификатора сотрудника из сессии", tags = {"employee"})
    @GetMapping("/api/employee/me")
    public Long getEmployeeId() {
        try {
            return authService.getUserEmployeeId();
        } catch (Exception ignored) {
            return null;
        }
    }

    @Operation(summary = "Получение всех идентификаторов назначений в команду", description = "Получение всех идентификаторов назначений в команду по переданному массиву идентификаторов организаций, кроме переданного назначения. " +
            "Если массив идентификаторов организаций пустой или равен null, то возвращаются назначения по организации, к которой относится пользователь из сессии", tags = {"employee"})
    @GetMapping("/api/employee/divisionteamassignmentbylegalentity")
    public List<Long> getDivisionTeamAssignmentIdsInLegalEntityIdsExceptAssignmentId(
            @Parameter(name = "division_team_assignment_id", description = "Идентификатор назначения в команду (таблица division_team_assignment)", example = "3")
            @RequestParam(name = "division_team_assignment_id") Long assignmentId,
            @Parameter(name = "legal_entity_id", description = "Массив идентификаторов организаций (таблица legal_entity)", example = "[1, 2, 3]")
            @RequestParam(name = "legal_entity_id", required = false) List<Long> legalEntityIds) {
        long employeeId = authService.getUserEmployeeId();
        return divisionService.findAllInLegalEntityIdsExceptAssignmentId(assignmentId, legalEntityIds, employeeId);
    }

    @Operation(summary = "Загрузка пользовательской формы excel файла для проверки табельных номеров сотрудников ", description = "Загрузка пользовательской формы excel файла для проверки табельных номеров сотрудников", tags = {"kpigoal"})
    @PostMapping("/api/kpigoal/check-numbers")
    public ResponseNumberDto checkAllNumbers(
            @RequestParam(name = "file") MultipartFile file) throws IOException, NotFoundException {
        loggerService.createLog(UUID.randomUUID(), "POST /api/employee/check-numbers", null, null);
        return employeeService.checkNumbers(file);
    }

    @Operation(summary = "Загрузка пользовательской формы excel файла для проверки табельных номеров сотрудников и получения в ответе excel файла с неподтвержденными номерами.", description = "Загрузка пользовательской формы excel файла для проверки табельных номеров сотрудников и получения в ответе excel файла с неподтвержденными номерами.", tags = {"kpigoal"})
    @PostMapping("/api/kpigoal/check-numbers-excel")
    public ResponseEntity<byte[]> checkAllNumbersExcel(
            @RequestParam(name = "file") MultipartFile file) throws IOException, NotFoundException {
        loggerService.createLog(UUID.randomUUID(), "POST /api/employee/check-numbers", null, null);
        ByteArrayOutputStream outputStream = employeeService.checkNumbersExcel(file);

        HttpHeaders headers = new HttpHeaders();
        String fileName = "unconfirmed.xlsx";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", fileName));
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(outputStream.toByteArray().length));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(outputStream.toByteArray().length)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(outputStream.toByteArray());
    }

    @Operation(summary = "Получение информации о подчиненных в команде подразделения по Id работника",
        description = "Получение информации о подчиненных в команде подразделения по ИД работника.(id работника берётся из кейклок)\n" +
            "1) Обязательный параметр team -  нужно искать подчинённых (system_role =2) в запрашиваемом подразделении + руководителя в нижестоящем (system_role = 1). " +
            "Если руководителя в нижестоящей команде нет, то выдавать всех остальных работников в команде. И опускаться в нижестоящие подразделения (если оно/они есть) и искать там руководителя и выводить его тоже. " +
            "Цикл заканчивается на найденном руководителе.\n" +
            "Нижестоящие подразделения ищем по division_team.parent_id.\n" +
            "2) Параметр withchilds. По умолчанию = false. В таком случае поиск производим как в п.1.\n" +
            "Если withchilds = true - то искать всевозможные дочерние подразделения и выдавать всех сотрудников по ним.",
        tags = {"employee"})
    @GetMapping("/api/bff/employeesubordinates")
    public List<EmployeeInfoShortDto> getTeamDivisionSubordinatesTeamByEmployee(
        @Parameter(name = "withchilds", description = "Необходимо ли получать подчиненных от подчиненных (таблица employee).", example = "true")
        @RequestParam(name = "withchilds", required = false, defaultValue = "false") Boolean withChilds) {
        return assignmentService.getDivisionTeamSubordinatesEmployeeFull(authService.getUserEmployeeId(), withChilds);
    }

    @GeneralAPIResponses
    @Operation(summary = "Список всех подчиненных текущего пользователя в рамках переданного процесса по статусам")
    @SurProtected(operation = SurOperation.UNIT)
    @PostMapping("/api/bff/employees-statuses")
    public PageResponse<EmployeesByStatusDto> getProcessSubordinatesByStatuses(@Valid @RequestBody EmployeeByStatusRequest request) {
        loggerService.createLog(UUID.randomUUID(), "/api/bff/employees-statuses", null, request);
        return employeeService.getEmployeesByStatuses(request, true);
    }

    @GeneralAPIResponses
    @Operation(summary = "Список всех сотрудников процесса по статусам")
    @SurProtected(operation = SurOperation.UNIT)
    @PostMapping("/api/bff/employees-statuses-hr")
    public PageResponse<EmployeesByStatusDto> getProcessMembersByStatuses(@Valid @RequestBody EmployeeByStatusRequest request) {
        loggerService.createLog(UUID.randomUUID(), "/api/bff/employees-statuses-hr", null, request);
        return employeeService.getEmployeesByStatuses(request, false);
    }

    @Operation(
        summary = "Получение сотрудников, подчиненных текущему пользователю",
        description = "По полученному из токена авторизации идентификатору пользователя и идентификатору цикла возвращает " +
            "информацию о сотрудниках, подчиненных пользователю и относящихся к циклу с переданным идентификатором")
    @GeneralAPIResponses
    @PostMapping("/api/bff/employee")
    public List<EmployeeDto> getCurrentUserSubordinates(@Valid @RequestBody CycleSubordinatesRequest request) {
        loggerService.createLog(UUID.randomUUID(), "GET /api/bff/employee", null, request);
        Long employeeId = authService.getUserEmployeeId();
        return employeeService.getCycleSubordinates(employeeId, request.getCycleId());
    }

    @GeneralAPIResponses
    @Operation(summary = "Работники выбранного процесса со статусами")
    @SurProtected(operation = SurOperation.UNIT)
    @PostMapping("/api/bff/employee-process-statuses")
    public FilterAwarePageResponse<ProcessEmployeeDto> findProcessEmployees(@Valid @RequestBody ProcessEmployeeRequest request) {
        loggerService.createLog(UUID.randomUUID(), "/api/bff/employee-process-statuses", null, request);
        return employeeService.findProcessEmployees(request);
    }

    @Hidden
    @GetMapping("/api/employee/check-substitute")
    public Boolean checkSubstitute(
            @RequestParam(name = "division_team") Long divisionTeamId,
            @RequestParam(name = "session_employee") Long sessionEmployee) {
        return assignmentService.checkSubstitute(divisionTeamId, sessionEmployee);
    }

    private Long getEmployeeId(Long id, String externalId) {
        if (id != null) {
            return id;
        }
        if (externalId != null) {
            return employeeService.getEmployeeIdByExternalId(externalId);
        }
        return authService.getUserEmployeeId();
    }

    private boolean isPassedHrOrHeadTeamChecking(Long employeeId, Long divisionTeamId) throws NotFoundException {
        DivisionEntity division = divisionDao.find(
            DivisionFilter.builder()
                .divisionTeamId(divisionTeamId)
                .build()
        );
        unitAccessService.checkUnitAccess(division.getLegalEntityEntity().getUnitCode());
        boolean isPassedHeadTeamChecking = divisionService.checkEmployeeHeadTeamDivision(employeeId, divisionTeamId);
        boolean isPassedHrChecking = divisionService.checkEmployeeHr(employeeId, division.getId());
        return isPassedHeadTeamChecking || isPassedHrChecking;
    }
}
