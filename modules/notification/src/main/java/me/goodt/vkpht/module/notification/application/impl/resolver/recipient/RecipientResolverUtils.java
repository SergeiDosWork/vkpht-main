package me.goodt.vkpht.module.notification.application.impl.resolver.recipient;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.goodt.vkpht.module.notification.api.dto.data.DivisionTeamInfo;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskFieldDto;
import me.goodt.vkpht.module.notification.api.dto.tasksetting2.TaskFindRequest;
import me.goodt.vkpht.module.notification.application.SavedObjectNames;
import me.goodt.vkpht.module.notification.application.impl.ResolverContext;
import me.goodt.vkpht.module.orgstructure.api.dto.DivisionTeamAssignmentDto;
import me.goodt.vkpht.module.orgstructure.api.dto.RecipientInfoDto;

import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.TASK_FIELD_TYPE_ID_140;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.TASK_FIELD_TYPE_ID_342;
import static me.goodt.vkpht.module.notification.application.utils.GlobalDefs.TASK_TYPE_ID_80;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.APPRAISAL_EVENT_ID_TO_APPRAISAL_EVENT_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.APPRAISAL_EVENT_ID_TO_TASK_HOLDER_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.EVENT_ID_TO_EVENT_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.LEARNING_COURSE_ID_TO_LEARNING_COURSE_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.LEARNING_COURSE_ID_TO_USER_LEARNING_COURSE_STEP_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.LEARNING_COURSE_STEP_ID_TO_LEARNING_COURSE_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.LEARNING_COURSE_STEP_ID_TO_LEARNING_COURSE_STEP_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POLL_ID_TO_POLL_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_ID_TO_EMPLOYEE_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_SUCCESSOR_READINESS_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.PROCESS_STAGE_ID_TO_PROCESS_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.PROCESS_STAGE_ID_TO_PROCESS_STAGE_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_AND_STATUS_ID_TO_TASK_HISTORY_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_APPRAISAL_EVENT_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_COMPETENCE_PROFILE_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_DEVPLAN_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_DEVPLAN_MENTOR;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_EVALUATION_EVENT_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_LEARNING_COURSE_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_LEARNING_STUDYGROUP_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_ONBOARDING_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_PRACTICE_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_PROCESS_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_HEAD_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_ID_TO_TASK_HOLDER_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_LINK_ID_TO_TASK_ID_FROM_HOLDER_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.TASK_LINK_ID_TO_TASK_ID_TO_HOLDER_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.USER_LEARNING_COURSE_ID_TO_LEARNING_COURSE_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.USER_LEARNING_COURSE_ID_TO_USER_INFO;
import static me.goodt.vkpht.module.notification.application.utils.TextConstants.USER_LEARNING_COURSE_ID_TO_USER_LEARNING_COURSE_STEP_INFO;

@UtilityClass
@Slf4j
public class RecipientResolverUtils {
    public Long findTaskId(ResolverContext context) {
        Object taskId = null;

        if (Objects.nonNull(context.getParameters())) {
            if (context.getParameters().containsKey(TASK_ID_TO_TASK_HOLDER_INFO)) {
                taskId = context.getParameters().get(TASK_ID_TO_TASK_HOLDER_INFO);
            }
            if (context.getParameters().containsKey(TASK_ID_AND_STATUS_ID_TO_TASK_HISTORY_INFO)) {
                taskId = context.getParameters().get(TASK_ID_AND_STATUS_ID_TO_TASK_HISTORY_INFO);
            }
            if (context.getParameters().containsKey(TASK_ID_TO_COMPETENCE_PROFILE_INFO)) {
                taskId = context.getParameters().get(TASK_ID_TO_COMPETENCE_PROFILE_INFO);
            }
            if (context.getParameters().containsKey(TASK_ID_TO_APPRAISAL_EVENT_INFO)) {
                taskId = context.getParameters().get(TASK_ID_TO_APPRAISAL_EVENT_INFO);
            }
            if (context.getParameters().containsKey(TASK_ID_TO_PRACTICE_INFO)) {
                taskId = context.getParameters().get(TASK_ID_TO_PRACTICE_INFO);
            }
            if (context.getParameters().containsKey(TASK_ID_TO_PROCESS_INFO)) {
                taskId = context.getParameters().get(TASK_ID_TO_PROCESS_INFO);
            }
            if (context.getParameters().containsKey(TASK_ID_TO_EVALUATION_EVENT_INFO)) {
                taskId = context.getParameters().get(TASK_ID_TO_EVALUATION_EVENT_INFO);
            }
            if (context.getParameters().containsKey(TASK_ID_TO_ONBOARDING_INFO)) {
                taskId = context.getParameters().get(TASK_ID_TO_ONBOARDING_INFO);
            }
            if (context.getParameters().containsKey(TASK_ID_TO_LEARNING_COURSE_INFO)) {
                taskId = context.getParameters().get(TASK_ID_TO_LEARNING_COURSE_INFO);
            }
            if (context.getParameters().containsKey(TASK_ID_TO_LEARNING_STUDYGROUP_INFO)) {
                taskId = context.getParameters().get(TASK_ID_TO_LEARNING_STUDYGROUP_INFO);
            }
            if (context.getParameters().containsKey(TASK_ID_TO_DEVPLAN_INFO)) {
                taskId = context.getParameters().get(TASK_ID_TO_DEVPLAN_INFO);
            }
            if (context.getParameters().containsKey(TASK_ID_TO_DEVPLAN_MENTOR)) {
                taskId = context.getParameters().get(TASK_ID_TO_DEVPLAN_MENTOR);
            }
            if (context.getParameters().containsKey(TASK_ID)) {
                taskId = context.getParameters().get(TASK_ID);
            }
            if (context.getParameters().containsKey(TASK_ID_TO_TASK_HOLDER_HEAD_INFO)) {
                taskId = context.getParameters().get(TASK_ID_TO_TASK_HOLDER_HEAD_INFO);
            }
        }
        if (taskId != null) {
            log.info("task id: {}", taskId);
            return Long.parseLong(taskId.toString());
        }
        log.warn("context not contains task key");
        return null;
    }

