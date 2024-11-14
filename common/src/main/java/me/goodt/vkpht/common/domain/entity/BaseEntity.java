package me.goodt.vkpht.common.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.*;

import me.goodt.micro.core.entity.AbstractEntity;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity extends AbstractEntity<Long> implements Persistable<Long> {

    public static final String SEQUENCE_BASE_GENERATOR = "base_generator";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_BASE_GENERATOR)
    @Column(unique = true, nullable = false)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Transient
    private boolean isNew = true;

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    public BaseEntity setId(Long id) {
        this.id = id;
        this.markNotNew();
        return this;
    }
}
