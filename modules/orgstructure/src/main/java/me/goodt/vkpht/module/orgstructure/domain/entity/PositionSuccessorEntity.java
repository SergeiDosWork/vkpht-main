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
@Table(name = "org_position_successor")
public class PositionSuccessorEntity extends DomainObject {

    @Column(name = "date_commit_hr", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCommitHr;
    @Column(name = "date_priority", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePriority;
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @Column(name = "employee_id", insertable = false, updatable = false)
    private Long employeeId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "position_id")
    private PositionEntity position;
    @Column(name = "position_id", insertable = false, updatable = false)
    private Long positionId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "position_group_id")
    private PositionGroupEntity positionGroup;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "reason_id_inclusion")
    private OrgReasonEntity reasonInclusion;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "reason_id_exclusion")
    private OrgReasonEntity reasonExclusion;
    @Column(name = "comment_inclusion", length = 1024)
    private String commentInclusion;
    @Column(name = "comment_exclusion", length = 1024)
    private String commentExclusion;
    @Column(name = "document_url_inclusion", length = 512)
    private String documentUrlInclusion;
    @Column(name = "document_url_exclusion", length = 512)
    private String documentUrlExclusion;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    public PositionSuccessorEntity(Date dateCommitHr, Date datePriority, EmployeeEntity employee,
                                   PositionEntity position, PositionGroupEntity positionGroup,
                                   OrgReasonEntity reasonInclusion, OrgReasonEntity reasonExclusion,
                                   String commentInclusion, String commentExclusion,
                                   String documentUrlInclusion, String documentUrlExclusion,
                                   Date dateFrom, Date dateTo) {
        this.dateCommitHr = dateCommitHr;
        this.datePriority = datePriority;
        this.employee = employee;
        this.position = position;
        this.positionGroup = positionGroup;
        this.reasonInclusion = reasonInclusion;
        this.reasonExclusion = reasonExclusion;
        this.commentInclusion = commentInclusion;
        this.commentExclusion = commentExclusion;
        this.documentUrlInclusion = documentUrlInclusion;
        this.documentUrlExclusion = documentUrlExclusion;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
