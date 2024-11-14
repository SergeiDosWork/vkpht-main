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
@Table(name = "org_reason_type")
public class OrgReasonTypeEntity extends DomainObject {

    @Column(name = "name", nullable = false, unique = true, length = 128)
    private String name;

    public OrgReasonTypeEntity(Long id, String name) {
        setId(id);
        this.name = name;
    }
}
