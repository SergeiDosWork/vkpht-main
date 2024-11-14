package me.goodt.vkpht.module.notification.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.common.api.LoggerService;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.CompetenceService;
import me.goodt.vkpht.module.notification.api.EventService;
import me.goodt.vkpht.module.notification.api.dto.BaseNotificationInputData;
import me.goodt.vkpht.module.notification.api.dto.CustomEmailEventCodeInputDto;
import me.goodt.vkpht.module.notification.api.dto.CustomEmailEventInputDto;
import me.goodt.vkpht.module.notification.api.dto.DataForKafkaMessageInputDto;
import me.goodt.vkpht.module.notification.api.dto.DevFormAchievementDto;
import me.goodt.vkpht.module.notification.api.dto.EventInputDto;
import me.goodt.vkpht.module.notification.api.dto.KrAchievementEventDto;
import me.goodt.vkpht.module.notification.api.dto.KrProgressDto;
import me.goodt.vkpht.module.notification.api.dto.Params;
import me.goodt.vkpht.module.notification.api.dto.QuizFinishInputDto;
import me.goodt.vkpht.module.notification.api.dto.QuizStartInputDto;
import me.goodt.vkpht.module.notification.api.dto.RemindByHeadInputDto;
import me.goodt.vkpht.module.notification.api.dto.SuccessorNotificateByCodeInputData;
import me.goodt.vkpht.module.notification.api.dto.TaskAchievementDto;
import me.goodt.vkpht.module.notification.api.dto.data.DataFromStatusChangeToRostalentStatusChange;
import me.goodt.vkpht.module.notification.api.dto.data.OperationResult;
import me.goodt.vkpht.module.notification.api.dto.monitor.EventDto;
import me.goodt.vkpht.module.notification.api.dto.monitor.EventExtendedDto;
import me.goodt.vkpht.module.notification.api.dto.monitor.ShortEventDto;
import me.goodt.vkpht.module.notification.api.dto.monitor.TaskEstimationMarkDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.CompetenceNotificationDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.DevelopmentFormNotificationDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskFieldDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskFindRequest;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.UserTaskTreeDto;
import me.goodt.vkpht.module.notification.api.monitor.MonitorServiceClient;
import me.goodt.vkpht.module.notification.api.rtcore.RtCoreServiceClient;
import me.goodt.vkpht.module.notification.api.tasksetting2.TasksettingServiceClient;
import me.goodt.vkpht.module.notification.application.utils.TextConstants;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.COMPETENCE_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.GOAL_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.KP_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.NAME_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.PARENT_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.PROGRESS_OLD_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.PROGRESS_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TASK_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TASK_TYPE_NAME_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.TITLE_TAG;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.TASK_TYPE_ID_5;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController {

    public static final long EVENT_TYPE_ID_TASK_TYPE_5 = 19L;
    public static final long EVENT_TYPE_ID_DEFAULT = 3L;
    private static final List<Long> KRACHIEVEMENT_ALLOW_TYPE = Arrays.asList(4L, 23L);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final CompetenceService competenceService;
    private final MonitorServiceClient monitorService;
    private final TasksettingServiceClient tasksettingServiceClient;
    private final RtCoreServiceClient rtCoreServiceClient;
    private final EventService eventService;
    private final LoggerService loggerService;

    private static Long getEventTypeIdByTaskTypeId(Long taskTypeId) {
        if (Objects.equals(taskTypeId, TASK_TYPE_ID_5)) {
            return EVENT_TYPE_ID_TASK_TYPE_5;
        } else {
            return EVENT_TYPE_ID_DEFAULT;
        }
    }

    @Operation(summary = "Фиксация факта фиксации достижения ключевого результата", description = "Фиксация факта фиксации достижения ключевого результата. Метод ожидает во входных данных task_type = 4", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/krachievement")
    @Transactional(rollbackFor = Exception.class)
    public EventDto createKrAchievementEvent(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для создание нового фиксации достижения ключевого результата", required = true)
        @RequestBody KrAchievementEventDto data) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST api/event/create/krachievement", null, data);

        TaskDto taskDto = tasksettingServiceClient.getTaskById(data.getKrId());

        if (!KRACHIEVEMENT_ALLOW_TYPE.contains(taskDto.getType().getId())) {
            return null;
        }

        Map<String, Object> taskInfo = new LinkedHashMap<>();
        taskInfo.put(ID_TAG, taskDto.getId());
        taskInfo.put(PARENT_ID_TAG, taskDto.getParentId());
        taskInfo.put(NAME_TAG, getName(taskDto));
        taskInfo.put(TASK_TYPE_NAME_TAG, taskDto.getType().getName());
        Map<String, String> progress = getProgressAndProgressOld(taskDto);
        taskInfo.put(PROGRESS_TAG, progress.get(PROGRESS_TAG));
        taskInfo.put(PROGRESS_OLD_TAG, progress.get(PROGRESS_OLD_TAG));

        List<UserTaskTreeDto> trees =
            tasksettingServiceClient.getTaskTrees(
                null,
                Collections.singletonList(taskDto.getUserId()), 1L, null, true);

        if (trees == null || trees.isEmpty()) {
            return null;
        }
        Long parentId = taskDto.getParentId();

        Map<String, Object> eventBody = new LinkedHashMap<>();
        List<Map<String, String>> krs = new ArrayList<>();

        List<TaskDto> taskList = trees.getFirst().getTaskTree().stream().sorted(Comparator.comparingLong(i -> i.getDateFrom().getTime())).collect(Collectors.toList());
        for (TaskDto task : taskList) {
            if (task.getId().equals(parentId)) {
                eventBody.put(GOAL_TAG, buildMapOfTaskObject(task, false));
            } else if (task.getParentId() != null && task.getParentId().equals(parentId)) {
                krs.add(buildMapOfTaskObject(task, true));
            }
        }

        eventBody.put(COMPETENCE_TAG, getCompetenceData(data.getCompetenceId()));
        eventBody.put(TASK_TAG, taskInfo);
        eventBody.put(KP_TAG, krs);

        log.info("response: {}", OBJECT_MAPPER.writeValueAsString(eventBody));
        ShortEventDto shortEventDto = new ShortEventDto(null, data.getAssignmentId(), data.getAuthorName(), data.getAuthorImage(),
            OBJECT_MAPPER.writeValueAsString(eventBody), 11L, 1L, data.getProcessId());

        EventDto eventDto = monitorService.createEvent(shortEventDto);
        eventService.executeBaseNotificationForEventCreate(11L, eventDto.getId());
        return eventDto;
    }

    @Operation(summary = "Фиксация прогресса в достижении ключевого результата", description = "Фиксация прогресса в достижении ключевого результата", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/krprogress")
    @Transactional(rollbackFor = Exception.class)
    public EventExtendedDto createKrProgress(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для создания новой фиксации прогресса в достижении ключевого результата", required = true)
                                             @RequestBody KrProgressDto body) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/krprogress", null, body);
        TaskDto taskDto = tasksettingServiceClient.getTaskById(body.getKrId());
        if (taskDto.getType().getId() != 4L) {
            return null;
        }
        List<UserTaskTreeDto> trees = tasksettingServiceClient.getTaskTrees(null,
            Collections.singletonList(taskDto.getUserId()), 1L, null, true);

        Map<String, Object> event = new HashMap<>();
        Map<String, Object> goal = new HashMap<>();
        List<Map<String, Object>> kr = new ArrayList<>();

        Optional<TaskDto> optionalParentTask = trees.getFirst().getTaskTree().stream()
            .filter(x -> x.getId().equals(taskDto.getParentId()))
            .findFirst();
        if (optionalParentTask.isEmpty()) {
            return null;
        }

        TaskDto parentTaskDto = optionalParentTask.get();

        goal.put(NAME_TAG, parentTaskDto.getType().getName());
        TaskFieldDto progressField = getProgressField(parentTaskDto);
        if (progressField != null) {
            goal.put(PROGRESS_TAG, progressField.getValue());
        }

        for (TaskDto task : trees.getFirst().getTaskTree()) {
            if (task.getParentId() != null && task.getParentId().equals(taskDto.getParentId())) {
                goal.put(NAME_TAG, parentTaskDto.getType().getName());
                TaskFieldDto taskFieldDto = getProgressField(task);
                if (taskFieldDto != null) {
                    goal.put(PROGRESS_TAG, taskFieldDto.getValue());
                } else {
                    goal.put(PROGRESS_TAG, null);
                }
            } else {
                if ((task.getId() != null && task.getId().equals(body.getKrId())) || (task.getParentId() != null && task.getParentId().equals(parentTaskDto.getId()))) {
                    //progress_old
                    Map<String, Object> krElement = new HashMap<>();
                    TaskFieldDto taskFieldDto = getProgressField(task);
                    if (taskFieldDto != null) {
                        krElement.put(PROGRESS_TAG, taskFieldDto.getValue());
                    } else {
                        krElement.put(PROGRESS_TAG, null);
                    }
                    krElement.put(NAME_TAG, getName(task));
                    kr.add(krElement);
                }
            }
        }

        event.put(GOAL_TAG, goal);
        event.put("kr", kr);
        event.put(COMPETENCE_TAG, getCompetenceData(body.getCompetenceId()));

        ShortEventDto dto = new ShortEventDto(null, body.getAssignmentId(), body.getAuthorName(), body.getAuthorImage(),
            OBJECT_MAPPER.writeValueAsString(event), 11L, 1L, null);

        List<TaskEstimationMarkDto> marks = monitorService.getMarks(taskDto.getId());
        List<TaskEstimationMarkDto> marksCreated = monitorService.createTaskMarks(body.getKrId());
        marks.addAll(marksCreated);
        EventDto eventDto = monitorService.createEvent(dto);

        return new EventExtendedDto(eventDto, marks);
    }

    @Operation(summary = "Фиксация нарушения по ценностям", description = "Фиксация достижений по ценностям Лидерство, Дисциплина, Устойчивое развитие; фиксация нарушений по ценностям Дисциплина, Устойчивое развитие", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/devformachievementviolation")
    @Transactional(rollbackFor = Exception.class)
    public EventDto createDevformAchievementViolation(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для фиксации нарушения по ценности Дисциплина", required = true)
                                                      @RequestBody DevFormAchievementDto body) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST api/event/create/devformachievementviolation", null, body);
        DevelopmentFormNotificationDto developmentFormDto = rtCoreServiceClient.findDevelopmentFormById(body.getDevelopmentFormId());
        EventDto eventDto = monitorService.createEvent(formDevFormAchievementEvent(body));
        eventDto.setDevelopmentFormName(developmentFormDto.getName());
        eventService.executeBaseNotificationForEventCreate(body.getEventTypeId(), eventDto.getId());
        return eventDto;
    }

    /**
     * @deprecated use /api/event/create/devformachievementviolation
     */
    @Operation(summary = "Фиксация нарушения по ценности Дисциплина", description = "Фиксация нарушения по ценности Дисциплина", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/violationdiscipline")
    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    public EventDto createViolationDiscipline(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для фиксации нарушения по ценности Дисциплина", required = true)
                                              @RequestBody DevFormAchievementDto body) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST api/event/create/violationdiscipline", null, body);
        DevelopmentFormNotificationDto developmentFormDto = rtCoreServiceClient.findDevelopmentFormById(body.getDevelopmentFormId());
        body.setEventTypeId(7L);
        EventDto eventDto = monitorService.createEvent(formDevFormAchievementEvent(body));
        eventDto.setDevelopmentFormName(developmentFormDto.getName());
        return eventDto;
    }

    /**
     * @deprecated use /api/event/create/devformachievementviolation
     */
    @Operation(summary = "Фиксация нарушения по ценности Устойчивое развитие", description = "Фиксация нарушения по ценности Устойчивое развитие", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/violationur")
    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    public EventDto createViolationUr(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для фиксации нарушения по ценности Устойчивое развитие", required = true)
                                      @RequestBody DevFormAchievementDto body) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST api/event/create/violationur", null, body);

        DevelopmentFormNotificationDto developmentFormDto = rtCoreServiceClient.findDevelopmentFormById(body.getDevelopmentFormId());
        body.setEventTypeId(8L);
        EventDto eventDto = monitorService.createEvent(formDevFormAchievementEvent(body));
        eventDto.setDevelopmentFormName(developmentFormDto.getName());
        return eventDto;
    }

    /**
     * @deprecated use /api/event/create/devformachievementviolation
     */
    @Operation(summary = "Фиксация достижения по ценности Устойчивое развитие", description = "Фиксация достижения по ценности Устойчивое развитие", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/devformachievementur")
    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    public EventDto createDevFormAchievementUr(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для фиксации достижения по ценности Устойчивое развитие", required = true)
                                               @RequestBody DevFormAchievementDto body) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST api/event/create/devformachievementur", null, body);

        DevelopmentFormNotificationDto developmentFormDto = rtCoreServiceClient.findDevelopmentFormById(body.getDevelopmentFormId());
        body.setEventTypeId(1L);
        EventDto eventDto = monitorService.createEvent(formDevFormAchievementEvent(body));
        eventDto.setDevelopmentFormName(developmentFormDto.getName());
        eventService.executeBaseNotificationForEventCreate(body.getEventTypeId(), eventDto.getId());
        return eventDto;
    }

    /**
     * @deprecated use /api/event/create/devformachievementviolation
     */
    @Operation(summary = "Фиксация достижения по ценности Дисциплина", description = "Фиксация достижения по ценности Дисциплина", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/devformachievementdiscipline")
    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    public EventDto createDevFormAchievementDiscipline(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для фиксации достижения по ценности Дисциплина", required = true)
                                                       @RequestBody DevFormAchievementDto body) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST api/event/create/devformachievementdiscipline", null, body);
        DevelopmentFormNotificationDto developmentFormDto = rtCoreServiceClient.findDevelopmentFormById(body.getDevelopmentFormId());
        body.setEventTypeId(21L);

        EventDto eventDto = monitorService.createEvent(formDevFormAchievementEvent(body));
        eventDto.setDevelopmentFormName(developmentFormDto.getName());
        eventService.executeBaseNotificationForEventCreate(body.getEventTypeId(), eventDto.getId());
        return eventDto;
    }

    /**
     * @deprecated use /api/event/create/devformachievementviolation
     */
    @Operation(summary = "Фиксация достижения по ценности Лидерство", description = "Фиксация достижения по ценности Лидерство", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/devfromachievementleadership")
    @Transactional(rollbackFor = Exception.class)
    @Deprecated
    public EventDto createDevFormAchievement(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для фиксации достижения по ценности Лидерство", required = true)
                                             @RequestBody DevFormAchievementDto body) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST api/event/create/devformachievementur", null, body);
        DevelopmentFormNotificationDto developmentFormDto = rtCoreServiceClient.findDevelopmentFormById(body.getDevelopmentFormId());
        body.setEventTypeId(22L);
        EventDto eventDto = monitorService.createEvent(formDevFormAchievementEvent(body));
        eventDto.setDevelopmentFormName(developmentFormDto.getName());
        eventService.executeBaseNotificationForEventCreate(body.getEventTypeId(), eventDto.getId());
        return eventDto;
    }

    @Operation(summary = "Создание факта фиксации достижения цели", description = "Создание факта фиксации достижения цели", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/taskachievement")
    @Transactional(rollbackFor = Exception.class)
    public EventExtendedDto createTaskAchievement(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для создание нового факта фиксации достижения цели", required = true)
        @RequestBody TaskAchievementDto body) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/taskachievement", null, body);
        List<TaskDto> tasks = tasksettingServiceClient.taskFind(
            new TaskFindRequest().setIds(Collections.singletonList(body.getTaskId()))
        );

        if (tasks == null || tasks.isEmpty()) {
            return null;
        }

        TaskDto taskDto = tasks.getFirst();
        Map<String, Object> event = new HashMap<>();
        Map<String, Object> task = new HashMap<>();

        task.put(ID_TAG, taskDto.getId());
        task.put(PARENT_ID_TAG, taskDto.getParentId());
        Map<String, String> progress = getProgressAndProgressOld(taskDto);
        task.put(PROGRESS_TAG, progress.get(PROGRESS_TAG));
        task.put(PROGRESS_OLD_TAG, progress.get(PROGRESS_OLD_TAG));

        task.put(NAME_TAG, getName(taskDto));
        event.put(TASK_TAG, task);
        event.put(COMPETENCE_TAG, getCompetenceData(body.getCompetenceId()));

        Long eventTypeId = getEventTypeIdByTaskTypeId(taskDto.getType().getId());
        ShortEventDto dto = new ShortEventDto(null, body.getAssignmentId(), body.getAuthorName(), body.getAuthorImage(),
            OBJECT_MAPPER.writeValueAsString(event), eventTypeId, 1L, body.getProcessId());
        List<TaskEstimationMarkDto> marks = monitorService.getMarks(taskDto.getId());
        List<TaskEstimationMarkDto> marksCreated = monitorService.createTaskMarks(body.getTaskId());
        marks.addAll(marksCreated);

        EventDto eventDto = monitorService.createEvent(dto);
        eventService.executeBaseNotificationForEventCreate(eventTypeId, eventDto.getId());
        return new EventExtendedDto(eventDto, marks);
    }

    @Operation(summary = "Создание уведомления о входящем запросе на эксперта для руководителя заявки", description = "Создание уведомления о входящем запросе на эксперта", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/experttaskcreate")
    public EventDto expertTaskCreate(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создание нового факта фиксации достижения цели")
        @RequestBody EventInputDto input) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/experttaskcreate", null, input);
        return eventService.expertTaskCreate(input);
    }

    @Operation(summary = "Создание уведомления об отклонении запроса на эксперта", description = "Создание уведомления об отклонении запроса на эксперта", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/employeeheadexpertreject")
    public EventDto employeeHeadExpertReject(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создание нового факта фиксации достижения цели")
        @RequestBody EventInputDto input) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/employeeheadexpertreject", null, input);
        return eventService.employeeHeadExpertReject(input);
    }

    @Operation(summary = "Создание уведомления о входящем запросе на эксперта для руководителя эксперта", description = "Создание уведомления об отклонении запроса на эксперта", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/expertheadnotice")
    public EventDto expertHeadNotice(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создание нового факта фиксации достижения цели")
        @RequestBody EventInputDto input) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/expertheadnotice", null, input);
        return eventService.expertHeadNotice(input);
    }

    @Operation(summary = "Создание уведомления об отклонении запроса на эксперта руководителем эксперта", description = "Создание уведомления об отклонении запроса на эксперта руководителем эксперта", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class)))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/expertheadreject")
    public List<EventDto> expertHeadReject(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создание нового факта фиксации достижения цели")
        @RequestBody EventInputDto input) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/expertheadreject", null, input);
        return eventService.expertHeadReject(input);
    }

    @Operation(summary = "Создание уведомления об отклонении назначения эксперта для работника и руководителей", description = "Создание уведомления об отклонении назначения эксперта для работника и руководителей", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class)))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/hrexpertreject")
    public List<EventDto> hrExpertReject(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создание нового факта фиксации достижения цели")
        @RequestBody EventInputDto input) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/hrexpertreject", null, input);
        return eventService.hrExpertReject(input);
    }

    @Operation(summary = "Создание уведомления о согласовании запроса на эксперта", description = "Создание уведомления о согласовании запроса на эксперта", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class)))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/hrexpertaccept")
    public List<EventDto> hrExpertAccept(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создание нового факта фиксации достижения цели")
        @RequestBody EventInputDto input) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/hrexpertaccept", null, input);
        return eventService.hrExpertAccept(input);
    }

    @Operation(summary = "Создание уведомления о входящем запросе на эксперта для HR", description = "Создание уведомления о входящем запросе на эксперта для HR", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class)))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/hrexpertnotice")
    public List<EventDto> hrExpertNotice(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создание нового факта фиксации достижения цели")
        @RequestBody EventInputDto input) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/hrexpertnotice", null, input);
        return eventService.hrExpertNotice(input);
    }

    @Operation(summary = "Создание уведомления для руководителя о смене готовности преемника", description = "Создание уведомления для руководителя о смене готовности преемника", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объекты в БД были созданы (см. сервис monitor) и удалось получить DTO представление этих объектов",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class)))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/update_division_team_successor_readiness")
    public List<EventDto> updateDivisionTeamSuccessorReadiness(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создание нового факта фиксации достижения цели. Необходимые данные: update_required_days, author_name, button_link")
        @RequestBody EventInputDto input) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/update_division_team_successor_readiness", null, input);
        return eventService.updateDivisionTeamSuccessorReadiness(input);
    }

    @Operation(summary = "Создание уведомления для работника об актуализации готовности к ротации", description = "Создание уведомления для работника об актуализации готовности к ротации", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объекты в БД были созданы (см. сервис monitor) и удалось получить DTO представление этих объектов",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class)))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/update_division_team_assignment_rotation")
    public List<EventDto> updateDivisionTeamAssignmentRotation(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создание нового факта фиксации достижения цели. Необходимые данные: update_required_days (либо employee_assignment_id, если хотим получить информацию по определённому назначению), author_name, button_link_edit, button_link_update")
        @RequestBody EventInputDto input) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/update_division_team_assignment_rotation", null, input);
        return eventService.updateDivisionTeamAssignmentRotation(input);
    }

    @Operation(summary = "Создание уведомления для руководителя о сменен преемника hr-ом", description = "Создание уведомления для руководителя о сменен преемника hr-ом", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объекты в БД были созданы (см. сервис monitor) и удалось получить DTO представление этих объектов",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class)))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/update_successor")
    public List<EventDto> updateSuccessor(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создание нового факта фиксации достижения цели. Необходимые данные: author_name, button_link")
        @RequestBody EventInputDto input) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/update_successor/expertheadreject", null, input);
        return eventService.updateSuccessor(input);
    }

    @Operation(summary = "Отправка уведомления для руководителя о смене готовности преемника", description = "Отправка уведомления для руководителя о смене готовности преемника", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "В случае если не возникло проблем с отправкой уведомления"),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/notification_update_division_team_successor_readiness")
    public void noticeUpdateDivisionTeamSuccessorReadiness() {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/notification_update_division_team_successor_readiness", null, null);
        eventService.noticeUpdateDivisionTeamSuccessorReadiness();
    }

    @Operation(summary = "Метод по созданию event-ов", description = "Метод по созданию event-ов и отправки сообщений в кафку исходя из данных, переданных из goals-service /api/task/statuschange", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объекты в БД были созданы (см. сервис monitor) и удалось получить DTO представление этих объектов",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class)))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/taskstatuschange")
    public List<EventDto> taskStatusChange(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создания event-ов. Создаётся в goals-service в методе /api/task/statuschange")
        @RequestBody DataFromStatusChangeToRostalentStatusChange data) throws JsonProcessingException {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/taskstatuschange", null, data);
        return eventService.taskStatusChange(data);
    }

    @Operation(summary = "Метод по отправке сообщений в кафку о старте опроса", description = "Метод по отправке сообщений в кафку о старте опроса", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "В случае если удалось отправить сообщение в кафку"),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/quizstart")
    public OperationResult quizStart(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для создания сообщений для отправки в кафку")
        @RequestBody QuizStartInputDto data) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/quizstart", null, data);
        return eventService.quizStart(data);
    }

    @Operation(summary = "Метод по отправке сообщений в кафку о завершении прохождения опроса", description = "Метод по отправке сообщений в кафку о завершении прохождения опроса", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "В случае если удалось отправить сообщение в кафку"),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/quizfinish")
    public OperationResult quizFinish(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для создания сообщений для отправки в кафку")
        @RequestBody QuizFinishInputDto data) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/quizfinish", null, data);
        return eventService.quizFinish(data);
    }

    @Operation(summary = "Метод по отправке сообщений в кафку (кастомные уведомления)", description = "Метод по отправке сообщений в кафку (кастомные уведомления)", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "В случае если удалось отправить сообщение в кафку"),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/customemailevent")
    public OperationResult customEmailEvent(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для создания сообщений для отправки в кафку")
        @RequestBody CustomEmailEventInputDto data) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/customemailevent", null, data);
        return eventService.customEmailEvent(data);
    }

    @Operation(summary = "Метод по отправке сообщений в кафку, уведомление руководителя о возможности проведения испытательного срока", description = "Метод по отправке сообщений в кафку, уведомление руководителя о возможности проведения испытательного срока", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "В случае если удалось отправить сообщение в кафку"),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/onboardingresultshead")
    public OperationResult onboardingResultsHead(
        @RequestParam("division_team_assignment_id") List<Long> divisionTeamAssignmentIds) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/onboardingresultshead", Map.of("division_team_assignment_id", divisionTeamAssignmentIds), null);
        return eventService.onboardingResultsHead(divisionTeamAssignmentIds);
    }

    @Operation(summary = "Метод по отправке сообщений в кафку (кастомные уведомления)", description = "Метод по отправке сообщений в кафку (кастомные уведомления). Логика разная в зависимости от переданного кода", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "В случае если удалось отправить сообщение в кафку"),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/customemailevent_code")
    @SurProtected(operation = SurOperation.UNIT)
    public OperationResult customEmailEventCode(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для создания сообщений для отправки в кафку")
        @RequestBody CustomEmailEventCodeInputDto data) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/customemailevent_code", null, data);
        return eventService.customEmailEventCode(data);
    }

    @Operation(summary = "Метод по отправке сообщений в кафку для преемников", description = "Метод по отправке сообщений в кафку для преемников. Логика разная в зависимости от переданного кода", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "В случае если удалось отправить сообщение в кафку"),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/successornotificatebycode")
    @SurProtected(operation = SurOperation.UNIT)
    public OperationResult successorNotificateByCode(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для создания сообщений для отправки в кафку")
        @RequestBody SuccessorNotificateByCodeInputData data) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/successornotificatebycode", null, data);
        return eventService.successorNotificateByCode(data);
    }

    @Operation(summary = "Метод по отправке сообщений в кафку", description = "Метод по отправке сообщений в кафку. Логика разная в зависимости от переданного кода", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "В случае если удалось отправить сообщение в кафку"),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/basenotification")
    public OperationResult baseNotification(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, используемый для создания сообщений для отправки в кафку")
        @RequestBody BaseNotificationInputData data) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/basenotification", null, data);
        return eventService.baseNotification(data);
    }

    @Operation(summary = "Простая отправка уведомлений в кафку", description = "Простая отправка уведомлений в кафку", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "В случае если не возникло проблем и удалось отправить сообщение в кафку"),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
            content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/api/event/sendkafkamessage")
    public OperationResult sendKafkaMessage(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, хранящий данные для отправки сообщения в кафку")
        @RequestBody DataForKafkaMessageInputDto data) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/sendkafkamessage", null, data);
        return eventService.sendKafkaMessage(data);
    }

    @Operation(summary = "Метод по напоминанию руководителем о необходимости заполнить опрос", description = "Метод по напоминанию руководителем о необходимости заполнить опрос", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "В случае если не возникло проблем и удалось отправить сообщение в кафку"),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так.",
            content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/api/event/create/userpoll/remindbyhead")
    public OperationResult remindByHead(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO объект, хранящий данные для отправки сообщения в кафку")
        @RequestBody RemindByHeadInputDto data) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/userpoll/remindbyhead", null, data);
        return eventService.remindByHead(data);
    }

    @Operation(summary = "Создание уведомления для работника о том, что ему нужно подтвердить статус ротации", description = "Создание уведомления для работника о том, что ему нужно подтвердить статус ротации. Обязательные поля: \"author_name\", \"button_link\", \"division_team_assignment_id\"", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/updatedtarotation")
    public EventDto updateDtaRotation(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создания уведомления")
        @RequestBody EventInputDto input) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/updatedtarotation", null, input);
        return eventService.updateDtaRotation(input);
    }

    @Operation(summary = "Создание уведомления работника о том, что он актуализировал", description = "Создание уведомления работника о том, что он актуализировал. Обязательные поля: \"author_name\", \"division_team_assignment_id\"", tags = {"event"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "В случае если объект в БД был создан (см. сервис monitor) и удалось получить его DTO представление",
            content = @Content(schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "401", description = "В случае если запрос отправлен без токена или с недействительным токеном возвращается ошибка 401 и строка с ошибкой в теле запроса",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "В случае если что-то пошло не так (см. тело ответа с описанием ошибки)",
            content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/api/event/create/updatedtarotationcompleted")
    public EventDto updateDtaRotationCompleted(
        @io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "DTO объект, используемый для создания уведомления")
        @RequestBody EventInputDto input) {
        UUID hash = UUID.randomUUID();
        loggerService.createLog(hash, "POST /api/event/create/updatedtarotationcompleted", null, input);
        return eventService.updateDtaRotationCompleted(input);
    }

    private ShortEventDto formDevFormAchievementEvent(DevFormAchievementDto body) throws JsonProcessingException {
        List<DevelopmentFormNotificationDto> developmentFormList = rtCoreServiceClient.findActualDevelopmentForm();

        Map<String, String> devForm = new HashMap<>();

        for (var dev : developmentFormList) {
            if (dev.getId().equals(body.getDevelopmentFormId())) {
                devForm.put(NAME_TAG, dev.getName());
            }
        }
        if (body.getFields() != null) {
            TaskFieldDto field = body.getFields().stream()
                .filter(f -> Objects.equals(f.getType().getId(), 36L))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Field not found"));
            devForm.put("comment", field.getValue());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("DevelopmentForm", devForm);
        response.put(COMPETENCE_TAG, getCompetenceData(body.getCompetenceId()));

        return new ShortEventDto(null, body.getAssigmentId(), body.getAuthorName(), body.getAuthorImage(), OBJECT_MAPPER.writeValueAsString(response),
            body.getEventTypeId(), 1L, body.getProcessId());
    }

    private Map<String, String> buildMapOfTaskObject(TaskDto task, Boolean isKr) throws JsonProcessingException {
        //        Map<String, String> buildedTasks = buildTaskProgressObject(task);
        Map<String, String> response = buildTaskProgressObject(task, isKr);
        //        response.put(NAME_TAG, buildedTasks.get(NAME_TAG));
        //        if (buildedTasks.containsKey(PROGRESS_OLD_TAG)) {
        //            response.put(PROGRESS_OLD_TAG, buildedTasks.get(PROGRESS_OLD_TAG));
        //        }
        //        if (buildedTasks.containsKey(PROGRESS_TAG)) {
        //            response.put(PROGRESS_TAG, buildedTasks.get(PROGRESS_TAG));
        //        }

        String value = OBJECT_MAPPER.writeValueAsString(response);
        log.info("response: {}", value);
        return response;
    }

    private Map<String, String> getProgressAndProgressOld(TaskDto task) {

        Map<String, String> response = new HashMap<>();
        Date progressDate = new Date(0L);
        TaskFieldDto latestField = null;

        for (TaskFieldDto field : task.getFields()) {
            if (field.getType().getParams().equals(TextConstants.TYPE_PROGRESS)) {
                if (field.getDateTo() == null) {
                    response.put(PROGRESS_TAG, field.getValue());
                } else if (field.getDateTo().after(progressDate)) {
                    latestField = field;
                    progressDate = field.getDateTo();
                }
            }
        }
        if (latestField != null) {
            response.put(PROGRESS_OLD_TAG, latestField.getValue());
        } else {
            response.put(PROGRESS_OLD_TAG, "0");
        }

        return response;
    }

    private Map<String, String> buildTaskProgressObject(TaskDto task, Boolean isKr) {
        Map<String, String> goal = new LinkedHashMap<>();

        Date progressDate = new Date(0L);
        TaskFieldDto latestField = null;

        List<TaskFieldDto> fields = task.getFields().stream().sorted(Comparator.comparingLong(i -> i.getType().getId())).collect(Collectors.toList());

        if (isKr) {
            goal.put(ID_TAG, task.getId().toString());
        }

        for (TaskFieldDto field : fields) {
            if (isKr) {
                goal.put(TITLE_TAG, "\\");
                if ((task.getType().getId().equals(4L) && field.getType().getId().equals(14L)) ||
                    (task.getType().getId().equals(23L) && field.getType().getId().equals(142L))) {
                    goal.put(NAME_TAG, field.getValue());
                }
                if (task.getType().getId().equals(4L) && field.getType().getId().equals(1205L) ||
                    task.getType().getId().equals(23L) && field.getType().getId().equals(1206L)) {
                    goal.put(TITLE_TAG, field.getValue());
                }
            } else {
                if (task.getType().getId().equals(2L) && (field.getType().getId().equals(2L) ||
                    field.getType().getId().equals(3L) || field.getType().getId().equals(4L) || field.getType().getId().equals(6L))) {
                    if (!goal.containsKey(NAME_TAG)) {
                        goal.put(NAME_TAG, field.getValue());
                    } else {
                        goal.put(NAME_TAG, goal.get(NAME_TAG) + " " + field.getValue());
                    }
                }
                if (task.getType().getId().equals(3L) && (field.getType().getId().equals(9L) ||
                    field.getType().getId().equals(10L) || field.getType().getId().equals(12L))) {
                    if (!goal.containsKey(NAME_TAG)) {
                        goal.put(NAME_TAG, field.getValue());
                    } else {
                        goal.put(NAME_TAG, goal.get(NAME_TAG) + " " + field.getValue());
                    }
                }
            }
            if (field.getType().getParams().equals(TextConstants.TYPE_PROGRESS)) {
                if (field.getDateTo() == null) {
                    goal.put(PROGRESS_TAG, field.getValue());
                } else if (field.getDateTo().after(progressDate)) {
                    latestField = field;
                    progressDate = field.getDateTo();
                }
            }
        }
        if (latestField != null) {
            goal.put(PROGRESS_OLD_TAG, latestField.getValue());
        }

        return goal;
    }

    private List<Map<String, String>> getCompetenceData(List<Long> competenceIds) throws JsonProcessingException {
        List<Map<String, String>> competenceForm = new ArrayList<>();

        List<CompetenceNotificationDto> competenceList = competenceService.findCompetenceListByIds(competenceIds);

        for (var com : competenceList) {
            Map<String, String> competence = new HashMap<>();
            if (com.getCompetenceCatalogParams() != null) {
                Params params = OBJECT_MAPPER.readValue(com.getCompetenceCatalogParams(), Params.class);
                competence.put("color", params.getColor());
            }
            competence.put(NAME_TAG, com.getName());
            competenceForm.add(competence);
        }
        return competenceForm;
    }

    private TaskFieldDto getProgressField(TaskDto task) {
        return task.getFields().stream()
            .filter(x -> x.getType().getParams().equals(TextConstants.TYPE_PROGRESS))
            .findFirst()
            .orElse(null);
    }

    private String getName(TaskDto task) {
        List<TaskFieldDto> taskFieldsWithStringParam = task.getFields().stream()
            .filter(i -> Objects.isNull(i.getDateTo()))
            .filter(x -> x.getType().getParams().equals(TextConstants.TYPE_STRING))
            .filter(j -> j.getType().getIndex() != null && j.getType().getIndex() > 0)
            .sorted(Comparator.comparingLong(sortField -> sortField.getType().getIndex()))
            .collect(Collectors.toList());

        String result = "";

        for (TaskFieldDto tfd : taskFieldsWithStringParam) {
            result = result.concat(tfd.getValue() + " ");
        }

        return result.trim();
    }

}
