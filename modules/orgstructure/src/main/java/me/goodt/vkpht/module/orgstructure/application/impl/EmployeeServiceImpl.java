package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.goodt.drive.auth.sur.unit.UnitAccessService;
import com.goodt.drive.rtcore.constants.ComponentCode;
import com.goodt.drive.rtcore.dto.rostalent.ResponseNumberDto;
import com.goodt.drive.rtcore.dto.tasksetting2.EmployeesByStatusDto;
import com.goodt.drive.rtcore.dto.tasksetting2.FilterAwarePageResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.PageResponse;
import com.goodt.drive.rtcore.dto.tasksetting2.ProcessEmployeeDto;
import com.goodt.drive.rtcore.dto.tasksetting2.ProcessEventStageDto;
import com.goodt.drive.rtcore.dto.tasksetting2.StageOutputDto;
import com.goodt.drive.rtcore.dto.tasksetting2.StatusDto;
import com.goodt.drive.rtcore.dto.tasksetting2.TaskFindDto;
import com.goodt.drive.rtcore.dto.tasksetting2.filter.FilterDto;
import com.goodt.drive.rtcore.dto.tasksetting2.filter.FilterDtoFactory;
import com.goodt.drive.rtcore.dto.tasksetting2.filter.FilterOption;
import com.goodt.drive.rtcore.dto.tasksetting2.goalsetting.EmployeeDto;
import com.goodt.drive.rtcore.dto.tasksetting2.goalsetting.input.EmployeeByStatusRequest;
import com.goodt.drive.rtcore.dto.tasksetting2.goalsetting.input.ProcessEmployeeRequest;
import com.goodt.drive.rtcore.service.tasksetting2.CycleService;
import com.goodt.drive.rtcore.service.tasksetting2.StatusService;
import com.goodt.drive.rtcore.service.tasksetting2.goalsetting.IndicatorGoalService;
import com.goodt.drive.rtcore.service.tasksetting2.mapper.EmployeeMapper;
import com.goodt.drive.rtcore.service.tasksetting2.task.IProcessService;
import com.goodt.drive.rtcore.service.tasksetting2.task.ProcessEventService;
import me.goodt.vkpht.common.api.AuthService;
import me.goodt.vkpht.common.api.exception.ForbiddenException;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.common.application.util.GlobalDefs;
import me.goodt.vkpht.common.application.util.PersonUtil;
import me.goodt.vkpht.common.application.util.UtilClass;
import me.goodt.vkpht.common.domain.dao.filter.PositionAssignmentFilter;
import me.goodt.vkpht.common.domain.dao.tasksetting2.ProcessTaskDao;
import me.goodt.vkpht.common.domain.entity.orgstructure.specification.EmployeeSpecification;
import me.goodt.vkpht.common.domain.entity.orgstructure.specification.SearchCriteria;
import me.goodt.vkpht.common.domain.entity.orgstructure.specification.SearchOperation;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.ComponentEntity;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.CycleEntity;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.CycleTaskEntity;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.ProcessEntity;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.ProcessTaskEntity;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.StatusEntity;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.TaskEntity;
import me.goodt.vkpht.common.domain.entity.tasksetting2.entities.TaskTypeEntity;
import me.goodt.vkpht.module.orgstructure.api.AssignmentService;
import me.goodt.vkpht.module.orgstructure.api.EmployeeService;
import me.goodt.vkpht.module.orgstructure.api.PositionService;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeExtendedInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeFlatInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeSearchResult;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionAssignmentInfo;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.EmployeeDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.PositionDao;
import me.goodt.vkpht.module.orgstructure.domain.dao.RoleDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PersonEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.PositionEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.RoleEntity;
import me.goodt.vkpht.module.orgstructure.domain.factory.EmployeeExtendedInfoFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.EmployeeInfoFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PersonFactory;
import me.goodt.vkpht.module.orgstructure.domain.factory.PositionAssignmentFactory;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private PositionAssignmentDao positionAssignmentDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private DivisionTeamDao divisionTeamDao;
    @Autowired
    private DivisionTeamAssignmentDao divisionTeamAssignmentDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private AuthService authService;
    @Autowired
    private IndicatorGoalService indicatorGoalService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private ProcessEventService processEventService;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private PositionDao positionDao;
    @Autowired
    private PositionService positionService;
    @Autowired
    private IProcessService processService;
    @Autowired
    private ProcessTaskDao processTaskDao;
    @Autowired
    private UnitAccessService unitAccessService;
    @Autowired
    private CycleService cycleService;

    @Override
    public EmployeeSearchResult getDivisionTeamHeadByDivisionTeam(Long divisionTeamId, Long headLevel) {
        DivisionTeamEntity divisionTeam = divisionTeamDao.findById(divisionTeamId)
                .orElseThrow(() -> new NotFoundException(String.format("Division team %d is not found", divisionTeamId)));

        List<DivisionTeamAssignmentEntity> list = divisionTeamAssignmentDao
                .fetchByDivisionTeamIdAndSystemRoleId(divisionTeam.getId(), GlobalDefs.HEAD_SYSTEM_ROLE_ID);

        if (!list.isEmpty()) {
            headLevel -= 1;
        }

        while ((list.isEmpty() || headLevel > 0) && divisionTeam.getParent() != null) {
            Long divisionTeamParentId = divisionTeam.getParent().getId();
            divisionTeam = divisionTeamDao.findById(divisionTeamParentId)
                    .orElseThrow(() -> new NotFoundException(String.format("Division team %d is not found", divisionTeamParentId)));
            list = divisionTeamAssignmentDao
                    .fetchByDivisionTeamIdAndSystemRoleId(divisionTeam.getId(), GlobalDefs.HEAD_SYSTEM_ROLE_ID);
            if (!list.isEmpty()) {
                headLevel -= 1;
            }
        }

        if (headLevel > 0) {
            throw new NotFoundException(String.format("Head with this head_level not found in division team: %d", divisionTeamId));
        }
        DivisionTeamAssignmentEntity dta = list.stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Head not found, division team: %d", divisionTeamId)));

        return new EmployeeSearchResult(true, dta.getEmployee(), dta.getDivisionTeamRole().getDivisionTeam().getId(), dta.getId());
    }

    @Override
    public EmployeeSearchResult getDivisionTeamHeadDivisionTeamAndEmployee(Long divisionTeamId, Long employeeId, Long headLevel) {
        boolean existsById = divisionTeamDao.existsById(divisionTeamId);
        if (!existsById) {
            throw new NotFoundException(String.format("Division team %d is not found", divisionTeamId));
        }

        DivisionTeamAssignmentEntity entity = divisionTeamAssignmentDao.findByEmployeeIdAnDivisionTeamId(employeeId, divisionTeamId);
        if (entity == null) {
            throw new ForbiddenException(String.format("The employee %d has no division in team %d.", employeeId, divisionTeamId));
        }
        boolean isEmployeeHasHeadSystemRole;
        try {
            DivisionTeamAssignmentEntity divisionTeamAssignmentEntity = assignmentService.getOneTeamAssignment(employeeId, divisionTeamId);
            isEmployeeHasHeadSystemRole = divisionTeamAssignmentEntity.getDivisionTeamRole().getRole().getSystemRole().getId().equals(GlobalDefs.HEAD_SYSTEM_ROLE_ID);
        } catch (NotFoundException e) {
            isEmployeeHasHeadSystemRole = false;
        }
        if (isEmployeeHasHeadSystemRole) {
            headLevel += 1;
        }
        return getDivisionTeamHeadByDivisionTeam(divisionTeamId, headLevel);
    }

    @Override
    public EmployeeSearchResult getDivisionTeamHeadByEmployee(Long employeeId, Long headLevel) {
        Set<Long> divisionTeamIds = new HashSet<>(divisionTeamDao.findAllByEmployee(employeeId));
        boolean exist = roleDao.existForEmployee(employeeId, GlobalDefs.HEAD_SYSTEM_ROLE_ID);
        if (exist) {
            headLevel += 1;
        }

        Long divisionTeamId = divisionTeamIds.stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException(String.format("Division team assignment for employee %d is not found", employeeId)));
        return getDivisionTeamHeadByDivisionTeam(divisionTeamId, headLevel);
    }

    @Override
    public EmployeeSearchResult getDivisionTeamHead(Long employeeId, Long divisionTeamId) {
        try {
            // 1. Get Assignment by employee and division team
            DivisionTeamAssignmentEntity employeeAssignment = assignmentService.getOneTeamAssignment(employeeId, divisionTeamId);
            if (employeeAssignment == null) {
                return new EmployeeSearchResult(true, null, -1L, -1L);
            }
            return getDivisionTeamHead(employeeAssignment);
        } catch (Exception e) {
            log.error("An error occurred during getting Division Head, reason: {}", e.getMessage());
            return new EmployeeSearchResult(false, null, -1L, -1L);
        }
    }

    @Override
    public PositionAssignmentInfo getPositionAssignmentInfo(Long employeeId) {
        List<PositionAssignmentDto> positionAssignments = positionAssignmentDao.findAll(
            PositionAssignmentFilter.builder()
                .employeeId(employeeId)
                .unitCode(unitAccessService.getCurrentUnit())
                .build()
        ).stream()
            .map(PositionAssignmentFactory::create)
            .toList();
        return new PositionAssignmentInfo(positionAssignments);
    }

    @Override
    public List<EmployeeEntity> findByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }
        return employeeDao.findByIds(ids);
    }

    @Override
    public EmployeeEntity findById(Long id) {
        if (id == null) {
            throw new NotFoundException("Невозможно найти пользователя с id = null");
        }
        return employeeDao.findByIds(List.of(id))
            .stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException(
                String.format("Невозможно найти пользователя с id = %d", id))
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Long getEmployeeIdByExternalId(String externalId) {
        if (externalId == null) {
            return null;
        }
        return employeeDao.findIdByExternalId(externalId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeEntity> getEmployeeByExternalId(String externalId) {
        if (externalId == null) {
            return Optional.empty();
        }
        return employeeDao.findByExternalId(externalId);
    }

    @Override
    public Page<EmployeeEntity> findEmployee(List<Long> employeeIds, List<Long> divisionIds, List<Long> functionIds, String searchingValue, List<Long> legalEntityIds, Boolean withPatronymic, Long jobTitleId, Pageable pageable) {
        Page<EmployeeEntity> result;
        if (CollectionUtils.isEmpty(employeeIds) && CollectionUtils.isEmpty(functionIds) && CollectionUtils.isEmpty(divisionIds) && searchingValue == null
                && CollectionUtils.isEmpty(legalEntityIds) && withPatronymic == null && jobTitleId == null) {
            result = employeeDao.findAll(pageable);
        } else {
            EmployeeSpecification specification = new EmployeeSpecification();
            if (!CollectionUtils.isEmpty(employeeIds)) {
                specification.add(new SearchCriteria("id", employeeIds, SearchOperation.IN));
            }
            if (!CollectionUtils.isEmpty(divisionIds)) {
                specification.add(new SearchCriteria("", divisionIds, SearchOperation.IN_LIST_DIVISION_ID));
            }
            if (!CollectionUtils.isEmpty(functionIds)) {
                specification.add(new SearchCriteria("", functionIds, SearchOperation.IN_LIST_FUNCTION_ID));
            }
            if (searchingValue != null) {
                if (withPatronymic == null || withPatronymic) {
                    specification.add(new SearchCriteria("", searchingValue, SearchOperation.IN_VALUE));
                } else {
                    specification.add(new SearchCriteria("", searchingValue, SearchOperation.IN_VALUE_NO_PATR));
                }
            }
            if (!CollectionUtils.isEmpty(legalEntityIds)) {
                specification.add(new SearchCriteria("", legalEntityIds, SearchOperation.IN_LIST_LEGAL_ENTITY_ID));
            }
            if (jobTitleId != null) {
                specification.add(new SearchCriteria("", jobTitleId, SearchOperation.EQUAL_JOB_TITLE_ID));
            }
            result = employeeDao.findAll(specification, pageable);
        }
        return result;
    }

    @Override
    public Page<EmployeeEntity> findEmployeeNew(List<Long> employeeIds, List<Long> divisionIds, List<Long> functionIds, Long jobTitleId, String positionShortName, Long legalEntityId, String searchingValue, Boolean withPatronymic, Boolean withClosed, List<String> employeeNumber, List<String> emails, Pageable pageable) {
        if (CollectionUtils.isEmpty(employeeIds) && CollectionUtils.isEmpty(functionIds) && CollectionUtils.isEmpty(divisionIds) && jobTitleId == null &&
            StringUtils.isBlank(positionShortName) && legalEntityId == null && StringUtils.isEmpty(searchingValue) && withClosed &&
            CollectionUtils.isEmpty(employeeNumber) && CollectionUtils.isEmpty(emails)) {

            return employeeDao.findAll(pageable);
        }

        return employeeDao.findByParams(employeeIds, divisionIds, functionIds, jobTitleId, positionShortName, legalEntityId, searchingValue, withPatronymic, withClosed, employeeNumber, emails, pageable);
    }

    @Override
    public List<EmployeeInfoDto> getEmployeeInfoList(Collection<EmployeeEntity> employees, boolean hasPositionAssignment) {
        List<Long> employeeIds = employees.stream().map(EmployeeEntity::getId).collect(Collectors.toList());
        Map<Long, List<PositionAssignmentEntity>> positionAssignmentByEmployeeIds =
            positionAssignmentDao.findActualByEmployeeIds(employeeIds, unitAccessService.getCurrentUnit());
        return employees
                .stream()
                .map(emp -> EmployeeInfoFactory.createWithJobInfo(emp, positionAssignmentByEmployeeIds.getOrDefault(emp.getId(), Collections.emptyList())))
                .filter(emp -> {
                    if (hasPositionAssignment) {
                        return !emp.getPositionAssignments().isEmpty();
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EmployeeEntity> getEmployee(Long id, String externalId) {
        if (id != null) {
            return employeeDao.findByIdWithFetch(id);
        }
        if (externalId != null) {
            return getEmployeeByExternalId(externalId);
        }
        return getEmployeeByExternalId(authService.getCurrentUser().getEmployeeExternalId());
    }

    private EmployeeSearchResult getDivisionTeamHead(DivisionTeamAssignmentEntity employeeAssignment) {
        try {
            if (employeeAssignment == null) {
                log.debug("Employee assignment is null");
                return new EmployeeSearchResult(false, null, -1L, -1L);
            }
            RoleEntity role = roleDao.findById(employeeAssignment.getDivisionTeamRole().getRole().getId()).get();
            DivisionTeamEntity divisionTeam = divisionTeamDao.findById(employeeAssignment.getDivisionTeamRole().getDivisionTeam().getId()).get();
            // this is protection against cycling, for brake recursive algorithm
            List<Long> processedTeamDivisions = new ArrayList<>();
            if (!Objects.equals(role.getSystemRole().getId(), GlobalDefs.HEAD_SYSTEM_ROLE_ID)) {
                EmployeeSearchResult employeeSearchResult = getDivisionTeamHead(employeeAssignment.getDivisionTeamRole().getDivisionTeam().getId(), processedTeamDivisions);
                if (employeeSearchResult.getSearchStatus()) {
                    return employeeSearchResult;
                }
            }
            List<DivisionTeamEntity> parentDivisionTeams = divisionTeamDao.findByDivisionId(divisionTeam.getDivision().getParentId());
            if (parentDivisionTeams.isEmpty()) {
                throw new NotFoundException(String.format("Division team %d parent does not exists", divisionTeam.getId()));
            }
            return getDivisionTeamHead(parentDivisionTeams.get(0).getId(), processedTeamDivisions);
        } catch (Exception e) {
            log.error("An error occurred during getting Division Head Impl, reason: {}", e.getMessage());
            return new EmployeeSearchResult(false, null, -1L, -1L);
        }
    }

    private EmployeeSearchResult getDivisionTeamHead(Long divisionTeamId, List<Long> processedDivisionTeams) {
        if (processedDivisionTeams.stream().anyMatch(item -> Objects.equals(item, divisionTeamId))) {
            log.error("A loop in division team for division: %d during search division team head", divisionTeamId);
            throw new VerifyError(String.format("Processed division teams list already contains this &d division team (LOOP)!", divisionTeamId));
        }
        List<DivisionTeamAssignmentEntity> headAssignments = assignmentService.getHeadAssignment(divisionTeamId);
        if (headAssignments != null && !headAssignments.isEmpty()) {
            Long employeeId = headAssignments.get(0).getEmployee().getId();
            EmployeeEntity employee = employeeDao.findById(employeeId).get();
            return new EmployeeSearchResult(true, employee, divisionTeamId, headAssignments.get(0).getId());
        }
        processedDivisionTeams.add(divisionTeamId);
        Optional<DivisionTeamEntity> divisionTeam = divisionTeamDao.findById(divisionTeamId);
        if (divisionTeam.isEmpty()) {
            log.debug("Division team {} does not exists", divisionTeamId);
            return new EmployeeSearchResult(false, null, -1L, -1L);
        }
        List<DivisionTeamEntity> parentDivisionTeams = divisionTeamDao.findByDivisionId(divisionTeam.get().getDivision().getParentId());
        DivisionTeamEntity parentDivisionTeam = !parentDivisionTeams.isEmpty() ? parentDivisionTeams.get(0) : null;
        if (parentDivisionTeam == null) {
            log.debug("Division team {} parent does not exists", divisionTeamId);
            return new EmployeeSearchResult(false, null, -1L, -1L);
        }
        return getDivisionTeamHead(parentDivisionTeam.getId(), processedDivisionTeams);
    }

    @Override
    public ByteArrayOutputStream checkNumbersExcel(MultipartFile file) throws IOException, NotFoundException {
        ResponseNumberDto dto = checkNumbers(file);

        List<String> unconfirmed = dto.getUnconfirmed();
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("numbers");
        int i = 0;
        Row row = sheet.createRow(i);
        row.createCell(0).setCellValue("Unconfirmed");
        for (String number : unconfirmed) {
            i++;
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(number);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream;
    }

    @Override
    public List<EmployeeFlatInfoDto> getEmployeeFlatInfo(Long id, String externalId) {
        List<PositionAssignmentEntity> actualAssignments = positionAssignmentDao.findActual(id, externalId, unitAccessService.getCurrentUnit());

        return actualAssignments.stream()
            .map(assignment -> {
                PersonEntity person = assignment.getEmployee().getPerson();
                String fio = Stream.of(person.getSurname(), person.getName(), person.getPatronymic())
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(" "));

                return new EmployeeFlatInfoDto(assignment.getEmployeeId(),fio, person.getPhoto(), assignment.getFullName());
            })
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public ResponseNumberDto checkNumbers(MultipartFile file) throws IOException, NotFoundException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getPhysicalNumberOfRows() - 1;
        List<String> fileNumbers = new ArrayList<>();

        for (int i = 2; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row.getPhysicalNumberOfCells() == 0) {
                continue;
            }
            String number = getStringValue(row.getCell(0));
            fileNumbers.add(number);
        }

        List<String> confirmedNumbers = employeeDao.getActualNumbers(fileNumbers);
        fileNumbers.removeAll(confirmedNumbers);
        return new ResponseNumberDto(confirmedNumbers, fileNumbers);
    }

    @Override
    public PageResponse<EmployeesByStatusDto> getEmployeesByStatuses(EmployeeByStatusRequest request,
                                                                     boolean subordinatesOnly) {
        List<TaskFindDto> goalCards;
        Map<Integer, EmployeeInfoDto> idEmployeeMap;
        if (subordinatesOnly) {
            long employeeId = authService.getUserEmployeeId();
            idEmployeeMap = getSubordinates(employeeId)
                .stream()
                .collect(toMap(
                    subordinate -> subordinate.getId().intValue(),
                    Function.identity())
                );
            goalCards = indicatorGoalService.findGoalCards(idEmployeeMap.keySet(), request.getProcessId());
        } else {
            goalCards = indicatorGoalService.findGoalCards(Set.of(), request.getProcessId());
            Set<Long> processMemberIds = goalCards.stream()
                .map(TaskFindDto::getUserId)
                .collect(toSet());

            idEmployeeMap = findByIds(processMemberIds).stream()
                .collect(Collectors.toMap(
                    employee -> employee.getId().intValue(),
                    EmployeeInfoFactory::create
                ));
        }

        int totalEmployeeAmount = goalCards.size();
        ProcessEventStageDto currentEvent = processEventService.findCurrentEvent(request.getProcessId());
        List<ProcessEventStageDto> events;
        if (currentEvent != null) {
            events = List.of(currentEvent);
        } else {
            events = processEventService.getProcessEvents(request.getProcessId());
        }

        Map<Long, StageOutputDto> statusIdStatusMap = events.stream()
            .flatMap(event -> event.getStages().stream())
            .collect(toMap(
                StageOutputDto::getStatusId,
                Function.identity(),
                (existing, replacement) -> existing)
            );

        Map<Long, Set<Long>> statusEmployeeMap = goalCards.stream()
            .filter(goalCard -> statusIdStatusMap.containsKey(goalCard.getStatus().getId()))
            .collect(groupingBy(
                goalCard -> goalCard.getStatus().getId(),
                mapping(TaskFindDto::getUserId, toSet()))
            );

        Map<Long, StatusEntity> idStatusMap = statusService.findActualByIds(statusIdStatusMap.keySet())
            .stream()
            .collect(toMap(StatusEntity::getId, Function.identity()));

        List<EmployeesByStatusDto> data = new ArrayList<>();
        for (Map.Entry<Long, StageOutputDto> statusIdStatusEntry : statusIdStatusMap.entrySet()) {
            Long statusId = statusIdStatusEntry.getKey();
            String statusName = idStatusMap.get(statusId).getName();
            Set<Long> currentStatusEmployeeIds = statusEmployeeMap.getOrDefault(statusId, Set.of());
            int currentStatusEmployeeAmount = currentStatusEmployeeIds.size();
            if (currentEvent == null && currentStatusEmployeeAmount == 0) {
                continue;
            }
            double percent = UtilClass.round((double) currentStatusEmployeeAmount / totalEmployeeAmount * 100, 2);

            List<EmployeesByStatusDto.EmployeePhotoDto> photoDtoList = mapToPhotoDtoList(
                currentStatusEmployeeIds,
                idEmployeeMap
            );

            StageOutputDto stage = statusIdStatusEntry.getValue();
            LocalDateTime dateStart = !stage.getDateStart().isEmpty() ? LocalDateTime.parse(stage.getDateStart()) : null;
            LocalDateTime dateFinish = !stage.getDateFinish().isEmpty() ? LocalDateTime.parse(stage.getDateFinish()) : null;
            String style = calculateStageStyle(dateStart, dateFinish, currentStatusEmployeeAmount);

            EmployeesByStatusDto dto = new EmployeesByStatusDto();
            dto.setStatusId(statusId);
            dto.setStatusName(statusName);
            dto.setEmployeeStatus(currentStatusEmployeeAmount);
            dto.setEmployeeAll(totalEmployeeAmount);
            dto.setEmployeePercent(percent);
            dto.setEmployeePhotos(photoDtoList);
            dto.setDateStart(dateStart);
            dto.setDateEnd(dateFinish);
            dto.setStageStyle(style);
            data.add(dto);
        }
        return UtilClass.wrapWithPageResponse(request, data);
    }

    private String calculateStageStyle(LocalDateTime dateStart, LocalDateTime dateEnd, int employeeAmount) {
        if (dateStart == null || dateEnd == null) {
            return null;
        }
        LocalDate now = LocalDate.now();
        if (now.isBefore(dateStart.toLocalDate())) {
            return "color: #9E9E9E";
        } else if (!now.isAfter(dateEnd.toLocalDate())) {
            return "color: #0085FF";
        } else {
            return employeeAmount == 0 ? "color: #9E9E9E" : "color: #F82F58";
        }
    }

    @Override
    public List<EmployeeDto> getCycleSubordinates(Long employeeId, Long cycleId) {
        CycleEntity cycle = cycleService.getSecuredById(cycleId);

        DivisionTeamEntity divisionTeam = divisionTeamDao.findByHeadEmployeeId(employeeId);
        if (divisionTeam == null) {
            throw new NotFoundException(String.format("Division with head employee id = %d is not found", employeeId));
        }
        List<EmployeeEntity> directSubordinates = employeeDao.findWorkersByDivisionId(divisionTeam.getId());
        List<EmployeeEntity> subdivisionHeads = employeeDao.findSubdivisionHeadsByDivisionId(divisionTeam.getId());

        List<EmployeeEntity> subordinates = new ArrayList<>(directSubordinates);
        subordinates.addAll(subdivisionHeads);

        Set<Long> employeeIds = subordinates.stream()
            .map(EmployeeEntity::getId)
            .collect(Collectors.toSet());

        Map<Long, PositionEntity> employeeIdPositionMap = positionDao.findByEmployeeIds(employeeIds, divisionTeam.getId(), unitAccessService.getCurrentUnit());

        List<Long> cycleCardEmployeeIds = cycleService.findCycleTasksByCycleId(cycle.getId()).stream()
            .map(CycleTaskEntity::getTask)
            .map(TaskEntity::getUserId)
            .map(Long::valueOf)
            .collect(Collectors.toList());

        List<ProcessEntity> processList = processService.getProcessesByCycleId(cycle.getId());
        List<Long> processIds = processList.stream().map(ProcessEntity::getId).collect(Collectors.toList());

        Map<Long, ProcessTaskEntity> processTasks = processTaskDao.findByProcessIdList(processIds, unitAccessService.getCurrentUnit())
            .stream()
            .filter(processTaskEntity -> ComponentCode.GOAL_CARD_GM.equals(
                Optional.ofNullable(processTaskEntity.getTask())
                    .map(TaskEntity::getType)
                    .map(TaskTypeEntity::getComponent)
                    .map(ComponentEntity::getCode)
                    .orElse(null)
            ))
            .distinct()
            .collect(Collectors.toMap(
                processTaskEntity -> Long.valueOf(processTaskEntity.getTask().getUserId()),
                Function.identity(),
                (first, second) -> second
            ));

        return subordinates.stream()
            .filter(employeeEntity -> cycleCardEmployeeIds.contains(employeeEntity.getId()))
            .map(employeeEntity -> employeeMapper.toDto(
                employeeIdPositionMap,
                processTasks,
                employeeEntity
            ))
            .collect(Collectors.toList());
    }

    @Override
    public FilterAwarePageResponse<ProcessEmployeeDto> findProcessEmployees(ProcessEmployeeRequest request) {
        ProcessEntity process = processService.getProcessById(request.getProcessId());
        List<TaskFindDto> goalCards = indicatorGoalService.findGoalCards(List.of(), process.getId());
        if (goalCards.isEmpty()) {
            return new FilterAwarePageResponse<>();
        }

        Map<Long, TaskFindDto> employeeIdGoalCardMap = goalCards.stream()
            .collect(toMap(TaskFindDto::getUserId, Function.identity()));
        Set<Long> employeeIds = employeeIdGoalCardMap.keySet();
        List<EmployeeEntity> employees = employeeDao.findByIds(employeeIds);
        var positionAssignmentsByEmployeeIds = positionService.findActualPositionAssignmentByEmployeeIds(employeeIds);

        List<ProcessEmployeeDto> result = new ArrayList<>();
        for (EmployeeEntity employee : employees) {
            PersonEntity person = employee.getPerson();
            String employeeName = person.getSurname() + " " + person.getName();
            if (!StringUtils.isEmpty(request.getEmployeeName())
                && !StringUtils.containsIgnoreCase(employeeName, request.getEmployeeName())) {
                continue;
            }
            TaskFindDto goalCard = employeeIdGoalCardMap.get(employee.getId());
            StatusDto status = goalCard.getStatus();
            if (!CollectionUtils.isEmpty(request.getStatusIds()) && !request.getStatusIds().contains(status.getId())) {
                continue;
            }
            List<PositionAssignmentEntity> positionsAssignments = positionAssignmentsByEmployeeIds.get(employee.getId());
            for (PositionAssignmentEntity positionAssignment : positionsAssignments) {
                PositionEntity position = positionAssignment.getPosition();
                DivisionEntity division = position.getDivision();
                if (request.getDivisionId() != null && !request.getDivisionId().equals(division.getId())) {
                    continue;
                }
                String photoOrInitials = PersonUtil.getPhotoOrInitials(person.getPhoto(), person.getSurname(), person.getName());

                ProcessEmployeeDto dto  = new ProcessEmployeeDto();
                dto.setEmployeeId(employee.getId());
                dto.setEmployeePhoto(photoOrInitials);
                dto.setEmployeeName(employeeName);
                dto.setPositionId(position.getId());
                dto.setPositionName(position.getShortName());
                dto.setDivisionId(division.getId());
                dto.setDivisionName(division.getShortName());
                dto.setStatusId(status.getId());
                dto.setStatusName(status.getName());

                result.add(dto);
            }
        }

        List<FilterDto> filters = List.of();
        if (request.isUseFilter()) {
           filters = buildFilters(goalCards);
        }

        return UtilClass.wrapWithFilterAwarePageResponse(request, result, filters);
    }

    private List<FilterDto> buildFilters(List<TaskFindDto> goalCards) {
        Set<FilterOption> statusOptions = goalCards.stream()
            .map(TaskFindDto::getStatus)
            .map(statusDto -> new FilterOption(statusDto.getName(), statusDto.getId()))
            .collect(toSet());
        FilterDto statusFilter = FilterDtoFactory.createSelectFilter("status_name", "Статус", statusOptions);
        FilterDto employeeNameFilter = FilterDtoFactory.createTextFilter("employee_name", "ФИО работника");
        return List.of(employeeNameFilter, statusFilter);
    }

    private List<EmployeesByStatusDto.EmployeePhotoDto> mapToPhotoDtoList(Set<Long> employeeIds,
                                                                          Map<Integer, EmployeeInfoDto> idSubordinateMap) {
        return employeeIds.stream()
            .map(id -> idSubordinateMap.get(id.intValue()))
            .filter(Objects::nonNull)
            .map(employee -> {
                EmployeesByStatusDto.EmployeePhotoDto photoDto = new EmployeesByStatusDto.EmployeePhotoDto();
                String fullName = PersonUtil.getFullName(
                    employee.getLastName(),
                    employee.getFirstName(),
                    employee.getMiddleName()
                );
                String photo = PersonUtil.getPhotoOrInitials(
                    employee.getPhoto(),
                    employee.getLastName(),
                    employee.getFirstName()
                );
                photoDto.setEmployeeName(fullName);
                photoDto.setPhotoUrl(photo);
                return photoDto;
            })
            .collect(toList());
    }

    private List<EmployeeInfoDto> getSubordinates(Long employeeId) {
        return assignmentService.getDivisionTeamSubordinates(employeeId, null, null, false)
            .stream()
            .filter(assignment -> assignment.getDivisionTeamRole().getRole().getId() == 2L)
            .map(DivisionTeamAssignmentShortDto::getEmployee)
            .collect(Collectors.toList());
    }

    private static String getStringValue(Cell cell) throws NotFoundException {
        if (cell.getCellType().equals(CellType.STRING)) {
            return cell.getStringCellValue();

        } else if (cell.getCellType().equals(CellType.NUMERIC)) {
            double number = cell.getNumericCellValue();
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            decimalFormat.setDecimalSeparatorAlwaysShown(false);
            return decimalFormat.format(number);
        }
        log.error("Expected data type string, received: {}", cell);
        return StringUtils.EMPTY;
    }

    @Override
    public String getUnitCode(Long employeeId) {
        return positionAssignmentDao.findByEmployeeId(employeeId).getPosition().getDivision().getLegalEntityEntity().getUnitCode();
    }

    @Override
    public Long findIdByExternalId(String externalId) {
        return employeeDao.findIdByExternalId(externalId);
    }

    @Override
    public EmployeeInfoDto getEmployeeInfo(Long employeeId) {
        return getEmployeeInfo(employeeId, null);
    }

    @Override
    public EmployeeInfoDto getEmployeeInfo(Long employeeId, String externalId) {
        EmployeeEntity employee = findEmployee(employeeId, externalId);
        return EmployeeInfoFactory.create(employee);
    }

    @Override
    public EmployeeExtendedInfoDto getEmployeeExtendedInfo(Long employeeId, String externalId) {
        EmployeeEntity employee = findEmployee(employeeId, externalId);
        PositionAssignmentInfo info = getPositionAssignmentInfo(employee.getId());
        return EmployeeExtendedInfoFactory.create(
            EmployeeInfoFactory.create(employee),
            PersonFactory.create(employee.getPerson()),
            info.getAssignments());
    }

    @Override
    public EmployeeInfoDto getEmployeeInfoByAssignment(Long divisionTeamAssignmentId) {
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

    private EmployeeEntity findEmployee(Long employeeId, String externalId) {
        Long id = employeeId;
        if (id == null && StringUtils.isBlank(externalId)) {
            id = authService.getUserEmployeeId();
        }

        List<EmployeeEntity> employees = employeeDao.findByIdAndExternalId(id, externalId);
        if (employees.isEmpty()) {
            throw new NotFoundException(String.format("Employee with id=%d and external_employee=%s is not found", id, externalId));
        } else if (employees.size() > 1) {
            throw new NotFoundException(String.format("Found several employees with id=%d and external_employee=%s. " +
                "Perhaps there is no record in the employee table. ", id, externalId));
        }
        return employees.getFirst();
    }
}
