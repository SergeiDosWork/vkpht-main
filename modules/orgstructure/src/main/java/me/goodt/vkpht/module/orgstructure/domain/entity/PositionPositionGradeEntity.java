package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

import me.goodt.vkpht.module.orgstructure.domain.entity.composite.key.PositionPositionGradeId;
import me.goodt.vkpht.common.domain.entity.AbstractEntity;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_position_position_grade")
public class PositionPositionGradeEntity extends AbstractEntity<PositionPositionGradeId> {

    @EmbeddedId
    private PositionPositionGradeId id;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    public PositionPositionGradeEntity(PositionPositionGradeId id) {
        this.id = id;
    }
}
