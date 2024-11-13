package me.goodt.vkpht.module.notification.application.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.goodt.drive.notify.application.configuration.AppConfig;
import com.goodt.drive.notify.application.configuration.NotificationConfig;
import me.goodt.vkpht.common.api.exception.NotFoundException;
import me.goodt.vkpht.module.notification.api.CompetenceService;
import me.goodt.vkpht.module.notification.api.EventService;
import me.goodt.vkpht.module.notification.api.KeycloakDataService;
import me.goodt.vkpht.module.notification.api.NotificationTemplateContentAttachmentService;
import me.goodt.vkpht.module.notification.api.NotificationTemplateService;
import me.goodt.vkpht.module.notification.api.SubscribeEmployeeToNotificationService;
import me.goodt.vkpht.module.notification.api.dto.AssignmentsAccordanceDto;
import me.goodt.vkpht.module.notification.api.dto.BaseNotificationInputData;
import me.goodt.vkpht.module.notification.api.dto.CustomEmailEventCodeInputDto;
import me.goodt.vkpht.module.notification.api.dto.CustomEmailEventInputDto;
import me.goodt.vkpht.module.notification.api.dto.DataForKafkaMessageInputDto;
import me.goodt.vkpht.module.notification.api.dto.EventBodyArgs;
import me.goodt.vkpht.module.notification.api.dto.EventInputDto;
import me.goodt.vkpht.module.notification.api.dto.NotificationTemplateContentDto;
import me.goodt.vkpht.module.notification.api.dto.QuizFinishInputDto;
import me.goodt.vkpht.module.notification.api.dto.QuizStartInputDto;
import me.goodt.vkpht.module.notification.api.dto.RemindByHeadInputDto;
import me.goodt.vkpht.module.notification.api.dto.StaticEmailRecipientDto;
import me.goodt.vkpht.module.notification.api.dto.SuccessorNotificateByCodeInputData;
import me.goodt.vkpht.module.notification.api.dto.data.DataFromStatusChangeToRostalentStatusChange;
import me.goodt.vkpht.module.notification.api.dto.data.OperationResult;
import me.goodt.vkpht.module.notification.api.dto.monitor.EventDto;
import me.goodt.vkpht.module.notification.api.dto.monitor.ShortEventDto;
import me.goodt.vkpht.module.notification.api.dto.quiz.PollRawDto;
import me.goodt.vkpht.module.notification.api.dto.quiz.UserPollDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.CompetenceNotificationDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskFieldDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskFindRequest;
import me.goodt.vkpht.module.notification.api.monitor.MonitorServiceClient;
import me.goodt.vkpht.module.notification.application.orgstructure.OrgstructureServiceAdapter;
import me.goodt.vkpht.module.notification.api.quiz.QuizServiceClient;
import me.goodt.vkpht.module.notification.api.tasksetting2.TasksettingServiceClient;
import me.goodt.vkpht.module.notification.application.NotificationService;
import me.goodt.vkpht.module.notification.application.TokenResolver;
import me.goodt.vkpht.module.notification.application.kafka.KafkaService;
import me.goodt.vkpht.module.notification.application.utils.TextConstants;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentAttachmentEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateContentEntity;
import me.goodt.vkpht.module.notification.domain.entity.NotificationTemplateEntity;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentRotationDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamRoleContainerDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorDto;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamSuccessorShortDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoDto;
import me.goodt.vkpht.module.orgstructure.api.dto.EmployeeInfoResponse;
import me.goodt.vkpht.module.orgstructure.api.dto.LegalEntityTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.PositionDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;

