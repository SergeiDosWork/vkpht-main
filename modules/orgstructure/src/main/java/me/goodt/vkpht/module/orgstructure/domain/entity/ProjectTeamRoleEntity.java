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
@Table(name = "org_project_team_role")
public class ProjectTeamRoleEntity extends DomainObject {

    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "project_team_id")
    private ProjectTeamEntity projectTeam;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "role_id")
    private RoleEntity role;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "type_id")
    private ProjectTeamRoleTypeEntity type;
    @Column(name = "full_name", nullable = false, length = 256)
    private String fullName;
    @Column(name = "short_name", nullable = false, length = 128)
    private String shortName;
    @Column(name = "abbreviation", nullable = false, length = 64)
    private String abbreviation;
    @Column(name = "description", nullable = false, length = 1024)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "status_id")
    private ProjectTeamRoleStatusEntity projectStatus;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "plan_fte", columnDefinition = "NUMERIC(5,2)", nullable = false)
    private Double planFte;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public ProjectTeamRoleEntity(Date dateFrom, Date dateTo, ProjectTeamRoleTypeEntity type, ProjectTeamEntity projectTeam, RoleEntity role, String fullName,
                                 String shortName, String abbreviation, String description, ProjectTeamRoleStatusEntity projectStatus, String externalId) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.type = type;
        this.projectTeam = projectTeam;
        this.role = role;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.description = description;
        this.projectStatus = projectStatus;
        this.externalId = externalId;

    }
}
