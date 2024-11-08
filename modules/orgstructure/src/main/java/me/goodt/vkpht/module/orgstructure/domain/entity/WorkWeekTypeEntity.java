package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import me.goodt.vkpht.common.domain.entity.IntDomainObject;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_work_week_type")
public class WorkWeekTypeEntity extends IntDomainObject {

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "unit_code", nullable = false, length = 128)
    private String unitCode;

    public WorkWeekTypeEntity(Integer id, String name) {
        setId(id);
        this.name = name;
    }
}
