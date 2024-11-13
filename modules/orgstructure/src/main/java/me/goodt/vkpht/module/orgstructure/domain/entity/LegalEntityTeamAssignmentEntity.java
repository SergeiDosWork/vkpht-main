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
@Table(name = "org_legal_entity_team_assignment")
public class LegalEntityTeamAssignmentEntity extends DomainObject {

    @Column(name = "precursor_id")
    private Long precursorId;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "type_id")
    private AssignmentTypeEntity type;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @Column(name = "employee_id", insertable = false, updatable = false)
    private Long employeeId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "legal_entity_team_id")
    private LegalEntityTeamEntity legalEntityTeamEntity;
    @Setter(AccessLevel.NONE)
    @Column(name = "legal_entity_team_id", insertable = false, updatable = false)
    private Long legalEntityTeamId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "role_id")
    private RoleEntity role;
    @Column(name = "role_id", insertable = false, updatable = false)
    private Long roleId;
    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;
    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;
    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "status_id")
    private AssignmentStatusEntity status;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public LegalEntityTeamAssignmentEntity(Long precursorId,
                                           Date dateFrom, Date dateTo, AssignmentTypeEntity type,
                                           EmployeeEntity employee, LegalEntityTeamEntity legalEntityTeamEntity, RoleEntity role,
                                           String fullName, String shortName, String abbreviation,
                                           AssignmentStatusEntity status) {
        this.precursorId = precursorId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.type = type;
        this.employee = employee;
        this.legalEntityTeamEntity = legalEntityTeamEntity;
        this.role = role;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.status = status;
    }
}