    public Integer findLearningCourseId(ResolverContext context) {
        Integer learningCourseId = null;

        if (Objects.nonNull(context.getParameters())) {
            if (context.getParameters().containsKey(LEARNING_COURSE_ID_TO_LEARNING_COURSE_INFO)) {
                learningCourseId = (Integer) context.getParameters().get(LEARNING_COURSE_ID_TO_LEARNING_COURSE_INFO);
            }
            if (context.getParameters().containsKey(LEARNING_COURSE_ID_TO_USER_LEARNING_COURSE_STEP_INFO)) {
                learningCourseId = (Integer) context.getParameters().get(LEARNING_COURSE_ID_TO_USER_LEARNING_COURSE_STEP_INFO);
            }
        }

        return learningCourseId;
    }

    public Integer findAppraisalEventId(ResolverContext context) {
        Integer appraisalEventId = null;

        if (Objects.nonNull(context.getParameters())) {
            if (context.getParameters().containsKey(APPRAISAL_EVENT_ID_TO_APPRAISAL_EVENT_INFO)) {
                appraisalEventId = (Integer) context.getParameters().get(APPRAISAL_EVENT_ID_TO_APPRAISAL_EVENT_INFO);
            }
            if (context.getParameters().containsKey(APPRAISAL_EVENT_ID_TO_TASK_HOLDER_INFO)) {
                appraisalEventId = (Integer) context.getParameters().get(APPRAISAL_EVENT_ID_TO_TASK_HOLDER_INFO);
            }
        }

        return appraisalEventId;
    }

    public Integer findLearningCourseStepId(ResolverContext context) {
        Integer learningCourseStepId = null;

        if (Objects.nonNull(context.getParameters())) {
            if (context.getParameters().containsKey(LEARNING_COURSE_STEP_ID_TO_LEARNING_COURSE_INFO)) {
                learningCourseStepId = (Integer) context.getParameters().get(LEARNING_COURSE_STEP_ID_TO_LEARNING_COURSE_INFO);
            }
            if (context.getParameters().containsKey(LEARNING_COURSE_STEP_ID_TO_LEARNING_COURSE_STEP_INFO)) {
                learningCourseStepId = (Integer) context.getParameters().get(LEARNING_COURSE_STEP_ID_TO_LEARNING_COURSE_STEP_INFO);
            }
        }

        return learningCourseStepId;
    }

