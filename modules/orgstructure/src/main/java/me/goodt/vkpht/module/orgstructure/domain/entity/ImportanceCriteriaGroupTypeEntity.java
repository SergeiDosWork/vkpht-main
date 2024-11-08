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
@Table(name = "org_importance_criteria_group_type")
public class ImportanceCriteriaGroupTypeEntity extends DomainObject {
    @Column(name = "name", nullable = false, length = 256)
    private String name;

    public ImportanceCriteriaGroupTypeEntity(Long id, String name) {
        setId(id);
        this.name = name;
    }
}
