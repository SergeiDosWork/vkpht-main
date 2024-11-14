package me.goodt.vkpht.common.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScaleFilter {
    private String unitCode;
    @Builder.Default
    private boolean onlyActual = true;
}
