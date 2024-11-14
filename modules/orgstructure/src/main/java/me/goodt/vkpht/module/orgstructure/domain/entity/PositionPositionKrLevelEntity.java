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

import me.goodt.vkpht.module.orgstructure.domain.entity.composite.key.PositionPositionKrLevelId;
import me.goodt.vkpht.common.domain.entity.AbstractEntity;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_position_position_kr_level")
public class PositionPositionKrLevelEntity extends AbstractEntity<PositionPositionKrLevelId> {

    @EmbeddedId
    private PositionPositionKrLevelId id;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", nullable = true, columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    public PositionPositionKrLevelEntity(PositionPositionKrLevelId id) {
        this.id = id;
    }
}
