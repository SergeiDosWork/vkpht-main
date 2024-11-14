package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "org_project_team_role_position")
public class ProjectTeamRolePositionEntity extends DomainObject {

    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne
    @JoinColumn(name = "project_team_role_id", nullable = false)
    private ProjectTeamRoleEntity projectTeamRole;
    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private PositionEntity position;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "author_employee_id", columnDefinition = "BIGINT")
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", columnDefinition = "BIGINT")
    private Long updateEmployeeId;
    @Column(name = "update_date", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public ProjectTeamRolePositionEntity(Date dateFrom, Date dateTo, ProjectTeamRoleEntity projectTeamRole, PositionEntity position,
                                         String externalId, Long authorEmployeeId, Long updateEmployeeId, Date updateDate) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.projectTeamRole = projectTeamRole;
        this.position = position;
        this.externalId = externalId;
        this.authorEmployeeId = authorEmployeeId;
        this.updateEmployeeId = updateEmployeeId;
        this.updateDate = updateDate;
    }
}
