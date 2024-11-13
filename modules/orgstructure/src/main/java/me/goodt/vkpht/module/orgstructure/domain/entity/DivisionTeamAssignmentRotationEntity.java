package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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

/**
 * @author Pavel Khovaylo
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "org_division_team_assignment_rotation")
public class DivisionTeamAssignmentRotationEntity extends DomainObject {

    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Column(name = "date_commit_hr", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCommitHr;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "division_team_assignment_id", nullable = false)
    private DivisionTeamAssignmentShort assignment;
    @Setter(AccessLevel.NONE)
    @Column(name = "division_team_assignment_id", insertable = false, updatable = false)
    private Long assignmentId;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "rotation_id", nullable = false)
    private AssignmentRotationEntity rotation;
    @Column(name = "comment_hr", length = 512)
    private String commentHr;
    @Column(name = "comment_employee", length = 512)
    private String commentEmployee;

    public DivisionTeamAssignmentRotationEntity(Date dateFrom, Date dateTo, Date dateCommitHr,
                                                DivisionTeamAssignmentShort assignment,
                                                AssignmentRotationEntity rotation,
                                                String commentHr, String commentEmployee) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.dateCommitHr = dateCommitHr;
        this.assignment = assignment;
        this.rotation = rotation;
        this.commentHr = commentHr;
        this.commentEmployee = commentEmployee;
    }
}
