package me.goodt.vkpht.module.orgstructure.domain.dao.simple;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class DivisionSmp {

    private Long id;

    private Long parentId;

    private Long legalEntityId;

    private String fullName;

    private String shortName;
}
