package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DivisionWithDivisionTeamsStatDto extends DivisionDto {

    public DivisionWithDivisionTeamsStatDto(Long id, Long parentId, Long precursorId, Date dateFrom, Date dateTo,
                                            Long legalEntityId, Long structureId, String fullName, String shortName,
                                            String abbreviation, Long statusId, Long groupId, String externalId,
                                            List<DivisionTeamStatDto> divisionTeams, Long costCenterId) {
        super(id, parentId, precursorId, dateFrom, dateTo, legalEntityId, structureId, fullName, shortName,
            abbreviation, statusId, groupId, externalId, costCenterId);
        this.divisionTeams = divisionTeams;
    }

    private List<DivisionTeamStatDto> divisionTeams;
}
