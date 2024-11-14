package me.goodt.vkpht.module.orgstructure.application.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.goodt.drive.rtcore.dto.tasksetting.*;
import com.goodt.drive.rtcore.service.IndicatorServiceImpl;
import com.goodt.drive.rtcore.service.tasksetting.task.OldTaskService;
import me.goodt.vkpht.common.domain.entity.rostalent.entities.IndicatorLevelEntity;
import me.goodt.vkpht.module.orgstructure.api.CompetenceService;
import me.goodt.vkpht.module.orgstructure.api.ParticipantService;
import me.goodt.vkpht.module.orgstructure.domain.dao.DivisionTeamAssignmentDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.DivisionTeamAssignmentEntity;
import me.goodt.vkpht.module.orgstructure.domain.entity.EmployeeEntity;

import static java.util.Arrays.asList;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_FIELD_TYPE_ID_218;
import static me.goodt.vkpht.common.application.util.GlobalDefs.TASK_TYPE_ID_173;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final OldTaskService taskService;
    private final DivisionTeamAssignmentDao divisionTeamAssignmentDao;
    private final CompetenceService competenceService;

    private final IndicatorServiceImpl indicatorService;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipantDto> getParticipants(Long employeeId, Long evaluationEventId) {
        Map<TaskDto, List<TaskDto>> tasks172by170 = taskService.getTasks172by170(evaluationEventId, Long.valueOf(employeeId));
        Map<Long, List<Long>> competencesByEmployee = taskService.getCompetencesByEmployee(true, tasks172by170);

        List<ParticipantDto> participants = new ArrayList<>();
        tasks172by170.keySet().stream().forEach(t -> {
            List<TaskDto> taskList = tasks172by170.get(t);

            if (CollectionUtils.isNotEmpty(taskList)) {
                List<DivisionTeamAssignmentEntity> dtas = divisionTeamAssignmentDao.findActualByIds(asList(t.getUserId()));
                ParticipantDto dto = new ParticipantDto();
                if (CollectionUtils.isNotEmpty(dtas)) {
                    EmployeeEntity e = dtas.get(0).getEmployee();
                    dto.setEmployee(new EmployeeDto(e.getId(),
                        e.getPerson().getName(),
                        e.getPerson().getSurname(),
                        e.getPerson().getPatronymic(),
                        e.getPerson().getPhoto(),
                        null));
                    dto.setProgress(getProgress(competencesByEmployee, taskList, e.getId()));
                }
                dto.setStatus(taskList.get(0).getStatus().getId());
                participants.add(dto);
            }
        });
        participants.sort(Comparator.comparing((ParticipantDto p) -> p.getEmployee().getSurname()));
        return participants;
    }

    private ProgressDto getProgress(Map<Long, List<Long>> competencesByEmployee,
                                    List<TaskDto> tsk,
                                    Long employeeId) {
        List<Long> ids172 = tsk.stream().map(e -> e.getId()).collect(Collectors.toList());
        TaskFindRequest criteria173 = new TaskFindRequest();
        criteria173.setTaskType(asList(TASK_TYPE_ID_173));
        criteria173.setParentIds(ids172);
        List<TaskDto> tasks = taskService.findTask(criteria173);

        List<Long> indicatorLevelsIds = tasks
            .stream()
            .flatMap(x -> x.getFields().stream())
            .filter(x -> x.getType().getId().equals(TASK_FIELD_TYPE_ID_218))
            .map(TaskFieldDto::getValue)
            .map(Long::valueOf)
            .collect(Collectors.toList());

        List<IndicatorLevelEntity> indicatorLevelEntities = indicatorService.getIndicatorLevels(indicatorLevelsIds);
        Long countCompetenceWithIndicator = indicatorLevelEntities
            .stream()
            .map(x -> x.getIndicator().getCompetenceId())
            .distinct()
            .count();


        Long plan = (long) competencesByEmployee.getOrDefault(employeeId, Collections.emptyList()).size();
        return new ProgressDto(countCompetenceWithIndicator, plan);
    }
}
