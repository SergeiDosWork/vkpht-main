package me.goodt.vkpht.module.notification.api;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.data.DataFromStatusChangeToRostalentStatusChange;
import me.goodt.vkpht.module.notification.api.dto.data.OperationResult;
import me.goodt.vkpht.module.notification.api.dto.BaseNotificationInputData;
import me.goodt.vkpht.module.notification.api.dto.CustomEmailEventCodeInputDto;
import me.goodt.vkpht.module.notification.api.dto.CustomEmailEventInputDto;
import me.goodt.vkpht.module.notification.api.dto.DataForKafkaMessageInputDto;
import me.goodt.vkpht.module.notification.api.dto.EventInputDto;
import me.goodt.vkpht.module.notification.api.dto.QuizFinishInputDto;
import me.goodt.vkpht.module.notification.api.dto.QuizStartInputDto;
import me.goodt.vkpht.module.notification.api.dto.RemindByHeadInputDto;
import me.goodt.vkpht.module.notification.api.dto.SuccessorNotificateByCodeInputData;
import me.goodt.vkpht.module.notification.api.dto.monitor.EventDto;

public interface EventService {
	EventDto expertTaskCreate(EventInputDto input) throws JsonProcessingException;

	EventDto employeeHeadExpertReject(EventInputDto input) throws JsonProcessingException;

	EventDto expertHeadNotice(EventInputDto input) throws JsonProcessingException;

	List<EventDto> expertHeadReject(EventInputDto input) throws JsonProcessingException;

	List<EventDto> hrExpertReject(EventInputDto input) throws JsonProcessingException;

	List<EventDto> hrExpertAccept(EventInputDto input) throws JsonProcessingException;

	List<EventDto> hrExpertNotice(EventInputDto input) throws JsonProcessingException;

	List<EventDto> updateDivisionTeamSuccessorReadiness(EventInputDto input);

	List<EventDto> updateDivisionTeamAssignmentRotation(EventInputDto input) throws JsonProcessingException;

	List<EventDto> updateSuccessor(EventInputDto input);

	void noticeUpdateDivisionTeamSuccessorReadiness();

	List<EventDto> taskStatusChange(DataFromStatusChangeToRostalentStatusChange data) throws JsonProcessingException;

	OperationResult quizStart(QuizStartInputDto data);

	OperationResult quizFinish(QuizFinishInputDto data);

	OperationResult customEmailEvent(CustomEmailEventInputDto data);

	OperationResult customEmailEventCode(CustomEmailEventCodeInputDto data);

	OperationResult successorNotificateByCode(SuccessorNotificateByCodeInputData data);

	OperationResult baseNotification(BaseNotificationInputData data);

	OperationResult onboardingResultsHead(List<Long> divisionTeamAssignmentIds);

	OperationResult sendKafkaMessage(DataForKafkaMessageInputDto data);

	OperationResult remindByHead(RemindByHeadInputDto data);

	EventDto updateDtaRotation(EventInputDto input);

	EventDto updateDtaRotationCompleted(EventInputDto input);

	void executeBaseNotificationForEventCreate(Long eventTypeId, Long eventId);
}