    public void findHeadRecipient(ResolverContext context, List<DivisionTeamAssignmentDto> assignments, Set<RecipientInfoDto> employeeList) {
        Map<Long, DivisionTeamInfo> divisionTeamInfoMap = getDivisionTeamInfoMap(assignments);
        divisionTeamInfoMap.forEach((dtId, dtInfo) -> {
            if (dtInfo.getHeadAssignment() != null) {
                DivisionTeamAssignmentDto headHeadAssignment = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getEmployeeHead(dtInfo.getHeadAssignment().getEmployee().getId(), dtId);
                employeeList.add(headHeadAssignment.getEmployee());
                dtInfo.getEmployeeIds().remove(dtInfo.getHeadAssignment().getEmployee().getId());
                if (!dtInfo.getEmployeeIds().isEmpty()) {
                    employeeList.add(dtInfo.getHeadAssignment().getEmployee());
                }
            } else {
                DivisionTeamAssignmentDto headAssignment = context.getResolverServiceContainer().getOrgstructureServiceAdapter().getEmployeeHead(dtInfo.getEmployees().get(0).getId(), dtId);
                employeeList.add(headAssignment.getEmployee());
            }
        });
    }

    public Map<Long, DivisionTeamInfo> getDivisionTeamInfoMap(List<DivisionTeamAssignmentDto> assignments) {
        Map<Long, DivisionTeamInfo> divisionTeamInfoMap = new HashMap<>();
        assignments.forEach(dta -> {
            if (divisionTeamInfoMap.containsKey(dta.getDivisionTeam().getId())) {
                DivisionTeamInfo divisionTeamInfo = divisionTeamInfoMap.get(dta.getDivisionTeam().getId());
                if (divisionTeamInfo.getHeadAssignment() == null && dta.getDivisionTeamRole().getRole().getSystemRole().getId().equals(1)) {
                    divisionTeamInfo.setHeadAssignment(dta);
                }
                divisionTeamInfo.getEmployees().add(dta.getEmployee());
                divisionTeamInfo.getEmployeeIds().add(dta.getEmployee().getId());
            } else {
                DivisionTeamInfo divisionTeamInfo = new DivisionTeamInfo(
                    dta.getDivisionTeam().getId(),
                    dta.getDivisionTeamRole().getRole().getSystemRole().getId().equals(1) ? dta : null,
                    Stream.of(dta.getEmployee()).collect(Collectors.toList()),
                    Stream.of(dta.getEmployee().getId()).collect(Collectors.toSet())
                );
                divisionTeamInfoMap.put(dta.getDivisionTeam().getId(), divisionTeamInfo);
            }
        });
        return divisionTeamInfoMap;
    }

    public Integer findUserLearningCourseId(ResolverContext context) {
        Integer userLearningCourseId = null;

        if (Objects.nonNull(context.getParameters())) {
            if (context.getParameters().containsKey(USER_LEARNING_COURSE_ID_TO_LEARNING_COURSE_INFO)) {
                userLearningCourseId = (Integer) context.getParameters().get(USER_LEARNING_COURSE_ID_TO_LEARNING_COURSE_INFO);
            }
            if (context.getParameters().containsKey(USER_LEARNING_COURSE_ID_TO_USER_INFO)) {
                userLearningCourseId = (Integer) context.getParameters().get(USER_LEARNING_COURSE_ID_TO_USER_INFO);
            }
            if (context.getParameters().containsKey(USER_LEARNING_COURSE_ID_TO_USER_LEARNING_COURSE_STEP_INFO)) {
                userLearningCourseId = (Integer) context.getParameters().get(USER_LEARNING_COURSE_ID_TO_USER_LEARNING_COURSE_STEP_INFO);
            }
        }

        return userLearningCourseId;
    }

    public Integer findProcessStageId(ResolverContext context) {
        Integer processStageId = null;

        if (Objects.nonNull(context.getParameters())) {
            if (context.getParameters().containsKey(PROCESS_STAGE_ID_TO_PROCESS_STAGE_INFO)) {
                processStageId = (Integer) context.getParameters().get(PROCESS_STAGE_ID_TO_PROCESS_STAGE_INFO);
            }
            if (context.getParameters().containsKey(PROCESS_STAGE_ID_TO_PROCESS_INFO)) {
                processStageId = (Integer) context.getParameters().get(PROCESS_STAGE_ID_TO_PROCESS_INFO);
            }
        }

        return processStageId;
    }

    public Integer findPositionSuccessorId(ResolverContext context) {
        Integer positionSuccessorId = null;

        if (Objects.nonNull(context.getParameters())) {
            if (context.getParameters().containsKey(POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO)) {
                positionSuccessorId = (Integer) context.getParameters().get(POSITION_SUCCESSOR_ID_TO_POSITION_SUCCESSOR_INFO);
            }
            if (context.getParameters().containsKey(POSITION_SUCCESSOR_ID_TO_POSITION_INFO)) {
                positionSuccessorId = (Integer) context.getParameters().get(POSITION_SUCCESSOR_ID_TO_POSITION_INFO);
            }
            if (context.getParameters().containsKey(POSITION_SUCCESSOR_ID_TO_EMPLOYEE_INFO)) {
                positionSuccessorId = (Integer) context.getParameters().get(POSITION_SUCCESSOR_ID_TO_EMPLOYEE_INFO);
            }
            if (context.getParameters().containsKey(POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO)) {
                positionSuccessorId = (Integer) context.getParameters().get(POSITION_SUCCESSOR_ID_TO_EMPLOYEE_BY_POSITION_INFO);
            }
        }

        return positionSuccessorId;
    }

