package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_position_successor_readiness")
public class PositionSuccessorReadinessEntity extends DomainObject {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "position_successor_id")
    private PositionSuccessorEntity positionSuccessor;
    @Column(name = "position_successor_id", insertable = false, updatable = false)
    private Long positionSuccessorId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "readiness_id")
    private AssignmentReadinessEntity readiness;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    public PositionSuccessorReadinessEntity(PositionSuccessorEntity positionSuccessor, AssignmentReadinessEntity readiness, Date dateFrom, Date dateTo) {
        this.positionSuccessor = positionSuccessor;
        this.readiness = readiness;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
