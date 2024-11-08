package me.goodt.vkpht.module.orgstructure.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WorkFunctionFilter {

    private String unitCode;

    @Builder.Default
    private Boolean actual = true;
}
