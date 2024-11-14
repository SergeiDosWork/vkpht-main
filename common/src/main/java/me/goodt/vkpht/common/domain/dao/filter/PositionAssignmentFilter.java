package me.goodt.vkpht.common.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PositionAssignmentFilter {
    public String unitCode;

    @Builder.Default
    public Boolean actual = true;

    public List<Long> positions;

    public Long employeeId;

    public Long positionId;

    public Long legalEntityId;
}
