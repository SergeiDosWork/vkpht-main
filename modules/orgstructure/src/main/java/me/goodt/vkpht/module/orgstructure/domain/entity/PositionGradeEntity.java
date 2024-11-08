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
@Entity
@NoArgsConstructor
@Table(name = "org_position_grade")
public class PositionGradeEntity extends DomainObject {

    @Column(name = "name", nullable = false, length = 256)
    private String name;
    @Column(name = "description", nullable = false, length = 256)
    private String description;
    @Column(name = "index", nullable = false, length = 10)
    private Long index;
    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    public PositionGradeEntity(String name, String description, Long index) {
        this.name = name;
        this.description = description;
        this.index = index;
    }

}
