package me.goodt.vkpht.module.orgstructure.domain.dao.filter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StructureFilter {
    public String unitCode;

    public Integer statusId;

    public Long locationId;

    public Long precursorId;

    public Long parentId;

    public Integer typeId;

    public String fullName;

    public String shortName;

    @Builder.Default
    public Boolean actual = true;
}
