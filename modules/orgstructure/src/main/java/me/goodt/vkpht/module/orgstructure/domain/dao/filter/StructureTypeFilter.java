package me.goodt.vkpht.module.orgstructure.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StructureTypeFilter {
    public String name;

    public String externalId;

    @Builder.Default
    public Boolean actual = true;

    public static StructureTypeFilter asDefault() {
        return StructureTypeFilter.builder().build();
    }
}
