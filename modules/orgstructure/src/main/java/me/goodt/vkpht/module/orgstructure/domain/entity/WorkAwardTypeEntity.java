package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "org_work_award_type")
public class WorkAwardTypeEntity extends DomainObject {

    @Column(name = "name", nullable = false, length = 512, unique = true)
    private String name;

    @Column(name = "unit_code", nullable = false)
    private String unitCode;
}
