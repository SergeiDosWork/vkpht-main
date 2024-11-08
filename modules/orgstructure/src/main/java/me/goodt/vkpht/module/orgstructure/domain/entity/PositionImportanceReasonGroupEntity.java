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
@Table(name = "org_position_importance_reason_group")
public class PositionImportanceReasonGroupEntity extends DomainObject {

    @Column(name = "name", nullable = false, length = 256)
    private String name;
    @Column(name = "description", nullable = false, length = 256)
    private String description;
    @Column(name = "is_changeable", nullable = false, length = 10)
    private Long isChangeable;

    public PositionImportanceReasonGroupEntity(String name, String description, Long isChangeable) {
        this.name = name;
        this.description = description;
        this.isChangeable = isChangeable;
    }
}