import static me.goodt.vkpht.module.notification.application.utils.DataUtils.getStringDateByPattern;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.EVENT_STATUS;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.EVENT_TYPE_ACCEPT_REQUEST;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.EVENT_TYPE_FOR_STATUS_CHANGE;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.EVENT_TYPE_FOR_UPDATE_DTA_ROTATION;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.EVENT_TYPE_FOR_UPDATE_DTA_ROTATION_COMPLETED;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.EVENT_TYPE_INCOMING_REQUEST;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.EVENT_TYPE_REJECT_ASSIGNMENT;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.EVENT_TYPE_REJECT_REQUEST;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.EVENT_TYPE_UPDATE_DIVISION_TEAM_ASSIGNMENT_ROTATION;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.EVENT_TYPE_UPDATE_DIVISION_TEAM_SUCCESSOR_READINESS;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.EVENT_TYPE_UPDATE_SUCCESSOR;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.HR_ROLE_LIST_LONG;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.ATTACHMENT_FILES;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.DATE_PATTERN_2;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.EVENT_ID_TO_EVENT_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.HIGH_PRIORITY;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.IS_ENABLED_FIELD_FALSE;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.NO_NOTIFICATION_TEMPLATE_CONTEXT;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.NO_RECIPIENTS;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TITLE;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    public static final String TOKEN_DELIMETER = "%";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String TITLE_EMPLOYEE_SENT_REQUEST = "Работник направил заявку о назначении эксперта на согласование.";
    private static final String TITLE_REJECTED_REQUEST = "Заявка о назначении эксперта отклонена.";
    private static final String TITLE_RECEIVED_REQUEST = "Поступил запрос на согласование заявки о назначении эксперта.";
    private static final String TITLE_ASSIGNED_EXPERT_TO_YOU = "Вам был назначен эксперт.";
    private static final String TITLE_YOU_IS_EXPERT = "Вы назначены экспертом.";
    private static final String TITLE_ASSIGNED_EXPERT_TO_EMPLOYEE = "Работнику был назначен эксперт.";
    private static final String TITLE_EMPLOYEE_IS_EXPERT = "Работник был назначен экспертом.";
    private static final String TITLE_UPDATE_DTA_ROTATION = "Подтвердите свой статус ротации.";
    private static final String TITLE_UPDATE_DTA_ROTATION_COMPLETED = "Статус ротации актуализирован.";
    private static final String BUTTON_NAME_ONE = "Одобрить";
    private static final String BUTTON_NAME_TWO = "Отклонить";
    private static final String MESSAGE_NOTICE_UPDATE_DIVISION_TEAM_SUCCESSOR_READINESS = "Актуализируйте готовность преемника %s на позицию %s.";
    private static final String SUBJECT_NOTICE_UPDATE_DIVISION_TEAM_SUCCESSOR_READINESS = "Напоминание об актуализации готовности преемника";
    private static final String MESSAGE_NOTICE_UPDATE_DIVISION_TEAM_ASSIGNMENT_ROTATION = "Пожалуйста, актуализируйте свой статус ротации";
    private static final String SUBJECT_NOTICE_UPDATE_DIVISION_TEAM_ASSIGNMENT_ROTATION = "Напоминание об актуализации своего статуса ротации";
    private static final String MESSAGE_NOTICE_UPDATE_SUCCESSOR = "На позицию %s был назначен новый преемник - %s.";
    private static final String SUBJECT_NOTICE_UPDATE_SUCCESSOR = "Напоминание о назначении нового преемника";
    private static final String BODY_COMMENT_FOR_EMPLOYEE_602 = "Руководитель готов подвести итоги испытательного срока. Пожалуйста, запланируйте встречу для обсуждения и обмена обратной связью.";
    private static final String BODY_COMMENT_FOR_EMPLOYEE_604 = "Поздравляем! Ваша адаптация успешно завершена. Надеемся, что Вы теперь знаете, что необходимо для того, чтобы обеспечивать умный результат. И надеемся, что чувствуете себя частью сплоченной команды. Если мы можем сделать еще что-то для Вашей адаптации или совершенствования процедуры в целом, пожалуйста, пройдите короткий опрос.";
    private static final String BODY_COMMENT_FOR_EMPLOYEE_605 = "К сожалению, вы не прошли испытательный срок. Обратитесь к руководителю за обратной связью.";
    private static final String BODY_COMMENT_FOR_EMPLOYEE_624 = "Руководитель пока не готов подтвердить окончание срока адаптации. Узнайте, как он может Вас поддержать.";
    private static final String BODY_COMMENT_FOR_HEAD_EMPLOYEE_603 = "Ваш сотрудник %s отметил готовность к самостоятельной работе. Теперь вы можете подвести итоги его адаптации.";
    private static final String BODY_COMMENT_FOR_HEAD_EMPLOYEE_604 = "Поздравляем! Ваш сотрудник %s завершил адаптацию. Поделитесь вашими впечатлениями о процессе адаптации и пройдите короткий опрос.";
    private static final String BODY_COMMENT_FOR_BUDDY_EMPLOYEE_602 = "Руководитель готов подвести итоги испытательного срока по сотруднику %s, для которого вы являетесь бадди. Пожалуйста, запланируйте встречу для обсуждения и обмена обратной связью.";
    private static final String BODY_COMMENT_FOR_BUDDY_EMPLOYEE_603 = "Сотрудник %s, для которого вы являетесь бадди, отметил готовность к самостоятельной работе.";
    private static final String BODY_COMMENT_FOR_BUDDY_EMPLOYEE_604 = "Поздравляем! Ваш новый сотрудник %s завершил адаптацию. Поделитесь вашими впечатлениями о процессе адаптациии и пройдите короткий опрос.";
    private static final String BODY_COMMENT_FOR_BUDDY_EMPLOYEE_605 = "Ваш новый сотрудник %s не прошел испытательный срок.";
    private static final String BODY_COMMENT_FOR_BUDDY_EMPLOYEE_624 = "Руководитель пока не готов подтвердить окончание срока адаптации вашего нового сотрудника %s Свяжитесь с руководителем и узнайте, как вы можете поддержать нового сотрудника.";
    private static final String BODY_COMMENT_FOR_HR_EMPLOYEE_602 = "Руководитель подтвердил прохождение испытательного срока новым сотрудником %s";
    private static final String BODY_COMMENT_FOR_HR_EMPLOYEE_605 = "Руководитель не подтвердил прохождение испытательного срока новым сотрудником %s Пожалуйста, запланируйте встречу для обсуждения подготовки кадровых мероприятий и обмена обратной связью.";
    private static final String AUTHOR_NAME = "Система";
    private static final String TITLE_PROBATION = "Ваш испытательный срок";
    private static final String TITLE_ADAPTATION = "Ваша адаптация";
    private static final String TITLE_NEW_EMPLOYEE_ADAPTATION = "Адаптация нового сотрудника";
    private static final String TITLE_NEW_EMPLOYEE_PROBATION = "Испытательный срок нового сотрудника";
    private static final String COMMENT_OF_EMPLOYEE = "Комментарий сотрудника:";
    private static final String COMMENT_OF_HEAD = "Комментарий руководителя:";
    private static final String MESSAGE_FOR_RECEIVER_QUIZ_START_EMPLOYEE = "Уважаемый(ая) %s Пройдите короткий опрос \"%s\" по процессу адаптации. Это займет не более 5 минут. Результаты опроса будут учитываться для улучшения процесса. Спасибо!";
    private static final String MESSAGE_FOR_RECEIVER_QUIZ_START = "Пройдите короткий опрос \"%s\" по процессу адаптации нового сотрудника %s Это займет не более 5 минут. Результаты опроса будут учитываться для улучшения процесса. Спасибо!";
    private static final String MESSAGE_FOR_RECEIVER_QUIZ_FINISH = "Ваш сотрудник %s завершил прохождение опроса по процессу адаптации.";
    private static final String MESSAGE_ONBOARDING_RESULT = "Подошло время подвести итоги прохождения испытательного срока сотрудника %s";
    private static final Set<Long> POLL_ID_SET_SIMPLE = Set.of(2L, 3L, 4L);
    private static final Set<Long> POLL_ID_SET_FULL = Set.of(2L, 3L, 4L, 5L, 6L, 7L, 8L);
    private static final Integer MESSAGE_TYPE_1 = 1;
    private static final Integer MESSAGE_TYPE_2 = 2;
    private static final Integer MESSAGE_TYPE_3 = 3;
    private static final List<Integer> MESSAGE_TYPE_LIST = List.of(MESSAGE_TYPE_1, MESSAGE_TYPE_2, MESSAGE_TYPE_3);
    private static final String S_S_FOR_STRING_FORMAT = "%s %s";
    private static final String COMPETENCE_NAME = "%competence_name";
    private static final String ROOT_URL = "%root_url";
    private static final String FIO = "%FIO";
    private static final String EMPLOYEE_IN_POSITION_FIO_BY_DIVISION_TEAM_SUCCESSOR_ID = "%employee_in_position_fio_by_division_team_successor_id";
    private static final String EMPLOYEE_SUCCESSOR_FIO = "%employee_successor_fio";
    private static final String POSITION_FULL_NAME_BY_DIVISION_TEAM_SUCCESSOR_ID = "%position_full_name_by_division_team_successor_id";
    private static final String EMPLOYEE_FIO = "$employee.fio";
    private static final String REMIND_BY_HEAD_MESSAGE = "Напоминаем о необходимости пройти \"%s\" в срок до %s.";
    private static final String REMIND_BY_HEAD_SUBJECT = "Напоминание о необходимости пройти опрос";
    private static final String NO_COMMENT = "комментарий отсутствует";
    private static final String EVENTCREATE = "eventcreate";
    private static final String EVENTTYPE = "eventtype";
    private static String html = "<body><div><br><a href=\"$root_url\">Перейти на портал</a></div></body>";
    private final OrgstructureServiceAdapter orgstructureServiceAdapter;
    private final MonitorServiceClient monitorServiceClient;
    private final TasksettingServiceClient tasksettingServiceClient;
    private final CompetenceService competenceService;
    private final KafkaService kafkaService;
    private final QuizServiceClient quizServiceClient;
    private final NotificationConfig notificationConfig;
    private final NotificationTemplateService notificationTemplateService;
    private final KeycloakDataService keycloakDataService;
    private final AppConfig appConfig;
    private final NotificationService notificationService;
    private final TokenResolver tokenResolver;
    private final ResolverServiceContainer resolverServiceContainer;
    private final SubscribeEmployeeToNotificationService subscribeEmployeeToNotificationService;
    private final NotificationTemplateContentAttachmentService notificationTemplateContentAttachmentService;

    private static String createCodeForEventCreateEventType(Long eventTypeId) {
        return "%s_%s_%d".formatted(EVENTCREATE, EVENTTYPE, eventTypeId);
    }

    /**
     * @deprecated sonar: Unused "private" methods should be removed
     */
    @Deprecated(forRemoval = false)
    private static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    private static Date subtractDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

    private static String getNameShortFIO(EmployeeInfoDto employeeInfoDto) {
        return String.format("%s %s.%s.", employeeInfoDto.getLastName(), employeeInfoDto.getFirstName().charAt(0), employeeInfoDto.getMiddleName().charAt(0));
    }

    private static String getFioByAssignmentForNotification(DivisionTeamAssignmentDto assignment) {
        EmployeeInfoDto employee = assignment.getEmployee();
        return String.format("%s %s.%s.", employee.getLastName(), employee.getFirstName().charAt(0), employee.getMiddleName().charAt(0));
    }

    private static JSONObject replaceTokens(JSONObject content, ReplaceItem... replaceItems) {
        String result = replaceTokens(content.toString(), replaceItems);
        return new JSONObject(result);
    }

    private static JSONObject replaceTokens(JSONObject content, List<ReplaceItem> replaceItems) {
        String result = replaceTokens(content.toString(), replaceItems.toArray(new ReplaceItem[]{}));
        return new JSONObject(result);
    }

    private static String replaceTokens(String body, ReplaceItem... replaceItems) {
        StringBuilder builder = new StringBuilder(body);
        for (ReplaceItem item : replaceItems) {
            int start = builder.indexOf(item.getOldString());
            while (start != -1) {
                builder.replace(
                    start,
                    start + item.getOldString().length(),
                    Objects.isNull(item.getNewString()) ? item.getOldString() : item.getNewString()
                );
                start += Objects.isNull(item.getNewString()) ? item.getOldString().length() : item.getNewString().length();
                start = builder.indexOf(item.getOldString(), start);
            }
        }
        return builder.toString();
    }

    @PostConstruct
    public void init() {
        html = html.replace("$root_url", notificationConfig.getRootUrl());
    }

    @Override
    public EventDto expertTaskCreate(EventInputDto input) throws JsonProcessingException {
        TaskDto task = tasksettingServiceClient.getTaskById(input.getTaskId());
        DivisionTeamAssignmentDto taskOwnerAssignment = getAssignment(task.getUserId());
        Long taskOwnerHeadAssignmentId = getHeadAssignmentId(taskOwnerAssignment);
        DivisionTeamAssignmentDto employeeAssignment = getAssignment(input.getEmployeeAssignmentId());
        DivisionTeamAssignmentDto expertAssignment = getAssignment(input.getExpertAssignmentId());

        String body = getBody(EventBodyArgs.builder()
            .bodyTitle(TITLE_EMPLOYEE_SENT_REQUEST)
            .employeeAssignment(employeeAssignment)
            .expertAssignment(expertAssignment)
            .eventType(EVENT_TYPE_INCOMING_REQUEST)
            .buttonLinkOne(input.getButtonLinkOne())
            .buttonLinkTwo(input.getButtonLinkTwo())
            .taskId(input.getTaskId())
            .build());

        ShortEventDto shortEvent = new ShortEventDto(null, taskOwnerHeadAssignmentId, input.getAuthorName(),
            null, body, EVENT_TYPE_INCOMING_REQUEST, EVENT_STATUS, null);
        return monitorServiceClient.createEvent(shortEvent);
    }

    @Override
    public EventDto employeeHeadExpertReject(EventInputDto input) throws JsonProcessingException {
        DivisionTeamAssignmentDto employeeAssignment = getAssignment(input.getEmployeeAssignmentId());
        DivisionTeamAssignmentDto expertAssignment = getAssignment(input.getExpertAssignmentId());

        String body = getBody(EventBodyArgs.builder()
            .bodyTitle(TITLE_REJECTED_REQUEST)
            .employeeAssignment(employeeAssignment)
            .expertAssignment(expertAssignment)
            .eventType(EVENT_TYPE_REJECT_REQUEST)
            .userId(input.getEmployeeAssignmentId())
            .build());

        ShortEventDto shortEvent = new ShortEventDto(null, input.getEmployeeAssignmentId(), input.getAuthorName(),
            null, body, EVENT_TYPE_REJECT_REQUEST, EVENT_STATUS, null);
        return monitorServiceClient.createEvent(shortEvent);
    }

    @Override
    public EventDto expertHeadNotice(EventInputDto input) throws JsonProcessingException {
        DivisionTeamAssignmentDto employeeAssignment = getAssignment(input.getEmployeeAssignmentId());
        DivisionTeamAssignmentDto expertAssignment = getAssignment(input.getExpertAssignmentId());
        Long expertHeadAssignmentId = getHeadAssignmentId(expertAssignment);

        String body = getBody(EventBodyArgs.builder()
            .bodyTitle(TITLE_RECEIVED_REQUEST)
            .employeeAssignment(employeeAssignment)
            .expertAssignment(expertAssignment)
            .eventType(EVENT_TYPE_INCOMING_REQUEST)
            .buttonLinkOne(input.getButtonLinkOne())
            .buttonLinkTwo(input.getButtonLinkTwo())
            .taskId(input.getTaskId())
            .build());

        ShortEventDto shortEvent = new ShortEventDto(null, expertHeadAssignmentId, input.getAuthorName(),
            null, body, EVENT_TYPE_INCOMING_REQUEST, EVENT_STATUS, null);
        return monitorServiceClient.createEvent(shortEvent);
    }

    @Override
    public List<EventDto> expertHeadReject(EventInputDto input) throws JsonProcessingException {
        List<EventDto> events = new ArrayList<>();
        DivisionTeamAssignmentDto employeeAssignment = getAssignment(input.getEmployeeAssignmentId());
        Long employeeHeadAssignmentId = getHeadAssignmentId(employeeAssignment);
        DivisionTeamAssignmentDto expertAssignment = getAssignment(input.getExpertAssignmentId());

        String body = getBody(EventBodyArgs.builder()
            .bodyTitle(TITLE_REJECTED_REQUEST)
            .employeeAssignment(employeeAssignment)
            .expertAssignment(expertAssignment)
            .eventType(EVENT_TYPE_REJECT_REQUEST)
            .userId(input.getEmployeeAssignmentId())
            .build());

        ShortEventDto shortEventForEmployee = new ShortEventDto(null, input.getEmployeeAssignmentId(), input.getAuthorName(),
            null, body, EVENT_TYPE_REJECT_REQUEST, EVENT_STATUS, null);
        ShortEventDto shortEventForEmployeeHead = new ShortEventDto(null, employeeHeadAssignmentId, input.getAuthorName(),
            null, body, EVENT_TYPE_REJECT_REQUEST, EVENT_STATUS, null);
        events.add(monitorServiceClient.createEvent(shortEventForEmployee));
        events.add(monitorServiceClient.createEvent(shortEventForEmployeeHead));
        return events;
    }

    @Override
    public List<EventDto> hrExpertReject(EventInputDto input) throws JsonProcessingException {
        List<EventDto> events = new ArrayList<>();
        DivisionTeamAssignmentDto employeeAssignment = getAssignment(input.getEmployeeAssignmentId());
        Long employeeHeadAssignmentId = getHeadAssignmentId(employeeAssignment);
        DivisionTeamAssignmentDto expertAssignment = getAssignment(input.getExpertAssignmentId());
        Long expertHeadAssignmentId = getHeadAssignmentId(expertAssignment);

        String body = getBody(EventBodyArgs.builder()
            .bodyTitle(TITLE_REJECTED_REQUEST)
            .employeeAssignment(employeeAssignment)
            .expertAssignment(expertAssignment)
            .eventType(EVENT_TYPE_REJECT_ASSIGNMENT)
            .userId(input.getEmployeeAssignmentId())
            .build());

        ShortEventDto shortEventForEmployee = new ShortEventDto(null, input.getEmployeeAssignmentId(), input.getAuthorName(),
            null, body, EVENT_TYPE_REJECT_ASSIGNMENT, EVENT_STATUS, null);
        ShortEventDto shortEventForEmployeeHead = new ShortEventDto(null, employeeHeadAssignmentId, input.getAuthorName(),
            null, body, EVENT_TYPE_REJECT_ASSIGNMENT, EVENT_STATUS, null);
        events.add(monitorServiceClient.createEvent(shortEventForEmployee));
        events.add(monitorServiceClient.createEvent(shortEventForEmployeeHead));
        if (!employeeHeadAssignmentId.equals(expertHeadAssignmentId)) {
            ShortEventDto shortEventForExpertHead = new ShortEventDto(null, expertHeadAssignmentId, input.getAuthorName(),
                null, body, EVENT_TYPE_REJECT_ASSIGNMENT, EVENT_STATUS, null);
            events.add(monitorServiceClient.createEvent(shortEventForExpertHead));
        }
        return events;
    }

    @Override
    public List<EventDto> hrExpertAccept(EventInputDto input) throws JsonProcessingException {
        List<EventDto> events = new ArrayList<>();
        DivisionTeamAssignmentDto employeeAssignment = getAssignment(input.getEmployeeAssignmentId());
        Long employeeHeadAssignmentId = getHeadAssignmentId(employeeAssignment);
        DivisionTeamAssignmentDto expertAssignment = getAssignment(input.getExpertAssignmentId());
        Long expertHeadAssignmentId = getHeadAssignmentId(expertAssignment);

        String bodyForEmployee = getBody(EventBodyArgs.builder()
            .bodyTitle(TITLE_ASSIGNED_EXPERT_TO_YOU)
            .employeeAssignment(employeeAssignment)
            .expertAssignment(expertAssignment)
            .eventType(EVENT_TYPE_ACCEPT_REQUEST)
            .build());

        String bodyForExpert = getBody(EventBodyArgs.builder()
            .bodyTitle(TITLE_YOU_IS_EXPERT)
            .employeeAssignment(employeeAssignment)
            .expertAssignment(expertAssignment)
            .eventType(EVENT_TYPE_ACCEPT_REQUEST)
            .build());

        String bodyForEmployeeHead = getBody(EventBodyArgs.builder()
            .bodyTitle(TITLE_ASSIGNED_EXPERT_TO_EMPLOYEE)
            .employeeAssignment(employeeAssignment)
            .expertAssignment(expertAssignment)
            .eventType(EVENT_TYPE_ACCEPT_REQUEST)
            .build());

        ShortEventDto shortEventForEmployee = new ShortEventDto(null, input.getEmployeeAssignmentId(), input.getAuthorName(),
            null, bodyForEmployee, EVENT_TYPE_ACCEPT_REQUEST, EVENT_STATUS, null);
        ShortEventDto shortEventForExpert = new ShortEventDto(null, input.getExpertAssignmentId(), input.getAuthorName(),
            null, bodyForExpert, EVENT_TYPE_ACCEPT_REQUEST, EVENT_STATUS, null);
        ShortEventDto shortEventForEmployeeHead = new ShortEventDto(null, employeeHeadAssignmentId, input.getAuthorName(),
            null, bodyForEmployeeHead, EVENT_TYPE_ACCEPT_REQUEST, EVENT_STATUS, null);

        events.add(monitorServiceClient.createEvent(shortEventForEmployee));
        events.add(monitorServiceClient.createEvent(shortEventForExpert));
        events.add(monitorServiceClient.createEvent(shortEventForEmployeeHead));

        if (!employeeHeadAssignmentId.equals(expertHeadAssignmentId)) {
            String bodyForExpertHead = getBody(EventBodyArgs.builder()
                .bodyTitle(TITLE_EMPLOYEE_IS_EXPERT)
                .employeeAssignment(employeeAssignment)
                .expertAssignment(expertAssignment)
                .eventType(EVENT_TYPE_ACCEPT_REQUEST)
                .build());

            ShortEventDto shortEventForExpertHead = new ShortEventDto(null, expertHeadAssignmentId, input.getAuthorName(),
                null, bodyForExpertHead, EVENT_TYPE_ACCEPT_REQUEST, EVENT_STATUS, null);
            events.add(monitorServiceClient.createEvent(shortEventForExpertHead));
        }
        return events;
    }

    @Override
    public List<EventDto> hrExpertNotice(EventInputDto input) throws JsonProcessingException {
        Set<Long> hrAssignmentIds = new HashSet<>();
        List<EventDto> eventDtoList = new ArrayList<>();

        TaskDto task = tasksettingServiceClient.getTaskById(input.getTaskId());
        DivisionTeamAssignmentDto taskOwnerAssignment = getAssignment(task.getUserId());
        Long taskOwnerLegalEntityId = getDivision(taskOwnerAssignment).getLegalEntityId();

        List<LegalEntityTeamAssignmentDto> legalEntityTeamAssignments = orgstructureServiceAdapter.getLegalEntityAssignments(Collections.singletonList(taskOwnerLegalEntityId), HR_ROLE_LIST_LONG, null);
        if (legalEntityTeamAssignments != null && !legalEntityTeamAssignments.isEmpty()) {
            hrAssignmentIds.addAll(legalEntityTeamAssignments.stream().map(LegalEntityTeamAssignmentDto::getId).collect(Collectors.toSet()));
        }

        DivisionTeamAssignmentDto employeeAssignment = getAssignment(input.getEmployeeAssignmentId());
        DivisionTeamAssignmentDto expertAssignment = getAssignment(input.getExpertAssignmentId());

        String body = getBody(EventBodyArgs.builder()
            .bodyTitle(TITLE_RECEIVED_REQUEST)
            .expertAssignment(expertAssignment)
            .employeeAssignment(employeeAssignment)
            .eventType(EVENT_TYPE_INCOMING_REQUEST)
            .buttonLinkOne(input.getButtonLinkOne())
            .buttonLinkTwo(input.getButtonLinkTwo())
            .taskId(input.getTaskId())
            .build());

        for (Long hrAssignmentId : hrAssignmentIds) {
            ShortEventDto shortEvent = new ShortEventDto(null, hrAssignmentId, input.getAuthorName(), null, body, EVENT_TYPE_INCOMING_REQUEST, EVENT_STATUS, null);
            EventDto eventDto = monitorServiceClient.createEvent(shortEvent);
            eventDtoList.add(eventDto);
        }
        return eventDtoList;
    }

    @Override
    public List<EventDto> updateDivisionTeamSuccessorReadiness(EventInputDto input) {
        List<EventDto> events = new ArrayList<>();
        Date date = subtractDays(new Date(), input.getUpdateRequiredDays());
        List<DivisionTeamRoleContainerDto> teamRoleContainerList = orgstructureServiceAdapter.findDivisionTeamRoles(null, date, null, null, null, null);
        if (!teamRoleContainerList.isEmpty()) {
            Set<DivisionTeamAssignmentDto> headTeamAssignments = getHeadTeamAssignmentsFromTeamRoleContainerList(teamRoleContainerList);

            headTeamAssignments.forEach(headTeamAssignment -> teamRoleContainerList.stream()
                .filter(teamRoleContainer -> teamRoleContainer.getDivisionTeam().getId().equals(headTeamAssignment.getDivisionTeamRole().getDivisionTeam().getId()))
                .forEach(teamRoleContainer -> {
                    DivisionTeamAssignmentShortDto assignment = teamRoleContainer.getDivisionTeamAssignmentDtos().get(0);
                    DivisionTeamSuccessorShortDto successor = teamRoleContainer.getDivisionTeamSuccessorDtos().get(0);

                    Map<String, Object> mapBody = new LinkedHashMap<>();
                    mapBody.put(TextConstants.TITLE_CAPITAL, String.format("Актуализируйте готовность преемника на позицию %s", assignment.getFullName()));
                    mapBody.put(TextConstants.EMPLOYEE_ID, headTeamAssignment.getEmployee().getId());

                    DivisionTeamAssignmentDto successorAssignment = orgstructureServiceAdapter
                        .getAssignments(null, Collections.singletonList(successor.getEmployee().getId())).get(0);
                    List<AssignmentsAccordanceDto> assignmentsAccordanceDtoList = competenceService
                        .assignmentAccordance(new Long[]{assignment.getId()}, new String[]{String.valueOf(successorAssignment.getId())});

                    Map<String, Object> successorCard = new LinkedHashMap<>();
                    successorCard.put(TextConstants.PHOTO, successor.getEmployee().getPhoto());
                    successorCard.put("name", getNameShortFIO(successor.getEmployee()));
                    successorCard.put("competence_percent", assignmentsAccordanceDtoList.getFirst().getPercent());
                    successorCard.put("date", getStringDateByPattern(DATE_PATTERN_2, successor.getDivisionTeamSuccessorReadiness().get(0).getDateFrom()));
                    successorCard.put("readiness", successor.getDivisionTeamSuccessorReadiness().get(0).getAssignmentReadiness().getName());

                    Map<String, Object> button = new LinkedHashMap<>();
                    button.put(TextConstants.TITLE_CAPITAL, "ВЫБОР ПРЕЕМНИКОВ");
                    button.put("link", input.getButtonLink());

                    mapBody.put("successor_card", successorCard);
                    mapBody.put(TextConstants.BUTTON, button);

                    String body = null;
                    try {
                        body = OBJECT_MAPPER.writeValueAsString(mapBody);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    ShortEventDto shortEvent = new ShortEventDto(null, headTeamAssignment.getId(), input.getAuthorName(),
                        null, body, EVENT_TYPE_UPDATE_DIVISION_TEAM_SUCCESSOR_READINESS, EVENT_STATUS, null);
                    events.add(monitorServiceClient.createEvent(shortEvent));

                    String message = String.format(MESSAGE_NOTICE_UPDATE_DIVISION_TEAM_SUCCESSOR_READINESS,
                        getNameShortFIO(successor.getEmployee()), assignment.getFullName());

                    sendKafkaMessageWithoutCode(SUBJECT_NOTICE_UPDATE_DIVISION_TEAM_SUCCESSOR_READINESS, message, Collections.singletonList(headTeamAssignment.getEmployee().getId()));
                })
            );
        }
        return events;
    }

    @Override
    public List<EventDto> updateDivisionTeamAssignmentRotation(EventInputDto input) throws JsonProcessingException {
        List<EventDto> events = new ArrayList<>();
        if (input.getEmployeeAssignmentId() != null) {
            DivisionTeamAssignmentDto assignment = orgstructureServiceAdapter.getAssignments(Collections.singletonList(input.getEmployeeAssignmentId()), null).get(0);
            DivisionTeamAssignmentRotationDto divisionTeamAssignmentRotation = assignment.getDivisionTeamAssignmentRotations().get(0);
            String body = getBodyForUpdateRotations(divisionTeamAssignmentRotation.getRotation().getName(),
                divisionTeamAssignmentRotation.getDateFrom(),
                assignment.getId(),
                input.getButtonLinkEdit(), input.getButtonLinkUpdate());
            ShortEventDto shortEvent = new ShortEventDto(null, assignment.getId(), input.getAuthorName(),
                null, body, EVENT_TYPE_UPDATE_DIVISION_TEAM_ASSIGNMENT_ROTATION, EVENT_STATUS, null);
            events.add(monitorServiceClient.createEvent(shortEvent));

            sendKafkaMessageWithoutCode(SUBJECT_NOTICE_UPDATE_DIVISION_TEAM_ASSIGNMENT_ROTATION, MESSAGE_NOTICE_UPDATE_DIVISION_TEAM_ASSIGNMENT_ROTATION, Collections.singletonList(assignment.getEmployee().getId()));

            return events;
        }
        Date date = subtractDays(new Date(), input.getUpdateRequiredDays());
        List<DivisionTeamRoleContainerDto> teamRoles = orgstructureServiceAdapter.findDivisionTeamRoles(null, null, null, date, null, null);
        if (!teamRoles.isEmpty()) {
            teamRoles.forEach(teamRole -> {
                DivisionTeamAssignmentShortDto assignment = teamRole.getDivisionTeamAssignmentDtos().get(0);
                DivisionTeamAssignmentRotationDto divisionTeamAssignmentRotation = assignment.getDivisionTeamAssignmentRotations().get(0);
                String body = null;
                try {
                    body = getBodyForUpdateRotations(divisionTeamAssignmentRotation.getRotation().getName(),
                        divisionTeamAssignmentRotation.getDateFrom(),
                        assignment.getId(),
                        input.getButtonLinkEdit(), input.getButtonLinkUpdate());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                ShortEventDto shortEvent = new ShortEventDto(null, assignment.getId(), input.getAuthorName(),
                    null, body, EVENT_TYPE_UPDATE_DIVISION_TEAM_ASSIGNMENT_ROTATION, EVENT_STATUS, null);
                events.add(monitorServiceClient.createEvent(shortEvent));

                sendKafkaMessageWithoutCode(SUBJECT_NOTICE_UPDATE_DIVISION_TEAM_ASSIGNMENT_ROTATION, MESSAGE_NOTICE_UPDATE_DIVISION_TEAM_ASSIGNMENT_ROTATION, Collections.singletonList(assignment.getEmployee().getId()));
            });
        }
        return events;
    }

    @Override
    public List<EventDto> updateSuccessor(EventInputDto input) {
        List<EventDto> events = new ArrayList<>();
        Date date = new Date();

        List<DivisionTeamRoleContainerDto> teamRoleContainerList = orgstructureServiceAdapter.findDivisionTeamRoles(null, null, null, null, date, null);
        if (!teamRoleContainerList.isEmpty()) {
            Set<DivisionTeamAssignmentDto> headTeamAssignments = getHeadTeamAssignmentsFromTeamRoleContainerList(teamRoleContainerList);

            headTeamAssignments.forEach(headTeamAssignment -> teamRoleContainerList.stream()
                .filter(teamRoleContainer -> teamRoleContainer.getDivisionTeam().getId().equals(headTeamAssignment.getDivisionTeamRole().getDivisionTeam().getId()))
                .forEach(teamRoleContainer -> {
                    DivisionTeamAssignmentShortDto assignment = teamRoleContainer.getDivisionTeamAssignmentDtos().get(0);
                    DivisionTeamSuccessorShortDto successor = teamRoleContainer.getDivisionTeamSuccessorDtos().get(0);
                    Map<String, Object> mapBody = new LinkedHashMap<>();
                    mapBody.put(TextConstants.TITLE_CAPITAL, String.format("На позицию %s был назначен новый преемник", assignment.getFullName()));
                    mapBody.put(TextConstants.EMPLOYEE_ID, headTeamAssignment.getEmployee().getId());
                    Map<String, Object> successorCard = new LinkedHashMap<>();
                    successorCard.put(TextConstants.PHOTO, successor.getEmployee().getPhoto());
                    successorCard.put("name", getNameShortFIO(successor.getEmployee()));
                    DivisionTeamAssignmentDto successorAssignment = orgstructureServiceAdapter.getAssignments(null, Collections.singletonList(successor.getEmployee().getId())).get(0);
                    List<AssignmentsAccordanceDto> assignmentsAccordanceDtoList = competenceService.assignmentAccordance(new Long[]{assignment.getId()}, new String[]{String.valueOf(successorAssignment.getId())});
                    successorCard.put("competence_percent", assignmentsAccordanceDtoList.getFirst().getPercent());
                    successorCard.put("date", getStringDateByPattern(DATE_PATTERN_2, successor.getDivisionTeamSuccessorReadiness().get(0).getDateFrom()));
                    successorCard.put("readiness", successor.getDivisionTeamSuccessorReadiness().get(0).getAssignmentReadiness().getName());
                    Map<String, Object> button = new LinkedHashMap<>();
                    button.put(TITLE, "ВЫБОР ПРЕЕМНИКОВ");
                    button.put("link", input.getButtonLink());
                    mapBody.put("successor_card", successorCard);
                    mapBody.put(TextConstants.BUTTON, button);
                    String body = null;
                    try {
                        body = OBJECT_MAPPER.writeValueAsString(mapBody);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    ShortEventDto shortEvent = new ShortEventDto(null, headTeamAssignment.getId(), input.getAuthorName(),
                        null, body, EVENT_TYPE_UPDATE_SUCCESSOR, EVENT_STATUS, null);
                    events.add(monitorServiceClient.createEvent(shortEvent));

                    String message = String.format(MESSAGE_NOTICE_UPDATE_SUCCESSOR,
                        assignment.getFullName(), getNameShortFIO(successor.getEmployee()));

                    sendKafkaMessageWithoutCode(SUBJECT_NOTICE_UPDATE_SUCCESSOR, message, Collections.singletonList(headTeamAssignment.getEmployee().getId()));
                })
            );
        }
        return events;
    }

    @Override
    public void noticeUpdateDivisionTeamSuccessorReadiness() {
        Date date = subtractDays(new Date(), 365);
        List<DivisionTeamRoleContainerDto> teamRoleContainerList = orgstructureServiceAdapter.findDivisionTeamRoles(null, date, null, null, null, null);
        if (!teamRoleContainerList.isEmpty()) {
            Set<DivisionTeamAssignmentDto> headTeamAssignments = getHeadTeamAssignmentsFromTeamRoleContainerList(teamRoleContainerList);

            headTeamAssignments.forEach(headTeamAssignment -> teamRoleContainerList.stream()
                .filter(teamRole -> teamRole.getDivisionTeam().getId().equals(headTeamAssignment.getDivisionTeamRole().getDivisionTeam().getId()))
                .forEach(teamRole -> {
                    DivisionTeamAssignmentShortDto assignment = teamRole.getDivisionTeamAssignmentDtos().get(0);
                    DivisionTeamSuccessorShortDto successor = teamRole.getDivisionTeamSuccessorDtos().get(0);
                    String message = String.format(MESSAGE_NOTICE_UPDATE_DIVISION_TEAM_SUCCESSOR_READINESS, getNameShortFIO(successor.getEmployee()), assignment.getFullName());

                    sendKafkaMessageWithoutCode(SUBJECT_NOTICE_UPDATE_DIVISION_TEAM_SUCCESSOR_READINESS, message, Collections.singletonList(headTeamAssignment.getEmployee().getId()));
                }));
        }
    }

    @Override
    public List<EventDto> taskStatusChange(DataFromStatusChangeToRostalentStatusChange data) throws JsonProcessingException {
        List<EventDto> events = new ArrayList<>();
        if (data.getUserId() != null) {
            ShortEventDto shortEvent = getShortEventWithCommonFields();
            shortEvent.setAssignmentId(data.getUserId());
            shortEvent.setAuthorName(AUTHOR_NAME);
            if (data.getStatusId().equals(602L)) {
                shortEvent.setBody(getBodyForTaskStatusChangeWithComment(data.getEmployeeId(), data.getEmployeeDivisionTeamId(), TITLE_PROBATION, BODY_COMMENT_FOR_EMPLOYEE_602, S_S_FOR_STRING_FORMAT.formatted(COMMENT_OF_HEAD, data.getComment() != null ? data.getComment() : NO_COMMENT)));
                events.add(monitorServiceClient.createEvent(shortEvent));
                sendKafkaMessageWithoutCode(TITLE_PROBATION, makeMessageForKafka(BODY_COMMENT_FOR_EMPLOYEE_602, S_S_FOR_STRING_FORMAT.formatted(COMMENT_OF_HEAD, data.getComment() != null ? data.getComment() : NO_COMMENT), html), Collections.singletonList(data.getEmployeeId()));
            } else if (data.getStatusId().equals(604L)) {
                shortEvent.setBody(getBodyForTaskStatusChangeWithComment(data.getEmployeeId(), data.getEmployeeDivisionTeamId(), TITLE_ADAPTATION, BODY_COMMENT_FOR_EMPLOYEE_604, S_S_FOR_STRING_FORMAT.formatted(COMMENT_OF_HEAD, data.getComment() != null ? data.getComment() : NO_COMMENT)));
                events.add(monitorServiceClient.createEvent(shortEvent));
                sendKafkaMessageWithoutCode(TITLE_ADAPTATION, makeMessageForKafka(BODY_COMMENT_FOR_EMPLOYEE_604, S_S_FOR_STRING_FORMAT.formatted(COMMENT_OF_HEAD, data.getComment() != null ? data.getComment() : NO_COMMENT), html), Collections.singletonList(data.getEmployeeId()));
            } else if (data.getStatusId().equals(605L)) {
                shortEvent.setBody(getBodyForTaskStatusChange(data.getEmployeeId(), data.getEmployeeDivisionTeamId(), TITLE_ADAPTATION, BODY_COMMENT_FOR_EMPLOYEE_605));
                events.add(monitorServiceClient.createEvent(shortEvent));
                sendKafkaMessageWithoutCode(TITLE_ADAPTATION, makeMessageForKafka(BODY_COMMENT_FOR_EMPLOYEE_605, S_S_FOR_STRING_FORMAT.formatted(COMMENT_OF_HEAD, data.getComment() != null ? data.getComment() : NO_COMMENT), html), Collections.singletonList(data.getEmployeeId()));
            } else if (data.getStatusId().equals(624L)) {
                shortEvent.setBody(getBodyForTaskStatusChange(data.getEmployeeId(), data.getEmployeeDivisionTeamId(), TITLE_ADAPTATION, BODY_COMMENT_FOR_EMPLOYEE_624));
                events.add(monitorServiceClient.createEvent(shortEvent));
                sendKafkaMessageWithoutCode(TITLE_ADAPTATION, makeMessageForKafka(BODY_COMMENT_FOR_EMPLOYEE_624, html), Collections.singletonList(data.getEmployeeId()));
            }
        }
        if (data.getHeadTaskUserId() != null) {
            ShortEventDto shortEvent = getShortEventWithCommonFields();
            shortEvent.setAssignmentId(data.getHeadTaskUserId());
            shortEvent.setAuthorName(data.getFioEmployee());
            if (data.getStatusId().equals(603L)) {
                String comment = String.format(BODY_COMMENT_FOR_HEAD_EMPLOYEE_603, data.getFioEmployee());
                shortEvent.setBody(getBodyForTaskStatusChangeWithComment(data.getHeadTaskEmployeeId(), data.getHeadTaskDivisionTeamId(), TITLE_NEW_EMPLOYEE_ADAPTATION, comment, S_S_FOR_STRING_FORMAT.formatted(COMMENT_OF_EMPLOYEE, data.getComment() != null ? data.getComment() : NO_COMMENT)));
                events.add(monitorServiceClient.createEvent(shortEvent));
                sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_ADAPTATION, makeMessageForKafka(comment, S_S_FOR_STRING_FORMAT.formatted(COMMENT_OF_EMPLOYEE, data.getComment() != null ? data.getComment() : NO_COMMENT), html), Collections.singletonList(data.getHeadTaskEmployeeId()));
            } else if (data.getStatusId().equals(604L)) {
                String comment = String.format(BODY_COMMENT_FOR_HEAD_EMPLOYEE_604, data.getFioEmployee());
                shortEvent.setBody(getBodyForTaskStatusChange(data.getHeadTaskEmployeeId(), data.getHeadTaskDivisionTeamId(), TITLE_NEW_EMPLOYEE_ADAPTATION, comment));
                events.add(monitorServiceClient.createEvent(shortEvent));
                sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_ADAPTATION, makeMessageForKafka(comment, html), Collections.singletonList(data.getHeadTaskEmployeeId()));
            }
        }
        if (data.getBuddyTaskUserId() != null) {
            ShortEventDto shortEvent = getShortEventWithCommonFields();
            shortEvent.setAssignmentId(data.getBuddyTaskUserId());
            shortEvent.setAuthorName(AUTHOR_NAME);
            if (data.getStatusId().equals(602L)) {
                String comment = String.format(BODY_COMMENT_FOR_BUDDY_EMPLOYEE_602, data.getFioEmployee());
                shortEvent.setBody(getBodyForTaskStatusChange(data.getBuddyTaskEmployeeId(), data.getBuddyTaskDivisionTeamId(), TITLE_NEW_EMPLOYEE_PROBATION, comment));
                events.add(monitorServiceClient.createEvent(shortEvent));
                sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_PROBATION, makeMessageForKafka(comment, html), Collections.singletonList(data.getBuddyTaskEmployeeId()));
            } else if (data.getStatusId().equals(603L)) {
                String comment = String.format(BODY_COMMENT_FOR_BUDDY_EMPLOYEE_603, data.getFioEmployee());
                shortEvent.setBody(getBodyForTaskStatusChange(data.getBuddyTaskEmployeeId(), data.getBuddyTaskDivisionTeamId(), TITLE_NEW_EMPLOYEE_ADAPTATION, comment));
                events.add(monitorServiceClient.createEvent(shortEvent));
                sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_ADAPTATION, makeMessageForKafka(comment, html), Collections.singletonList(data.getBuddyTaskEmployeeId()));
            } else if (data.getStatusId().equals(604L)) {
                String comment = String.format(BODY_COMMENT_FOR_BUDDY_EMPLOYEE_604, data.getFioEmployee());
                shortEvent.setBody(getBodyForTaskStatusChange(data.getBuddyTaskEmployeeId(), data.getBuddyTaskDivisionTeamId(), TITLE_NEW_EMPLOYEE_ADAPTATION, comment));
                events.add(monitorServiceClient.createEvent(shortEvent));
                sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_ADAPTATION, makeMessageForKafka(comment, html), Collections.singletonList(data.getBuddyTaskEmployeeId()));
            } else if (data.getStatusId().equals(605L)) {
                String comment = String.format(BODY_COMMENT_FOR_BUDDY_EMPLOYEE_605, data.getFioEmployee());
                shortEvent.setBody(getBodyForTaskStatusChange(data.getBuddyTaskEmployeeId(), data.getBuddyTaskDivisionTeamId(), TITLE_NEW_EMPLOYEE_ADAPTATION, comment));
                events.add(monitorServiceClient.createEvent(shortEvent));
                sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_ADAPTATION, makeMessageForKafka(comment, html), Collections.singletonList(data.getBuddyTaskEmployeeId()));
            } else if (data.getStatusId().equals(624L)) {
                String comment = String.format(BODY_COMMENT_FOR_BUDDY_EMPLOYEE_624, data.getFioEmployee());
                shortEvent.setBody(getBodyForTaskStatusChange(data.getBuddyTaskEmployeeId(), data.getBuddyTaskDivisionTeamId(), TITLE_NEW_EMPLOYEE_ADAPTATION, comment));
                events.add(monitorServiceClient.createEvent(shortEvent));
                sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_ADAPTATION, makeMessageForKafka(comment, html), Collections.singletonList(data.getBuddyTaskEmployeeId()));
            }
        }
        if (data.getHrTaskEmployeeIds() != null) {

            if (data.getStatusId().equals(602L)) {
                String text = String.format(BODY_COMMENT_FOR_HR_EMPLOYEE_602, data.getFioEmployee());
                String comment = S_S_FOR_STRING_FORMAT.formatted(COMMENT_OF_HEAD, data.getComment() != null ? data.getComment() : NO_COMMENT);
                sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_ADAPTATION, makeMessageForKafka(text, comment, html), data.getHrTaskEmployeeIds());
            }

            if (data.getStatusId().equals(605L)) {
                String text = String.format(BODY_COMMENT_FOR_HR_EMPLOYEE_605, data.getFioEmployee());
                String comment = S_S_FOR_STRING_FORMAT.formatted(COMMENT_OF_HEAD, data.getComment() != null ? data.getComment() : NO_COMMENT);
                sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_ADAPTATION, makeMessageForKafka(text, comment, html), data.getHrTaskEmployeeIds());
            }
        }
        return events;
    }

    @Override
    public OperationResult quizStart(QuizStartInputDto data) {
        try {
            PollRawDto poll = quizServiceClient.getPoll(data.getPollId());
            if (POLL_ID_SET_SIMPLE.contains(data.getPollId())) {
                EmployeeInfoDto employee = orgstructureServiceAdapter.getEmployeeInfo(data.getEmployeeId());
                if (employee == null) {
                    return new OperationResult(false, String.format("Something went wrong during receiving employee with id=%d", data.getEmployeeId()));
                }
                sendKafkaMessageWithoutCode(TITLE_ADAPTATION, makeMessageForKafka(String.format(MESSAGE_FOR_RECEIVER_QUIZ_START_EMPLOYEE, getNameShortFIO(employee), poll.getName()), html), Collections.singletonList(data.getEmployeeId()));
                return new OperationResult(true, "");
            }
            TaskFindRequest request = createTaskFindRequest(data.getPollId(), data.getUserPollId());
            List<TaskDto> tasks = tasksettingServiceClient.taskFind(request);
            if (tasks != null && !tasks.isEmpty()) {
                DivisionTeamAssignmentDto assignment = orgstructureServiceAdapter.getAssignments(Collections.singletonList(tasks.getFirst().getUserId()), null).get(0);
                sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_ADAPTATION, makeMessageForKafka(String.format(MESSAGE_FOR_RECEIVER_QUIZ_START, poll.getName(), getNameShortFIO(assignment.getEmployee())), html), Collections.singletonList(data.getEmployeeId()));
                return new OperationResult(true, "");
            }
            return new OperationResult(false, "Poll_id is not from %s or there are no tasks with input params".formatted(POLL_ID_SET_FULL));
        } catch (Exception ex) {
            return new OperationResult(false, ex.getMessage());
        }
    }

    @Override
    public OperationResult quizFinish(QuizFinishInputDto data) {
        UserPollDto userPoll;
        try {
            userPoll = quizServiceClient.getUserPoll(data.getUserPollId(), Boolean.TRUE);
        } catch (Exception ex) {
            return new OperationResult(false, ex.getMessage());
        }
        if (POLL_ID_SET_SIMPLE.contains(userPoll.getPoll().getId())) {
            DivisionTeamAssignmentDto assignment = orgstructureServiceAdapter.getAssignments(Collections.singletonList(data.getPassedQuizDtaId()), null).get(0);
            DivisionTeamAssignmentDto headAssignment =
                orgstructureServiceAdapter.getEmployeeHead(assignment.getEmployee().getId(), assignment.getDivisionTeamRole().getDivisionTeam().getId());
            if (headAssignment != null) {
                sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_ADAPTATION, makeMessageForKafka(String.format(MESSAGE_FOR_RECEIVER_QUIZ_FINISH, getNameShortFIO(assignment.getEmployee())), html), Collections.singletonList(headAssignment.getEmployee().getId()));
                return new OperationResult(true, "");
            }
            return new OperationResult(false, String.format("Head for employee with id=%d and division_team with id=%d is not found", assignment.getEmployee().getId(), assignment.getDivisionTeamRole().getDivisionTeam().getId()));
        }
        return new OperationResult(false, String.format("User_poll with id=%d doesn't contain any poll_id with id from %s", data.getUserPollId(), POLL_ID_SET_SIMPLE));
    }

    @Override
    public OperationResult customEmailEvent(CustomEmailEventInputDto data) {
        if (data.getMessageType().equals(MESSAGE_TYPE_1)) {
            List<DivisionTeamAssignmentDto> assignments = orgstructureServiceAdapter.getAssignments(Collections.singletonList(data.getAssignmentId()), null);
            if (assignments != null && !assignments.isEmpty()) {
                sendKafkaMessageWithoutCode(data.getTitle(), data.getBody(), Collections.singletonList(assignments.getFirst().getEmployee().getId()));
                return new OperationResult(true, "");
            }
            return new OperationResult(false, String.format("Assignment with id=%d is not found", data.getAssignmentId()));
        }
        if (data.getMessageType().equals(MESSAGE_TYPE_2)) {
            List<DivisionTeamAssignmentDto> assignments = orgstructureServiceAdapter.getAssignments(Collections.singletonList(data.getAssignmentId()), null);
            if (assignments != null && !assignments.isEmpty()) {
                DivisionTeamAssignmentDto headAssignment =
                    orgstructureServiceAdapter.getEmployeeHead(assignments.getFirst().getEmployee().getId(), assignments.getFirst().getDivisionTeamRole().getDivisionTeam().getId());

                String employeeFio = getNameShortFIO(assignments.getFirst().getEmployee());

                data.setBody(
                    replaceTokens(
                        data.getBody(),
                        new ReplaceItem(EMPLOYEE_FIO, employeeFio)
                    )
                );

                if (headAssignment != null) {
                    sendKafkaMessageWithoutCode(data.getTitle(), data.getBody(), Collections.singletonList(headAssignment.getEmployee().getId()));
                    return new OperationResult(true, "");
                }
                return new OperationResult(false, String.format("Head for employee with id=%d and division_team with id=%d is not found", assignments.getFirst().getEmployee().getId(), assignments.getFirst().getDivisionTeamRole().getDivisionTeam().getId()));
            }
            return new OperationResult(false, String.format("Assignment with id=%d is not found", data.getAssignmentId()));
        }
        if (data.getMessageType().equals(MESSAGE_TYPE_3)) {
            List<DivisionTeamAssignmentDto> assignments = orgstructureServiceAdapter.getAssignments(Collections.singletonList(data.getAssignmentId()), null);
            if (assignments != null && !assignments.isEmpty()) {
                String employeeFio = getNameShortFIO(assignments.getFirst().getEmployee());

                data.setBody(
                    replaceTokens(
                        data.getBody(),
                        new ReplaceItem(EMPLOYEE_FIO, employeeFio)
                    )
                );

                try {
                    Long buddyAssignmentId = tasksettingServiceClient.buddyFind(data.getTaskIdTo(), data.getTaskLinkTypeId(), data.getTypeId());
                    DivisionTeamAssignmentDto buddyAssignment = orgstructureServiceAdapter.getAssignments(Collections.singletonList(buddyAssignmentId), null).getFirst();
                    sendKafkaMessageWithoutCode(data.getTitle(), data.getBody(), Collections.singletonList(buddyAssignment.getEmployee().getId()));
                    return new OperationResult(true, "");
                } catch (Exception e) {
                    return new OperationResult(false, e.getMessage());
                }
            }
            return new OperationResult(false, String.format("Assignment with id=%d is not found", data.getAssignmentId()));
        }
        return new OperationResult(false, "Message type is not from %s".formatted(MESSAGE_TYPE_LIST));
    }

    @Override
    public OperationResult customEmailEventCode(CustomEmailEventCodeInputDto data) {
        try {
            if (Set.of("1", "2").contains(data.getCode())) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    DivisionTeamAssignmentDto assignment = getEmployeeAssignmentByTaskIdForNotification(data.getTaskId());
                    String fio = getFioByAssignmentForNotification(assignment);
                    String competenceNames = getCompetenceNameByCompetenceIdsForNotification(data.getCompetenceIds());

                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        EmployeeInfoDto recipient;
                        if (orgstructureServiceAdapter.checkEmployeeHeadTeamByAssignment(assignment.getId())) {
                            recipient = getHeadHeadEmployeeIdByHeadEmployeeIdAndDivisionTeamIdForNotification(
                                assignment.getEmployee().getId(),
                                assignment.getDivisionTeamRole().getDivisionTeam().getId()
                            );
                        } else {
                            recipient = getHeadEmployeeIdByEmployeeIdAndDivisionTeamIdForNotification(
                                assignment.getEmployee().getId(),
                                assignment.getDivisionTeamRole().getDivisionTeam().getId()
                            );
                        }

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(recipient),
                            new ReplaceItem(FIO, fio),
                            new ReplaceItem(COMPETENCE_NAME, competenceNames),
                            new ReplaceItem(ROOT_URL, notificationConfig.getRootUrl())
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("3")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        TaskDto task = getTaskForNotification(data.getTaskId());
                        DivisionTeamAssignmentDto assignment = getEmployeeAssignmentByTaskUserIdForNotification(task.getUserId());
                        String competenceNames = getCompetenceNameByTaskForNotification(task);

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(assignment.getEmployee()),
                            new ReplaceItem(COMPETENCE_NAME, competenceNames),
                            new ReplaceItem(ROOT_URL, notificationConfig.getRootUrl())
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("4")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        String competenceNames = getCompetenceNameByCompetenceIdsForNotification(data.getCompetenceIds());

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            getSubordinates(),
                            new ReplaceItem(COMPETENCE_NAME, competenceNames),
                            new ReplaceItem(ROOT_URL, notificationConfig.getRootUrl())
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("10")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        DivisionTeamAssignmentDto assignment = getEmployeeAssignmentByTaskIdForNotification(data.getTaskId());
                        String fio = getFioByAssignmentForNotification(assignment);

                        EmployeeInfoDto recipient;
                        if (orgstructureServiceAdapter.checkEmployeeHeadTeamByAssignment(assignment.getId())) {
                            recipient = getHeadHeadEmployeeIdByHeadEmployeeIdAndDivisionTeamIdForNotification(
                                assignment.getEmployee().getId(),
                                assignment.getDivisionTeamRole().getDivisionTeam().getId()
                            );
                        } else {
                            recipient = getHeadEmployeeIdByEmployeeIdAndDivisionTeamIdForNotification(
                                assignment.getEmployee().getId(),
                                assignment.getDivisionTeamRole().getDivisionTeam().getId()
                            );
                        }

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(recipient),
                            new ReplaceItem(FIO, fio),
                            new ReplaceItem(ROOT_URL, notificationConfig.getRootUrl())
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("11")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        DivisionTeamAssignmentDto assignment = getEmployeeAssignmentByTaskIdForNotification(data.getTaskId());
                        String date = data.getComment().toLowerCase().replaceAll("[а-яa-z]", "").trim();

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(assignment.getEmployee()),
                            new ReplaceItem("%date", date)
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("12")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        DivisionTeamAssignmentDto assignment = getEmployeeAssignmentByTaskIdForNotification(data.getTaskId());
                        String fio = getFioByAssignmentForNotification(assignment);

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(assignment.getEmployee()),
                            new ReplaceItem(FIO, fio),
                            new ReplaceItem(ROOT_URL, notificationConfig.getRootUrl())
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("13")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        DivisionTeamAssignmentDto assignment = getEmployeeAssignmentByTaskIdForNotification(data.getTaskId());
                        String fio = getFioByAssignmentForNotification(assignment);

                        EmployeeInfoDto recipient;
                        if (orgstructureServiceAdapter.checkEmployeeHeadTeamByAssignment(assignment.getId())) {
                            recipient = getHeadHeadEmployeeIdByHeadEmployeeIdAndDivisionTeamIdForNotification(
                                assignment.getEmployee().getId(),
                                assignment.getDivisionTeamRole().getDivisionTeam().getId()
                            );
                        } else {
                            recipient = getHeadEmployeeIdByEmployeeIdAndDivisionTeamIdForNotification(
                                assignment.getEmployee().getId(),
                                assignment.getDivisionTeamRole().getDivisionTeam().getId()
                            );
                        }

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(recipient),
                            new ReplaceItem(FIO, fio),
                            new ReplaceItem(ROOT_URL, notificationConfig.getRootUrl())
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("14")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        DivisionTeamAssignmentDto assignment = getEmployeeAssignmentByTaskIdForNotification(data.getTaskId());
                        String fio = getFioByAssignmentForNotification(assignment);

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(assignment.getEmployee()),
                            new ReplaceItem(FIO, fio)
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("15")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        DivisionTeamAssignmentDto assignment = getEmployeeAssignmentByTaskIdForNotification(data.getTaskId());
                        String fio = getFioByAssignmentForNotification(assignment);

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(assignment.getEmployee()),
                            new ReplaceItem(FIO, fio)
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("16")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        DivisionTeamAssignmentDto assignment = getEmployeeAssignmentByTaskIdForNotification(data.getTaskId());
                        String fio = getFioByAssignmentForNotification(assignment);

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(assignment.getEmployee()),
                            new ReplaceItem(FIO, fio),
                            new ReplaceItem(ROOT_URL, notificationConfig.getRootUrl())
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (Set.of("20", "21", "22", "23").contains(data.getCode())) {
                if (sendMessageForCustomEmailEventCode_Code20_21_22_23(!Objects.equals(data.getCode(), "22"), data)) {
                    return new OperationResult(true, "");
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            return new OperationResult(false, String.format("There is no handler for code=%s", data.getCode()));
        } catch (Exception ex) {
            return new OperationResult(false, ex.getMessage());
        }
    }

    @Override
    public OperationResult successorNotificateByCode(SuccessorNotificateByCodeInputData data) {
        try {
            if (data.getCode().equals("6")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        DivisionTeamSuccessorDto divisionTeamSuccessor = getDivisionTeamSuccessorForNotification(data.getDivisionTeamSuccessorId());
                        DivisionTeamAssignmentShortDto assignmentShort = getDivisionTeamAssignmentByDivisionTeamRoleIdForNotification(divisionTeamSuccessor.getDivisionTeamRole().getId());
                        String employeeInPositionFioByDivisionTeamSuccessorId = getNameShortFIO(assignmentShort.getEmployee());
                        String employeeSuccessorFio = getNameShortFIO(divisionTeamSuccessor.getEmployee());
                        String positionFullNameByDivisionTeamSuccessorId = getPositionFullNameByEmployeeIdAndDivisionIdForNotification(
                            assignmentShort.getEmployee().getId(),
                            divisionTeamSuccessor.getDivisionTeamRole().getDivisionTeam().getDivisionId()
                        );

                        EmployeeInfoDto recipient = getHeadEmployeeIdByEmployeeIdAndDivisionTeamIdForNotification(
                            assignmentShort.getEmployee().getId(), divisionTeamSuccessor.getDivisionTeamRole().getDivisionTeam().getId()
                        );

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(recipient),
                            new ReplaceItem(EMPLOYEE_IN_POSITION_FIO_BY_DIVISION_TEAM_SUCCESSOR_ID, employeeInPositionFioByDivisionTeamSuccessorId),
                            new ReplaceItem(EMPLOYEE_SUCCESSOR_FIO, employeeSuccessorFio),
                            new ReplaceItem(POSITION_FULL_NAME_BY_DIVISION_TEAM_SUCCESSOR_ID, positionFullNameByDivisionTeamSuccessorId)
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("7")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        DivisionTeamSuccessorDto divisionTeamSuccessor = getDivisionTeamSuccessorForNotification(data.getDivisionTeamSuccessorId());
                        DivisionTeamAssignmentShortDto assignmentShort = getDivisionTeamAssignmentByDivisionTeamRoleIdForNotification(divisionTeamSuccessor.getDivisionTeamRole().getId());
                        String employeeSuccessorFio = getNameShortFIO(divisionTeamSuccessor.getEmployee());

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(assignmentShort.getEmployee()),
                            new ReplaceItem(EMPLOYEE_SUCCESSOR_FIO, employeeSuccessorFio)
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("8")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        DivisionTeamSuccessorDto divisionTeamSuccessor = getDivisionTeamSuccessorForNotification(data.getDivisionTeamSuccessorId());
                        DivisionTeamAssignmentShortDto assignmentShort = getDivisionTeamAssignmentByDivisionTeamRoleIdForNotification(divisionTeamSuccessor.getDivisionTeamRole().getId());
                        String employeeInPositionFioByDivisionTeamSuccessorId = getNameShortFIO(assignmentShort.getEmployee());
                        String positionFullNameByDivisionTeamSuccessorId = getPositionFullNameByEmployeeIdAndDivisionIdForNotification(
                            assignmentShort.getEmployee().getId(),
                            divisionTeamSuccessor.getDivisionTeamRole().getDivisionTeam().getDivisionId()
                        );

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(divisionTeamSuccessor.getEmployee()),
                            new ReplaceItem(EMPLOYEE_IN_POSITION_FIO_BY_DIVISION_TEAM_SUCCESSOR_ID, employeeInPositionFioByDivisionTeamSuccessorId),
                            new ReplaceItem(POSITION_FULL_NAME_BY_DIVISION_TEAM_SUCCESSOR_ID, positionFullNameByDivisionTeamSuccessorId)
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            if (data.getCode().equals("9")) {
                NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
                if (notificationTemplate.getIsEnabled().equals(1)) {
                    List<NotificationTemplateContentEntity> notificationTemplateContentList =
                        notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

                    if (!notificationTemplateContentList.isEmpty()) {
                        DivisionTeamSuccessorDto divisionTeamSuccessor = getDivisionTeamSuccessorForNotification(data.getDivisionTeamSuccessorId());
                        DivisionTeamAssignmentShortDto assignmentShort = getDivisionTeamAssignmentByDivisionTeamRoleIdForNotification(divisionTeamSuccessor.getDivisionTeamRole().getId());
                        String employeeInPositionFioByDivisionTeamSuccessorId = getNameShortFIO(assignmentShort.getEmployee());
                        String employeeSuccessorFio = getNameShortFIO(divisionTeamSuccessor.getEmployee());
                        String positionFullNameByDivisionTeamSuccessorId = getPositionFullNameByEmployeeIdAndDivisionIdForNotification(
                            assignmentShort.getEmployee().getId(),
                            divisionTeamSuccessor.getDivisionTeamRole().getDivisionTeam().getDivisionId()
                        );

                        EmployeeInfoDto recipient = getHeadEmployeeIdByEmployeeIdAndDivisionTeamIdForNotification(
                            assignmentShort.getEmployee().getId(), divisionTeamSuccessor.getDivisionTeamRole().getDivisionTeam().getId()
                        );

                        sendKafkaMessageWithStaticTokens(
                            notificationTemplateContentList,
                            Collections.singletonList(recipient),
                            new ReplaceItem(EMPLOYEE_IN_POSITION_FIO_BY_DIVISION_TEAM_SUCCESSOR_ID, employeeInPositionFioByDivisionTeamSuccessorId),
                            new ReplaceItem(EMPLOYEE_SUCCESSOR_FIO, employeeSuccessorFio),
                            new ReplaceItem(POSITION_FULL_NAME_BY_DIVISION_TEAM_SUCCESSOR_ID, positionFullNameByDivisionTeamSuccessorId)
                        );

                        return new OperationResult(true, "");
                    }
                    return new OperationResult(false, NO_NOTIFICATION_TEMPLATE_CONTEXT.formatted(notificationTemplate.getId()));
                }
                return new OperationResult(false, IS_ENABLED_FIELD_FALSE);
            }
            return new OperationResult(false, String.format("There is no handler for code=%s", data.getCode()));
        } catch (Exception ex) {
            return new OperationResult(false, ex.getMessage());
        }
    }

    /**
     * Метод обрабатывает токены (находит токенам их реальные значения) и отправляет сообщения в очередь kafka
     *
     * @param data объект, содержащий в себе информацию для поиска и обработки токенов
     * @return true - если метод выполнен успешно, false - если возникли ошибки
     */
    @Override
    public OperationResult baseNotification(BaseNotificationInputData data) {
        try {
            List<NotificationTemplateContentDto> notificationTemplateContentByCode = notificationTemplateService.findNotificationTemplateContentByCode(data.getCode());

            for (NotificationTemplateContentDto ntc : notificationTemplateContentByCode) {
                prepareNotificationAndSend(ntc, data);
            }
            return new OperationResult(true, "");
        } catch (Exception ex) {
            log.error("An occurred error during making notification, message: " + ex.getMessage(), ex);
            return new OperationResult(false, ex.getMessage());
        }
    }

    private void prepareNotificationAndSend(NotificationTemplateContentDto notificationTemplateContentDto, BaseNotificationInputData data) {
        TokenResolverImpl.ResolveRecipitentResult resolvedRecipients = tokenResolver.resolveRecipients(notificationTemplateContentDto, resolverServiceContainer, data, false, notificationConfig.getSubstituteLevel());
        TokenResolverImpl.ResolveRecipitentResult resolvedCopyRecipients = tokenResolver.resolveRecipients(resolvedRecipients.getResolvedTemplateContent(), resolverServiceContainer, data, true, 0);

        Set<String> tokens = getAllTokens(resolvedRecipients.getResolvedTemplateContent().getBodyJson());
        ResolverContext context = new ResolverContext(resolverServiceContainer)
            .prepareTokens(tokens)
            .prepareFromBaseNotificationInputData(data)
            .notificationTemplateContent(resolvedRecipients.getResolvedTemplateContent());
        Map<String, String> resolvedTokenValues = tokenResolver.resolveTokens(context);

        if (resolvedRecipients.getRecipients() != null && !resolvedRecipients.getRecipients().isEmpty()) {
            for (RecipientInfoDto recipientDto : resolvedRecipients.getRecipients()) {
                /* Отправка уведомления происходит индивидуально для каждого employee, в связи с тем, что существуют токены при расчете которых
                формируется индивидуальный текст уведомления для каждого получателя */
                List<Long> recipientsIds = Stream.of(recipientDto)
                    .filter(e -> e instanceof EmployeeInfoDto)
                    .map(RecipientInfoDto::getId)
                    .filter(id -> subscribeEmployeeToNotificationService
                        .isPossibleSendNotificationToEmployee(id,
                            notificationTemplateContentDto.getId(),
                            notificationTemplateContentDto.getReceiverSystem().getId()))
                    .collect(Collectors.toList());

                List<Long> recipientsCopyIds = resolvedCopyRecipients.getRecipients().stream()
                    .filter(e -> e instanceof EmployeeInfoDto)
                    .map(RecipientInfoDto::getId)
                    .filter(id -> subscribeEmployeeToNotificationService
                        .isPossibleSendNotificationToEmployee(id,
                            resolvedRecipients.getResolvedTemplateContent().getId(),
                            resolvedRecipients.getResolvedTemplateContent().getReceiverSystem().getId()))
                    .collect(Collectors.toList());

                List<Long> emailIds = Stream.of(recipientDto)
                    .filter(e -> e instanceof StaticEmailRecipientDto)
                    .map(RecipientInfoDto::getId)
                    .collect(Collectors.toList());

                List<Long> emailCopyIds = resolvedCopyRecipients.getRecipients().stream()
                    .filter(e -> e instanceof StaticEmailRecipientDto)
                    .map(RecipientInfoDto::getId)
                    .collect(Collectors.toList());

                Map<String, String> employeeResolvedTokenValues = new HashMap<>(resolvedTokenValues);
                employeeResolvedTokenValues.putAll(tokenResolver.resolveEmployeeTokens(recipientDto, context));

                JSONObject contentJson = prepareJsonWithReplaceTokens(resolvedRecipients.getResolvedTemplateContent(), data, employeeResolvedTokenValues);
                if (!subscribeEmployeeToNotificationService.isPossibleSendNotificationToEmployee(recipientDto.getId(), resolvedRecipients.getResolvedTemplateContent().getId(), resolvedRecipients.getResolvedTemplateContent().getReceiverSystem().getId())) {
                    continue;
                }
                List<String> usersKeycloakIds = recipientDto instanceof EmployeeInfoDto ?
                    calculateKeycloakIds(recipientDto, resolvedCopyRecipients.getRecipients())
                    : null;

                kafkaService.sendKafkaMessage(
                    Collections.singletonList(resolvedRecipients.getResolvedTemplateContent().getId()),
                    recipientsIds,
                    recipientsCopyIds,
                    emailIds,
                    emailCopyIds,
                    usersKeycloakIds,
                    Collections.singletonList(resolvedRecipients.getResolvedTemplateContent().getReceiverSystem().getName()),
                    contentJson.toMap(),
                    null
                );

                if (CollectionUtils.isNotEmpty(recipientsIds)) {
                    log.info("sent message for employee_ids={}", recipientsIds);
                }

                if (CollectionUtils.isNotEmpty(emailIds)) {
                    log.info(
                        "sent message for static emails={}",
                        Stream.of(recipientDto)
                            .filter(e -> e instanceof StaticEmailRecipientDto)
                            .map(RecipientInfoDto::getEmail)
                            .collect(Collectors.toList())
                    );
                }
            }
        } else {
            log.info(NO_RECIPIENTS, data);
        }

    }

    private JSONObject prepareJsonWithReplaceTokens(NotificationTemplateContentDto ntc, BaseNotificationInputData data, Map<String, String> resolvedTokenValues) {
        List<ReplaceItem> replaceItemList = new ArrayList<>(resolvedTokenValues.size() + (data.getTokenValues() != null ? data.getTokenValues().size() : 0));
        resolvedTokenValues.forEach((stringToken, value) -> replaceItemList.add(new ReplaceItem("%" + stringToken + "%", value)));

        if (data.getTokenValues() != null) {
            data.getTokenValues().forEach((stringToken, value) -> replaceItemList.add(new ReplaceItem("%" + stringToken + "%", Objects.nonNull(value) ? value.toString() : "")));
        }

        List<NotificationTemplateContentAttachmentEntity> attachments = notificationTemplateContentAttachmentService.getAttachments(ntc);
        JSONObject bodyJson = new JSONObject(ntc.getBodyJson());
        if (!attachments.isEmpty()) {
            List<Object> attachmentInfos = attachments
                .stream()
                .map(this::createAttachmentInfo)
                .collect(Collectors.toList());
            bodyJson.put(ATTACHMENT_FILES, attachmentInfos);
        }
        if (Boolean.TRUE.equals(ntc.getPriority())) {
            bodyJson.put(HIGH_PRIORITY, true);
        }

        JSONObject contentJson = replaceTokens(new JSONObject(Map.of(ntc.getReceiverSystem().getName(), bodyJson)), replaceItemList);
        return contentJson;
    }

    private List<String> calculateKeycloakIds(RecipientInfoDto employeeInfoDto, Set<RecipientInfoDto> employeeCopyList) {
        List<String> employeeExternalIds = new ArrayList<>();
        if (employeeInfoDto instanceof EmployeeInfoDto dto) {
            if (Objects.nonNull(dto.getExternalId())) {
                employeeExternalIds.add(dto.getExternalId());
            }

            employeeExternalIds.addAll(employeeCopyList.stream()
                .filter(e -> e instanceof EmployeeInfoDto)
                .map(e -> ((EmployeeInfoDto) e).getExternalId())
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
            );
        }
        return keycloakDataService.getUsersKeycloakIds(employeeExternalIds.stream().distinct().collect(Collectors.toList()));
    }

    private Set<String> getAllTokens(String bodyJson) {
        Set<String> tokens = new HashSet<>();
        String[] tokensFromBodyJson = StringUtils.substringsBetween(bodyJson, TOKEN_DELIMETER, TOKEN_DELIMETER);
        if (tokensFromBodyJson != null) {
            Collections.addAll(tokens, tokensFromBodyJson);
        }
        return tokens;
    }

    @Override
    public OperationResult onboardingResultsHead(List<Long> divisionTeamAssignmentIds) {
        for (Long divisionTeamAssignmentId : divisionTeamAssignmentIds) {
            DivisionTeamAssignmentDto assignmentDto = orgstructureServiceAdapter.getAssignments(Collections.singletonList(divisionTeamAssignmentId), null).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Assignment not found, id: %d".formatted(divisionTeamAssignmentId)));

            DivisionTeamAssignmentDto employeeHead = orgstructureServiceAdapter.getEmployeeHead(assignmentDto.getEmployee().getId(), assignmentDto.getDivisionTeamRole().getDivisionTeam().getId());

            String fullName = getNameShortFIO(assignmentDto.getEmployee());
            sendKafkaMessageWithoutCode(TITLE_NEW_EMPLOYEE_ADAPTATION, makeMessageForKafka(MESSAGE_ONBOARDING_RESULT.formatted(fullName), html), Collections.singletonList(employeeHead.getEmployee().getId()));
        }
        return new OperationResult(true, "");
    }

    @Override
    public OperationResult sendKafkaMessage(DataForKafkaMessageInputDto data) {
        try {
            if (data.getUsersKeycloakIds() == null) {
                EmployeeInfoResponse employeeResponse = orgstructureServiceAdapter.findEmployee(data.getEmployeeIds());
                List<String> employeeExternalIds = employeeResponse.getData().stream().map(EmployeeInfoDto::getExternalId).collect(Collectors.toList());
                data.setUsersKeycloakIds(keycloakDataService.getUsersKeycloakIds(employeeExternalIds));
            }
            sendKafkaMessageWithoutCodeForLukoil(data.getSubject(), data.getMessage(), data.getEmployeeIds(), data.getUsersKeycloakIds(),
                data.getEventSubType(), data.getUseSystemTemplates());
            return new OperationResult(true, "");
        } catch (Exception ex) {
            return new OperationResult(false, ex.getMessage());
        }
    }

    @Override
    public OperationResult remindByHead(RemindByHeadInputDto data) {
        try {
            PollRawDto poll = quizServiceClient.getPoll(data.getPollId());
            List<Long> employeeIds = orgstructureServiceAdapter.getAssignments(data.getAssignments(), null)
                .stream()
                .map(assignment -> assignment.getEmployee().getId())
                .collect(Collectors.toList());
            String dateEnd = getStringDateByPattern(DATE_PATTERN_2, poll.getDateEnd());
            sendKafkaMessageWithoutCode(REMIND_BY_HEAD_SUBJECT, String.format(REMIND_BY_HEAD_MESSAGE, poll.getName(), dateEnd), employeeIds);
            return new OperationResult(true, "");
        } catch (Exception ex) {
            return new OperationResult(false, ex.getMessage());
        }
    }

    @Override
    public EventDto updateDtaRotation(EventInputDto input) {
        List<DivisionTeamAssignmentDto> assignmentList = orgstructureServiceAdapter.getAssignments(Collections.singletonList(input.getDivisionTeamAssignmentId()), null);
        if (assignmentList != null && !assignmentList.isEmpty()) {
            String rotationName = assignmentList.getFirst().getDivisionTeamAssignmentRotations().get(0).getRotation().getName();
            String currentDate = getStringDateByPattern(DATE_PATTERN_2, new Date());
            Map<String, Object> mapBody = new LinkedHashMap<>();
            mapBody.put(TITLE, TITLE_UPDATE_DTA_ROTATION);
            Map<String, Object> rotationCard = new LinkedHashMap<>();
            rotationCard.put("rotation", rotationName);
            rotationCard.put("current_date", currentDate);
            mapBody.put("rotation_card", rotationCard);
            Map<String, Object> button = new LinkedHashMap<>();
            button.put(TITLE, "Перейти");
            button.put("link", input.getButtonLink());
            mapBody.put("button", button);

            StringBuilder body = null;
            try {
                body = new StringBuilder(OBJECT_MAPPER.writeValueAsString(mapBody));
            } catch (JsonProcessingException ex) {
                log.error(ex.getMessage());
            }

            ShortEventDto shortEvent = new ShortEventDto(
                null, input.getDivisionTeamAssignmentId(), input.getAuthorName(),
                null, body != null ? body.toString() : null, EVENT_TYPE_FOR_UPDATE_DTA_ROTATION, EVENT_STATUS, null
            );

            return monitorServiceClient.createEvent(shortEvent);
        }
        throw new NotFoundException(String.format("There is no division_team_assignment with id=%d", input.getDivisionTeamAssignmentId()));
    }

    @Override
    public EventDto updateDtaRotationCompleted(EventInputDto input) {
        List<DivisionTeamAssignmentDto> assignmentList = orgstructureServiceAdapter.getAssignments(Collections.singletonList(input.getDivisionTeamAssignmentId()), null);
        if (assignmentList != null && !assignmentList.isEmpty()) {
            String rotationName = assignmentList.getFirst().getDivisionTeamAssignmentRotations().get(0).getRotation().getName();
            String currentDate = getStringDateByPattern(DATE_PATTERN_2, new Date());
            Map<String, Object> mapBody = new LinkedHashMap<>();
            mapBody.put(TITLE, TITLE_UPDATE_DTA_ROTATION_COMPLETED);
            Map<String, Object> rotationCard = new LinkedHashMap<>();
            rotationCard.put("rotation", rotationName);
            rotationCard.put("current_date", currentDate);
            mapBody.put("rotation_card", rotationCard);

            StringBuilder body = null;
            try {
                body = new StringBuilder(OBJECT_MAPPER.writeValueAsString(mapBody));
            } catch (JsonProcessingException ex) {
                log.error(ex.getMessage());
            }

            ShortEventDto shortEvent = new ShortEventDto(
                null, input.getDivisionTeamAssignmentId(), input.getAuthorName(),
                null, body != null ? body.toString() : null, EVENT_TYPE_FOR_UPDATE_DTA_ROTATION_COMPLETED, EVENT_STATUS, null
            );

            return monitorServiceClient.createEvent(shortEvent);
        }
        throw new NotFoundException(String.format("There is no division_team_assignment with id=%d", input.getDivisionTeamAssignmentId()));
    }

    @Override
    public void executeBaseNotificationForEventCreate(Long eventTypeId, Long eventId) {
        String code = createCodeForEventCreateEventType(eventTypeId);
        log.debug("Execute base notification for event create, code = {}", code);
        try {
            Map<String, Object> parameters = new HashMap<>();

            List<NotificationTemplateContentDto> notificationTemplateContentList = notificationTemplateService.findNotificationTemplateContentByCode(code);
            if (!notificationTemplateContentList.isEmpty()) {
                notificationTemplateContentList.forEach(ntc -> {
                    String[] tokensFromBodyJson = StringUtils.substringsBetween(ntc.getBodyJson(), "%", "%");
                    if (tokensFromBodyJson != null) {
                        Arrays.stream(tokensFromBodyJson).forEach(t -> {
                            String tokenGroup = StringUtils.substringBefore(t, ".");
                            if (tokenGroup.equals(EVENT_ID_TO_EVENT_INFO)) {
                                parameters.putIfAbsent(EVENT_ID_TO_EVENT_INFO, eventId);
                            }
                        });
                    }
                });
            }

            notificationService.baseNotification(null, code, parameters);
        } catch (Exception e) {
            log.error("Error while execute base notification for event create, code {}", code, e);
        }
    }

    private Set<DivisionTeamAssignmentDto> getHeadTeamAssignmentsFromTeamRoleContainerList(List<DivisionTeamRoleContainerDto> teamRoleContainerList) {
        Set<DivisionTeamAssignmentDto> headTeamAssignments = new HashSet<>();
        teamRoleContainerList.forEach(teamRoleContainer -> {
                DivisionTeamAssignmentShortDto shortAssignment = teamRoleContainer.getDivisionTeamAssignmentDtos().get(0);
                DivisionTeamAssignmentDto headTeamAssignment = orgstructureServiceAdapter.getEmployeeHead(shortAssignment.getEmployee().getId(), teamRoleContainer.getDivisionTeam().getId());
                if (headTeamAssignment != null) {
                    headTeamAssignments.add(headTeamAssignment);
                }
            }
        );
        return headTeamAssignments;
    }

    private DivisionTeamAssignmentDto getAssignment(Long id) {
        return orgstructureServiceAdapter.getAssignments(Collections.singletonList(id), null).getFirst();
    }

    private Long getHeadAssignmentId(DivisionTeamAssignmentDto assignment) {
        return orgstructureServiceAdapter.getEmployeeHead(assignment.getEmployee().getId(),
            assignment.getDivisionTeamRole().getDivisionTeam().getId()).getId();
    }

    private String getName(DivisionTeamAssignmentDto assignment) {
        return String.format(S_S_FOR_STRING_FORMAT, assignment.getEmployee().getFirstName(), assignment.getEmployee().getLastName());
    }

    private DivisionDto getDivision(DivisionTeamAssignmentDto assignment) {
        return orgstructureServiceAdapter.getDivisionInfo(assignment.getDivisionTeamRole().getDivisionTeam().getDivisionId()).getDivision();
    }

    private String getBody(String title,
                           String expertName,
                           String expertDivisionName,
                           String employeeName,
                           String employeeDivisionName,
                           EventBodyArgs eventBodyArgs) throws JsonProcessingException {
        Map<String, Object> mapBody = new LinkedHashMap<>();
        mapBody.put(TITLE, title);

        Map<String, Object> tile1 = new LinkedHashMap<>();
        tile1.put("mdi", "human-male-board.svg");
        tile1.put(TITLE, "Эксперт");
        tile1.put("name", expertName);
        tile1.put("division_name", expertDivisionName);
        tile1.put(TextConstants.PHOTO, eventBodyArgs.getExpertAssignment().getEmployee().getPhoto());
        tile1.put("first_name", eventBodyArgs.getExpertAssignment().getEmployee().getFirstName());
        tile1.put("last_name", eventBodyArgs.getExpertAssignment().getEmployee().getLastName());

        Map<String, Object> tile2 = new LinkedHashMap<>();
        tile2.put("mdi", "account-tie.svg");
        tile2.put(TITLE, "Работник");
        tile2.put("name", employeeName);
        tile2.put("division_name", employeeDivisionName);
        tile2.put(TextConstants.PHOTO, eventBodyArgs.getEmployeeAssignment().getEmployee().getPhoto());
        tile2.put("first_name", eventBodyArgs.getEmployeeAssignment().getEmployee().getFirstName());
        tile2.put("last_name", eventBodyArgs.getEmployeeAssignment().getEmployee().getLastName());
        if (eventBodyArgs.getEventType().equals(EVENT_TYPE_REJECT_REQUEST) || eventBodyArgs.getEventType().equals(EVENT_TYPE_REJECT_ASSIGNMENT)) {
            tile2.put("user_id", eventBodyArgs.getUserId());
        }

        mapBody.put("tile1", tile1);
        mapBody.put("tile2", tile2);

        if (eventBodyArgs.getEventType().equals(EVENT_TYPE_INCOMING_REQUEST)) {
            Map<String, Object> buttonOne = new LinkedHashMap<>();
            buttonOne.put("name", BUTTON_NAME_ONE);
            buttonOne.put("link", eventBodyArgs.getButtonLinkOne());
            buttonOne.put("id", eventBodyArgs.getTaskId());

            Map<String, Object> buttonTwo = new LinkedHashMap<>();
            buttonTwo.put("name", BUTTON_NAME_TWO);
            buttonTwo.put("link", eventBodyArgs.getButtonLinkTwo());
            buttonTwo.put("id", eventBodyArgs.getTaskId());

            mapBody.put("button1", buttonOne);
            mapBody.put("button2", buttonTwo);
        }
        return OBJECT_MAPPER.writeValueAsString(mapBody);
    }

    private String getBody(EventBodyArgs eventBodyArgs) throws JsonProcessingException {
        String employeeName = getName(eventBodyArgs.getEmployeeAssignment());
        String employeeDivisionName = getDivision(eventBodyArgs.getEmployeeAssignment()).getShortName();
        String expertName = getName(eventBodyArgs.getExpertAssignment());
        String expertDivisionName = getDivision(eventBodyArgs.getExpertAssignment()).getShortName();
        return getBody(eventBodyArgs.getBodyTitle(), expertName, expertDivisionName, employeeName, employeeDivisionName, eventBodyArgs);
    }

    private String getBodyForUpdateRotations(String divisionTeamRotationName,
                                             Date divisionTeamRotationDateFrom,
                                             Long divisionTeamAssignmentId,
                                             String buttonLinkEdit,
                                             String buttonLinkUpdate) throws JsonProcessingException {
        Map<String, Object> mapBody = new LinkedHashMap<>();
        mapBody.put(TextConstants.TITLE_CAPITAL, MESSAGE_NOTICE_UPDATE_DIVISION_TEAM_ASSIGNMENT_ROTATION);
        Map<String, Object> rotationCard = new LinkedHashMap<>();
        rotationCard.put("rotation", divisionTeamRotationName);
        rotationCard.put("rotation_date", getStringDateByPattern(DATE_PATTERN_2, divisionTeamRotationDateFrom));
        Map<String, Object> buttonEditor = new LinkedHashMap<>();
        buttonEditor.put(TITLE, "ИЗМЕНИТЬ");
        buttonEditor.put("link", "%s?id=%d".formatted(buttonLinkEdit, divisionTeamAssignmentId));
        Map<String, Object> button = new LinkedHashMap<>();
        button.put(TITLE, "ПОДТВЕРДИТЬ");
        button.put("link", "%s?id=%d".formatted(buttonLinkUpdate, divisionTeamAssignmentId));
        mapBody.put("rotation_card", rotationCard);
        mapBody.put("button_editor", buttonEditor);
        mapBody.put(TextConstants.BUTTON, button);
        return OBJECT_MAPPER.writeValueAsString(mapBody);
    }

    private ShortEventDto getShortEventWithCommonFields() {
        ShortEventDto shortEvent = new ShortEventDto();
        shortEvent.setEventTypeId(EVENT_TYPE_FOR_STATUS_CHANGE);
        shortEvent.setEventStatusId(EVENT_STATUS);
        return shortEvent;
    }

    private Map<String, Object> mapBodyForTaskStatusChange(Long employeeId, Long divisionTeamId, String title, String comment) {
        Map<String, Object> body = new HashMap<>();
        body.put(TextConstants.EMPLOYEE_ID, employeeId);
        body.put("division_team_id", divisionTeamId);
        body.put(TITLE, title);
        body.put("comment", comment);
        return body;
    }

    private String getBodyForTaskStatusChange(Long employeeId, Long divisionTeamId, String title, String comment) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(mapBodyForTaskStatusChange(employeeId, divisionTeamId, title, comment));
    }

    private String getBodyForTaskStatusChangeWithComment(Long employeeId, Long divisionTeamId, String title, String comment, String title2) throws JsonProcessingException {
        Map<String, Object> body = mapBodyForTaskStatusChange(employeeId, divisionTeamId, title, comment);
        body.put("title2", title2);
        return OBJECT_MAPPER.writeValueAsString(body);
    }

    private String makeMessageForKafka(String... message) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        for (String msg : message) {
            stringJoiner.add(msg);
        }
        return stringJoiner.toString();
    }

    private TaskFindRequest createTaskFindRequest(Long pollId, Long userPollId) {
        TaskFindRequest request = new TaskFindRequest();
        request.setTaskFieldValue(userPollId.toString());
        if (pollId.equals(5L)) {
            request.setTaskTypeFieldId(186L);
        }
        if (pollId.equals(6L)) {
            request.setTaskTypeFieldId(187L);
        }
        if (pollId.equals(7L)) {
            request.setTaskTypeFieldId(188L);
        }
        if (pollId.equals(8L)) {
            request.setTaskTypeFieldId(185L);
        }
        return request;
    }

    private TaskDto getTaskForNotification(Long taskId) throws Exception {
        TaskDto task = tasksettingServiceClient.getTaskById(taskId);
        if (task == null) {
            throw new Exception("Something went wrong during receiving task with id=%d".formatted(taskId));
        }
        return task;
    }

    private DivisionTeamAssignmentDto getEmployeeAssignmentByTaskUserIdForNotification(Long userId) throws Exception {
        List<DivisionTeamAssignmentDto> assignments = orgstructureServiceAdapter.getAssignments(Collections.singletonList(userId), null);
        if (assignments == null || assignments.isEmpty()) {
            throw new Exception("Something went wrong during receiving division team assignment with id=%d".formatted(userId));
        }
        return assignments.getFirst();
    }

    private DivisionTeamAssignmentDto getEmployeeAssignmentByTaskIdForNotification(Long taskId) throws Exception {
        TaskDto task = getTaskForNotification(taskId);
        List<DivisionTeamAssignmentDto> assignments = orgstructureServiceAdapter.getAssignments(Collections.singletonList(task.getUserId()), null);
        if (assignments == null || assignments.isEmpty()) {
            throw new Exception(String.format("Something went wrong during receiving division team assignment with id=%d", task.getUserId()));
        }
        return assignments.getFirst();
    }

    private EmployeeInfoDto getHeadEmployeeIdByEmployeeIdAndDivisionTeamIdForNotification(Long employeeId, Long divisionTeamId) throws Exception {
        DivisionTeamAssignmentDto headAssignment = orgstructureServiceAdapter.getEmployeeHead(employeeId, divisionTeamId);
        if (headAssignment == null) {
            throw new Exception("Something went wrong during receiving head division team assignment for employee with id=%d and division team with id=%d".formatted(employeeId, divisionTeamId));
        }
        return headAssignment.getEmployee();
    }

    private EmployeeInfoDto getHeadHeadEmployeeIdByHeadEmployeeIdAndDivisionTeamIdForNotification(Long headEmployeeId, Long divisionTeamId) throws Exception {
        DivisionTeamAssignmentDto headHeadAssignment = orgstructureServiceAdapter.getTeamDivisionHeadHead(headEmployeeId, divisionTeamId);
        return headHeadAssignment.getEmployee();
    }

    private List<EmployeeInfoDto> getSubordinates() throws Exception {
        DivisionTeamAssignmentDto assignment = getEmployeeAssignmentByTaskUserIdForNotification(null);
        Long divisionTeamId = assignment.getDivisionTeamRole().getDivisionTeam().getId();
        List<DivisionTeamAssignmentShortDto> subordinates = orgstructureServiceAdapter.getTeamDivisionSubordinates(divisionTeamId);
        if (subordinates.isEmpty()) {
            throw new Exception("Something went wrong during subordinates for division team with id=%d".formatted(divisionTeamId));
        }
        return subordinates.stream().map(DivisionTeamAssignmentShortDto::getEmployee).collect(Collectors.toList());
    }

    private String getCompetenceNameByCompetenceIdsForNotification(List<Long> competenceIds) throws Exception {
        List<String> competenceNames = competenceService.findCompetenceListByIds(competenceIds)
            .stream()
            .map(CompetenceNotificationDto::getName)
            .collect(Collectors.toList());
        if (competenceNames.isEmpty()) {
            throw new Exception("Something went wrong during receiving competence with ids=%s".formatted(competenceIds));
        }
        return StringUtils.join(competenceNames, ", ");
    }

    private TaskFieldDto getTaskFieldByTaskAndTaskTypeFieldId(TaskDto task, Long taskFieldTypeId) throws Exception {
        TaskFieldDto taskField = task.getFields()
            .stream()
            .filter(tf -> Objects.equals(tf.getType().getId(), taskFieldTypeId))
            .findFirst()
            .orElse(null);
        if (taskField == null) {
            throw new Exception("Something went wrong during receiving task field type with id=%d".formatted(taskFieldTypeId));
        }
        return taskField;
    }

    private String getCompetenceNameByTaskForNotification(TaskDto task) throws Exception {
        TaskFieldDto taskField = getTaskFieldByTaskAndTaskTypeFieldId(task, 19L);
        Long competenceId = Long.valueOf(taskField.getValue());
        List<String> competenceNames = competenceService.findCompetenceListByIds(Collections.singletonList(competenceId))
            .stream()
            .map(CompetenceNotificationDto::getName)
            .collect(Collectors.toList());
        if (competenceNames.isEmpty()) {
            throw new Exception("Something went wrong during receiving competence with id=%d".formatted(competenceId));
        }
        return competenceNames.getFirst();
    }

    private DivisionTeamSuccessorDto getDivisionTeamSuccessorForNotification(Long divisionTeamSuccessorId) {
        return orgstructureServiceAdapter.getDivisionTeamSuccessor(divisionTeamSuccessorId);
    }

    private DivisionTeamAssignmentShortDto getDivisionTeamAssignmentByDivisionTeamRoleIdForNotification(Long divisionTeamRoleId) throws Exception {
        List<DivisionTeamRoleContainerDto> roleContainers = orgstructureServiceAdapter.findDivisionTeamRoles(null, null, null, null, null, divisionTeamRoleId);
        if (roleContainers == null || roleContainers.isEmpty()) {
            throw new Exception("Something went wrong during receiving role container by division_team_role_id=%d".formatted(divisionTeamRoleId));
        }
        return roleContainers.getFirst().getDivisionTeamAssignmentDtos().get(0);
    }

    private String getPositionFullNameByEmployeeIdAndDivisionIdForNotification(Long employeeId, Long divisionId) throws Exception {
        List<PositionDto> positions = orgstructureServiceAdapter.getPositionByEmployeeIdAndDivisionId(employeeId, divisionId);
        if (positions.isEmpty()) {
            throw new Exception("Something went wrong during receiving position by employee_id=%d and division_id=%d".formatted(employeeId, divisionId));
        }
        return positions.getFirst().getFullName();
    }

    private boolean sendMessageForCustomEmailEventCode_Code20_21_22_23(boolean sendForHead, CustomEmailEventCodeInputDto data) throws Exception {
        NotificationTemplateEntity notificationTemplate = notificationTemplateService.getNotificationTemplateByCode(data.getCode());
        if (notificationTemplate.getIsEnabled().equals(1)) {
            List<NotificationTemplateContentEntity> notificationTemplateContentList =
                notificationTemplateService.getNotificationTemplateContentByNotificationTemplateId(notificationTemplate.getId());

            if (!notificationTemplateContentList.isEmpty()) {
                TaskDto task = getTaskForNotification(data.getTaskId());
                DivisionTeamAssignmentDto sessionAssignment = getEmployeeAssignmentByTaskUserIdForNotification(null);
                String sessionFio = getFioByAssignmentForNotification(sessionAssignment);
                DivisionTeamAssignmentDto taskOwnerAssignment = getEmployeeAssignmentByTaskUserIdForNotification(task.getUserId());
                String taskOwnerFio = getFioByAssignmentForNotification(taskOwnerAssignment);

                EmployeeInfoDto recipient = taskOwnerAssignment.getEmployee();
                if (sendForHead) {
                    if (orgstructureServiceAdapter.checkEmployeeHeadTeamByAssignment(taskOwnerAssignment.getId())) {
                        recipient = getHeadHeadEmployeeIdByHeadEmployeeIdAndDivisionTeamIdForNotification(
                            taskOwnerAssignment.getEmployee().getId(),
                            taskOwnerAssignment.getDivisionTeam().getId()
                        );
                    } else {
                        recipient = getHeadEmployeeIdByEmployeeIdAndDivisionTeamIdForNotification(
                            taskOwnerAssignment.getEmployee().getId(),
                            taskOwnerAssignment.getDivisionTeam().getId()
                        );
                    }
                }

                sendKafkaMessageWithStaticTokens(
                    notificationTemplateContentList,
                    Collections.singletonList(recipient),
                    new ReplaceItem("%type_name", task.getType().getName()),
                    new ReplaceItem("%status_name", task.getStatus().getName()),
                    new ReplaceItem("%task_owner_full_name", taskOwnerFio),
                    new ReplaceItem("%author_name", sessionFio)
                );

                return true;
            }
            return false;
        }
        return false;
    }

    private void sendKafkaMessageWithStaticTokens(Collection<NotificationTemplateContentEntity> notificationTemplateContentList, Collection<EmployeeInfoDto> employeeList, ReplaceItem... replaceItems) {
        List<String> channels = new ArrayList<>();
        JSONObject content = new JSONObject();

        List<Long> notificationTemplateContentIds = new ArrayList<>();
        notificationTemplateContentList.forEach(ntc -> {
            channels.add(ntc.getReceiverSystem().getName());

            JSONObject bodyJson = new JSONObject(ntc.getBodyJson());
            List<NotificationTemplateContentAttachmentEntity> attachments = notificationTemplateContentAttachmentService.getAttachments(ntc);
            if (!attachments.isEmpty()) {
                List<Object> attachmentInfos = attachments
                    .stream()
                    .map(this::createAttachmentInfo)
                    .collect(Collectors.toList());
                bodyJson.put(ATTACHMENT_FILES, attachmentInfos);
            }
            if (Boolean.TRUE.equals(ntc.getPriority())) {
                bodyJson.put(HIGH_PRIORITY, true);
            }
            content.put(ntc.getReceiverSystem().getName(), bodyJson);
            notificationTemplateContentIds.add(ntc.getId());
        });

        JSONObjectReplace jsonObjectReplace = new JSONObjectReplace(content, replaceItems);

        Set<Long> employeeIds = new HashSet<>();
        Set<String> employeeExternalIds = new HashSet<>();
        employeeList.forEach(e -> {
            employeeIds.add(e.getId());
            employeeExternalIds.add(e.getExternalId());
        });
        List<String> usersKeycloakIds = keycloakDataService.getUsersKeycloakIds(employeeExternalIds);

        kafkaService.sendKafkaMessage(notificationTemplateContentIds, employeeIds, null, null, null, usersKeycloakIds, channels, jsonObjectReplace.getReplacedContent(), null);
    }

    private void sendKafkaMessageWithoutCode(String subject, String message, Collection<Long> employeeIds) {
        sendKafkaMessageWithoutCodeForLukoil(subject, message, employeeIds, null, null, null);
    }

    private void sendKafkaMessageWithoutCodeForLukoil(String subject, String message, Collection<Long> employeeIds, Collection<String> usersKeycloakIds,
                                                      String eventSubType, Boolean useSystemTemplates) {
        JSONObject content = new JSONObject();
        JSONObject email = new JSONObject();
        email.put("subject", subject);
        email.put("body", message);
        if (useSystemTemplates != null) {
            email.put("use_system_templates", useSystemTemplates);
        }
        content.put("email", email);
        kafkaService.sendKafkaMessage(Collections.emptyList(), employeeIds, null, null, null, usersKeycloakIds, null, content.toMap(), eventSubType);
    }

    private Object createAttachmentInfo(NotificationTemplateContentAttachmentEntity attachmentEntity) {
        Map<String, String> info = new HashMap<>();

        info.put("storage_file_path", attachmentEntity.getStorageFilePath());
        info.put("file_name", attachmentEntity.getFileName());
        if (attachmentEntity.getFileType() != null) {
            info.put("file_type", attachmentEntity.getFileType());
        }

        return info;
    }

    @Getter
    @RequiredArgsConstructor
    private static class ReplaceItem {
        private final String oldString;
        private final String newString;
    }

    @Getter
    private static class JSONObjectReplace {
        private final Map<String, Object> replacedContent;

        public JSONObjectReplace(JSONObject content, ReplaceItem... replaceItems) {
            StringBuilder builder = new StringBuilder(content.toString());
            for (ReplaceItem item : replaceItems) {
                int start = builder.indexOf(item.getOldString());
                while (start != -1) {
                    builder.replace(start, start + item.getOldString().length(), item.getNewString());
                    start += item.getNewString().length();
                    start = builder.indexOf(item.getOldString(), start);
                }
            }
            this.replacedContent = new JSONObject(builder.toString()).toMap();
        }

        public JSONObjectReplace(JSONObject content, List<ReplaceItem> replaceItems) {
            StringBuilder builder = new StringBuilder(content.toString());
            replaceItems.forEach(item -> {
                int start = builder.indexOf(item.getOldString());
                while (start != -1) {
                    builder.replace(start, start + item.getOldString().length(), item.getNewString());
                    start += item.getNewString().length();
                    start = builder.indexOf(item.getOldString(), start);
                }
            });
            this.replacedContent = new JSONObject(builder.toString()).toMap();
        }
    }
}
