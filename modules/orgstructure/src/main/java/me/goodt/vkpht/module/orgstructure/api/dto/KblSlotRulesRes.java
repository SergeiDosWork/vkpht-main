package me.goodt.vkpht.module.orgstructure.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import me.goodt.vkpht.common.dictionary.core.dto.AbstractRes;

@Setter
@Getter
@Accessors(chain = true)
public class KblSlotRulesRes extends AbstractRes<KblSlotRulesRes> {

    private boolean enabled;

    private String module;

    private String slot;

    private Integer number;

    private String leftRule;

    private String lrDsOrKv;

    private String operator;

    private String rightRule;

    private String rrDsOrKv;
}
