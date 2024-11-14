package me.goodt.vkpht.module.orgstructure.domain.dao.simple;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class JobTitleSmp {

    private Long id;

    private String fullName;

    private String shortName;
}
