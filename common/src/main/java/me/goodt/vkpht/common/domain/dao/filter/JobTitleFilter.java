package me.goodt.vkpht.common.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

@Getter
@Builder
public class JobTitleFilter {
    private String unitCode;

    private Collection<Long> ids;

    @Builder.Default
    private Boolean actual = true;
}
