package me.goodt.vkpht.module.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.AssignmentsAccordanceDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.CompetenceNotificationDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.CompetenceProfileNotificationDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.CompetenceProfilePositionNotificationDto;
import me.goodt.vkpht.module.notification.api.CompetenceService;
import me.goodt.vkpht.module.notification.api.rtcore.RtCoreServiceClient;

@Service
@RequiredArgsConstructor
public class CompetenceServiceImpl implements CompetenceService {

	private final RtCoreServiceClient rtCoreServiceClient;

	@Override
	public CompetenceNotificationDto findCompetenceById(Long id) {
		return rtCoreServiceClient.findCompetenceById(id);
	}

	@Override
	public List<CompetenceNotificationDto> findCompetenceListByIds(Collection<Long> ids) {
		return rtCoreServiceClient.findCompetenceListByIds(ids);
	}

	@Override
	public CompetenceProfilePositionNotificationDto findCompetenceProfilePositionById(Long id) {
		return rtCoreServiceClient.findCompetenceProfilePositionById(id);
	}

	@Override
	public CompetenceProfilePositionNotificationDto findOneCompetenceProfilePositionByCompetenceProfileId(Long competenceProfileId) {
		return rtCoreServiceClient.findOneCompetenceProfilePositionByCompetenceProfileId(competenceProfileId);
	}

	@Override
	public CompetenceProfileNotificationDto findCompetenceProfileById(Long id) {
		return rtCoreServiceClient.findCompetenceProfileById(id);
	}

	@Override
	public List<AssignmentsAccordanceDto> assignmentAccordance(Long[] assignmentsFrom, String[] assignmentsTo) {
		List<AssignmentsAccordanceDto> assignmentsAccordance = new ArrayList<>();
		for (Long assignmentFrom : assignmentsFrom) {
			for (String assignmentTo : assignmentsTo) {
				if (assignmentTo.equals("null")) {
					assignmentsAccordance.add(new AssignmentsAccordanceDto(new Long[]{assignmentFrom, null}, null));
				} else {
					assignmentsAccordance.add(new AssignmentsAccordanceDto(new Long[]{assignmentFrom, Long.valueOf(assignmentTo)}, 0f));
				}
			}
		}
		return assignmentsAccordance;
	}
}
