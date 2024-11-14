package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_importance_criteria_group")
public class ImportanceCriteriaGroupEntity extends DomainObject {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private ImportanceCriteriaGroupTypeEntity typeId;
    @Column(name = "name", nullable = false, length = 128)
    private String name;
    @Column(name = "description", nullable = false, length = 1024)
    private String description;
    @Column(name = "is_editable", nullable = false, columnDefinition = "smallint")
    private short isEditable;
    @Column(name = "unit_code", nullable = false, length = 128)
    private String unitCode;

    public ImportanceCriteriaGroupEntity(ImportanceCriteriaGroupTypeEntity typeId, String name, String description, short isEditable) {
        this.typeId = typeId;
        this.name = name;
        this.description = description;
        this.isEditable = isEditable;
    }
}
