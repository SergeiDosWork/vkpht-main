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
@Table(name = "org_product_status")
public class ProductStatusEntity extends IntDomainObject {

    @Column(name = "name", nullable = false, unique = true, length = 64)
    private String name;

    public ProductStatusEntity(Integer id, String name) {
        setId(id);
        this.name = name;
    }
}
