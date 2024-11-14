package me.goodt.vkpht.common.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LegalEntityFilter {
    private String unitCode;

    private String externalId;

    private List<Long> ids;

    @Builder.Default
    private Boolean actual = true;

    public static LegalEntityFilter asDefault() {
        return LegalEntityFilter.builder().build();
    }
}
