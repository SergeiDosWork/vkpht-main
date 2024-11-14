package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import me.goodt.vkpht.module.orgstructure.domain.entity.composite.key.DivisionLinksId;
import me.goodt.vkpht.common.domain.entity.AbstractEntity;

@Getter
@Setter
@Entity
@Immutable
@NoArgsConstructor
@IdClass(DivisionLinksId.class)
@Table(name = "org_division_links")
public class DivisionLinksEntity extends AbstractEntity<DivisionLinksId> {

    public DivisionLinksEntity(DivisionEntity parent, DivisionEntity child, Long levelToParent) {
        this.parent = parent;
        this.parentId = parent.getId();
        this.child = child;
        this.childId = child.getId();
        this.levelToParent = levelToParent;
    }

    @Id
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "parent_id")
    private Long parentId;

    @Id
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "child_id")
    private Long childId;

    @ManyToOne
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private DivisionEntity parent;

    @JoinColumn(name = "child_id", insertable = false, updatable = false)
    @ManyToOne
    private DivisionEntity child;

    @Column(name = "level_to_parent", nullable = false)
    private Long levelToParent;

    @Override
    public DivisionLinksId getId() {
        return new DivisionLinksId(parentId, childId);
    }
}
