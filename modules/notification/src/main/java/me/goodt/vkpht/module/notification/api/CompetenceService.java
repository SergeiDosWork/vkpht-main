package me.goodt.vkpht.module.notification.api;

import java.util.Collection;
import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.AssignmentsAccordanceDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.CompetenceNotificationDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.CompetenceProfileNotificationDto;
import me.goodt.vkpht.module.notification.api.dto.rtcore.CompetenceProfilePositionNotificationDto;

public interface CompetenceService {
	CompetenceNotificationDto findCompetenceById(Long id);

	List<CompetenceNotificationDto> findCompetenceListByIds(Collection<Long> ids);

	CompetenceProfilePositionNotificationDto findCompetenceProfilePositionById(Long id);

	CompetenceProfilePositionNotificationDto findOneCompetenceProfilePositionByCompetenceProfileId(Long competenceProfileId);

	CompetenceProfileNotificationDto findCompetenceProfileById(Long id);

	List<AssignmentsAccordanceDto> assignmentAccordance(Long[] assignmentsFrom, String[] assignmentsTo);
}
