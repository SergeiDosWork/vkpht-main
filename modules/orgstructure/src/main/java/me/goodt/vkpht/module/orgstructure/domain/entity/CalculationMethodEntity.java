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
@Table(name = "org_calculation_method")
public class CalculationMethodEntity extends DomainObject {

    @Column(name = "code ", nullable = false, length = 64)
    private String code;
    @Column(name = "name", nullable = false, length = 128)
    private String name;
    @Column(name = "description", nullable = false, length = 128)
    private String description;

    public CalculationMethodEntity(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
}
