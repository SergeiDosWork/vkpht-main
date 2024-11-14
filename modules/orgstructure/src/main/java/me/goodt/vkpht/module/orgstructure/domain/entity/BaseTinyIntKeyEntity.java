package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import me.goodt.vkpht.common.domain.entity.BaseEntity;
import me.goodt.vkpht.common.domain.entity.AbstractEntity;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseTinyIntKeyEntity extends AbstractEntity<Integer> {

    @Id
    @Column(name = "id", columnDefinition = "SMALLINT")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = BaseEntity.SEQUENCE_BASE_GENERATOR)
    @Access(AccessType.PROPERTY)
    private Integer id;

    public BaseTinyIntKeyEntity(Integer id) {
        setId(id);
    }
}
