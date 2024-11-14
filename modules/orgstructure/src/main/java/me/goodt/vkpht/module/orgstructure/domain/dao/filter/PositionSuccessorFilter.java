package me.goodt.vkpht.module.orgstructure.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PositionSuccessorFilter {
    private Long employeeId;

    @Builder.Default
    private Boolean actual = true;
}
