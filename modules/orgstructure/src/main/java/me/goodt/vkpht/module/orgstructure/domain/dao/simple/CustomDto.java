package me.goodt.vkpht.module.orgstructure.domain.dao.simple;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CustomDto extends BaseDynamicObject {

    private Long id;
}