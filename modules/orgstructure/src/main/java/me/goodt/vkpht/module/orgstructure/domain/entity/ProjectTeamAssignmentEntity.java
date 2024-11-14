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
@Table(name = "org_project_team_assignment")
public class ProjectTeamAssignmentEntity extends DomainObject {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "precursor_id")
    private ProjectTeamAssignmentEntity precursor;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "type_id")
    private ProjectTeamAssignmentTypeEntity type;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "project_team_role_id")
    private ProjectTeamRoleEntity projectTeamRole;
    @Column(name = "full_name", nullable = false, length = 256)
    private String fullName;
    @Column(name = "short_name", nullable = false, length = 128)
    private String shortName;
    @Column(name = "abbreviation", nullable = false, length = 64)
    private String abbreviation;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "status_id")
    private ProjectTeamAssignmentStatusEntity projectStatus;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "percent_fte")
    private Long percentFte;
    @Column(name = "comment_hr", length = 512)
    private String commentHr;
    @Column(name = "plan_employment_date", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date planEmploymentDate;
    @Column(name = "author_employee_id", columnDefinition = "BIGINT", nullable = false)
    private Double authorEmployeeId;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public ProjectTeamAssignmentEntity(ProjectTeamAssignmentEntity precursor, Date dateFrom, Date dateTo,
                                       ProjectTeamAssignmentTypeEntity type, ProjectTeamRoleEntity projectTeamRole,
                                       EmployeeEntity employee, String fullName, String shortName, String abbreviation,
                                       ProjectTeamAssignmentStatusEntity projectStatus, String externalId, Long percentFte) {
        this.precursor = precursor;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.type = type;
        this.projectTeamRole = projectTeamRole;
        this.employee = employee;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.projectStatus = projectStatus;
        this.externalId = externalId;
        this.percentFte = percentFte;
    }
}
