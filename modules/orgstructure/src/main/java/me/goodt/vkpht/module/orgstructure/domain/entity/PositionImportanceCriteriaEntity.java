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
@Table(name = "org_position_importance_criteria")
public class PositionImportanceCriteriaEntity extends DomainObject {

    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "position_id")
    private PositionEntity position;
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "importance_criteria_id")
    private ImportanceCriteriaEntity importanceCriteria;
    @Column(name = "weight", nullable = false, columnDefinition = "DECIMAL(6,2) NOT NULL DEFAULT 0")
    private float weight;
    @Column(name = "value", nullable = false, columnDefinition = "DECIMAL(6,2) NOT NULL DEFAULT 0")
    private float value;

    public PositionImportanceCriteriaEntity(Date dateFrom, Date dateTo, PositionEntity position, ImportanceCriteriaEntity importanceCriteria, float weight, float value) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.position = position;
        this.importanceCriteria = importanceCriteria;
        this.weight = weight;
        this.value = value;
    }
}
