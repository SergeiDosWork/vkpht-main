package me.goodt.vkpht.module.orgstructure.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DivisionFilter {
    private String unitCode;

    private Long legalEntityId;

    private Long divisionTeamId;

    private List<Long> parentIds;

    private Long positionId;

    @Builder.Default
    private Boolean actual = true;

    private List<Long> divisionIds;

    private List<Long> groupIds;
}