    public Integer findPositionSuccessorReadinessId(ResolverContext context) {
        Integer positionSuccessorReadinessId = null;

        if (Objects.nonNull(context.getParameters())) {
            if (context.getParameters().containsKey(POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_SUCCESSOR_READINESS_INFO)) {
                positionSuccessorReadinessId = (Integer) context.getParameters().get(POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_SUCCESSOR_READINESS_INFO);
            }
            if (context.getParameters().containsKey(POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO)) {
                positionSuccessorReadinessId = (Integer) context.getParameters().get(POSITION_SUCCESSOR_READINESS_ID_TO_POSITION_INFO);
            }
        }

        return positionSuccessorReadinessId;
    }

    public static Integer findTaskLinkId(ResolverContext context) {
        Integer taskLinkId = null;

        if (Objects.nonNull(context.getParameters())) {
            if (context.getParameters().containsKey(TASK_LINK_ID_TO_TASK_ID_TO_HOLDER_INFO)) {
                taskLinkId = (Integer) context.getParameters().get(TASK_LINK_ID_TO_TASK_ID_TO_HOLDER_INFO);
            }
            if (context.getParameters().containsKey(TASK_LINK_ID_TO_TASK_ID_FROM_HOLDER_INFO)) {
                taskLinkId = (Integer) context.getParameters().get(TASK_LINK_ID_TO_TASK_ID_FROM_HOLDER_INFO);
            }
        }

        return taskLinkId;
    }

    public static Integer findPollId(ResolverContext context) {
        Integer pollId = null;

        if (Objects.nonNull(context.getParameters())) {
            if (context.getParameters().containsKey(POLL_ID_TO_POLL_INFO)) {
                pollId = (Integer) context.getParameters().get(POLL_ID_TO_POLL_INFO);
            }
            if (context.getParameters().containsKey(POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL)) {
                pollId = (Integer) context.getParameters().get(POLL_ID_TO_COUNT_EMPLOYEE_USER_POLL);
            }
        }

        return pollId;
    }

    public List<DivisionTeamAssignmentDto> findRecipientByTaskIdHolderByLearningCourse(ResolverContext context) {
        TaskDto task = (TaskDto) context.getOrResolveObject(SavedObjectNames.TASK, () -> Optional.ofNullable(findTaskId(context))
            .map(taskId -> context.getResolverServiceContainer().getTasksettingServiceClient()
                .taskFind(new TaskFindRequest().setIds(Collections.singletonList(taskId.longValue()))))
            .filter(taskList -> !CollectionUtils.isEmpty(taskList))
            .map(taskList -> taskList.get(0))
            .orElse(null));

        if (task != null) {
            String tfValue = task.getFields()
                .stream()
                .filter(tf -> tf.getType().getId().equals(TASK_FIELD_TYPE_ID_342))
                .map(TaskFieldDto::getValue)
                .findFirst()
                .orElse(null);
            if (tfValue != null) {
                List<TaskDto> taskList = context.getResolverServiceContainer().getTasksettingServiceClient().taskFind(
                    new TaskFindRequest()
                        .setTaskType(Collections.singletonList(TASK_TYPE_ID_80))
                        .setTaskTypeFieldId(TASK_FIELD_TYPE_ID_140)
                        .setTaskFieldValue(tfValue)
                );
                if (taskList != null && !taskList.isEmpty()) {
                    return context.getResolverServiceContainer().getOrgstructureServiceAdapter().getAssignments(taskList.stream().map(TaskDto::getUserId).filter(Objects::nonNull).collect(Collectors.toList()), null);
                }
            }
        }
        return null;
    }

    public static Long findEventId(ResolverContext context) {
        Long eventId = null;

        if (Objects.nonNull(context.getParameters())) {
            if (context.getParameters().containsKey(EVENT_ID_TO_EVENT_INFO)) {
                eventId = (Long) context.getParameters().get(EVENT_ID_TO_EVENT_INFO);
            }
        }

        return eventId;
    }
}
