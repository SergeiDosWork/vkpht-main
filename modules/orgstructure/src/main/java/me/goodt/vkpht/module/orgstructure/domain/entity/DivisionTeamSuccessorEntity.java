package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.AccessLevel;
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
@Table(name = "org_division_team_successor")
public class DivisionTeamSuccessorEntity extends DomainObject {

    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Column(name = "date_commit_hr", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCommitHr;
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @Setter(AccessLevel.NONE)
    @Column(name = "employee_id", insertable = false, updatable = false)
    private Long employeeId;
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "division_team_role_id")
    private DivisionTeamRoleEntity divisionTeamRole;
    @Setter(AccessLevel.NONE)
    @Column(name = "division_team_role_id", insertable = false, updatable = false)
    private Long divisionTeamRoleId;
    @Column(name = "date_priority", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePriority;
    @Column(name = "reason_id_inclusion")
    private Integer reasonIdInclusion;
    @Column(name = "reason_id_exclusion")
    private Integer reasonIdExclusion;
    @Column(name = "comment_inclusion", length = 1024)
    private String commentInclusion;
    @Column(name = "comment_exclusion", length = 1024)
    private String commentExclusion;
    @Column(name = "document_url_inclusion", length = 512)
    private String documentUrlInclusion;
    @Column(name = "document_url_exclusion", length = 512)
    private String documentUrlExclusion;

    public DivisionTeamSuccessorEntity(Date dateFrom, Date dateTo, Date dateCommitHr, EmployeeEntity employee, DivisionTeamRoleEntity divisionTeamRole,
                                       Date datePriority, Integer reasonIdInclusion, Integer reasonIdExclusion,
                                       String commentInclusion, String commentExclusion, String documentUrlInclusion, String documentUrlExclusion) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.dateCommitHr = dateCommitHr;
        this.employee = employee;
        this.divisionTeamRole = divisionTeamRole;
        this.datePriority = datePriority;
        this.reasonIdInclusion = reasonIdInclusion;
        this.reasonIdExclusion = reasonIdExclusion;
        this.commentInclusion = commentInclusion;
        this.commentExclusion = commentExclusion;
        this.documentUrlInclusion = documentUrlInclusion;
        this.documentUrlExclusion = documentUrlExclusion;
    }
}
