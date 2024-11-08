package me.goodt.vkpht.module.orgstructure.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PositionGroupFilter {
    private String unitCode;
    @Builder.Default
    private Boolean actual = true;
}
