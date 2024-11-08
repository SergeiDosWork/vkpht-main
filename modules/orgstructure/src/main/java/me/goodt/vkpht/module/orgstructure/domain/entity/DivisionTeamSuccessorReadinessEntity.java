package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "org_division_team_successor_readiness")
public class DivisionTeamSuccessorReadinessEntity extends DomainObject {

    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "division_team_successor_id")
    private DivisionTeamSuccessorEntity divisionTeamSuccessor;
    @ManyToOne
    @JoinColumn(name = "readiness_id")
    private AssignmentReadinessEntity readiness;

    public DivisionTeamSuccessorReadinessEntity(Date dateFrom, Date dateTo,
                                                DivisionTeamSuccessorEntity divisionTeamSuccessor, AssignmentReadinessEntity readiness) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.divisionTeamSuccessor = divisionTeamSuccessor;
        this.readiness = readiness;
    }
}
