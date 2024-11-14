package me.goodt.vkpht.module.orgstructure.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FunctionStatusFilter {
    private String unitCode;

    @Builder.Default
    private Boolean actual = true;

    private String name;

    private String externalId;
}
