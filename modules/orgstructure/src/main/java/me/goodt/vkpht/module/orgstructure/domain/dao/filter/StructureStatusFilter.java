package me.goodt.vkpht.module.orgstructure.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StructureStatusFilter {
    public String name;

    public String externalId;

    @Builder.Default
    public Boolean actual = true;

    public static StructureStatusFilter asDefault() {
        return StructureStatusFilter.builder().build();
    }
}
