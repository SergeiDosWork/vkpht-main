package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Setter
@Getter
@Entity
@Accessors(chain = true)
@Table(name = "org_slot_rules")
public class KblSlotRules extends DomainObject {

    @Column(name = "is_enabled")
    private boolean enabled;
    @Column(name = "module")
    private String module;
    @Column(name = "slot_id")
    private String slot;
    @Column(name = "rule_id")
    private Integer number;
    @Column(name = "left_rule")
    private String leftRule;
    @Column(name = "lr_ds_or_kv")
    private String lrDsOrKv;
    @Column(name = "operator")
    private String operator;
    @Column(name = "right_rule")
    private String rightRule;
    @Column(name = "rr_ds_or_kv")
    private String rrDsOrKv;
}
