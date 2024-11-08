package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_importance_criteria")
public class ImportanceCriteriaEntity extends DomainObject {
    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private ImportanceCriteriaGroupEntity group;
    @Column(name = "name", nullable = false, length = 256)
    private String name;
    @Column(name = "description", nullable = false, length = 1024)
    private String description;
    @Column(name = "weight", nullable = false, columnDefinition = "DECIMAL(6,2) NOT NULL DEFAULT 0")
    private float weight;
    @ManyToOne
    @JoinColumn(name = "calculation_method_id")
    private CalculationMethodEntity calculationMethod;
    @Column(name = "is_enabled")
    private Boolean isEnabled;

    public ImportanceCriteriaEntity(ImportanceCriteriaGroupEntity group, String name, String description, float weight, CalculationMethodEntity calculationMethod, Boolean isEnabled) {
        this.group = group;
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.calculationMethod = calculationMethod;
        this.isEnabled = isEnabled;
    }
}
