package me.goodt.vkpht.module.orgstructure.domain.entity;

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

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "org_division_team_assignment")
public class DivisionTeamAssignmentEntity extends DomainObject implements ArchivableEntity {

    /**
     * from DivisionTeamAssignmentEntity
     */
    @Column(name = "precursor_id")
    private Long precursorId;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private AssignmentTypeEntity type;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @Column(name = "employee_id", insertable = false, updatable = false)
    private Long employeeId;
    @ManyToOne
    @JoinColumn(name = "division_team_role_id", nullable = false, insertable = false, updatable = false)
    private DivisionTeamRoleEntity divisionTeamRole;
    @Column(name = "division_team_role_id", nullable = false)
    private Long divisionTeamRoleId;
    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;
    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;
    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;
    @ManyToOne(cascade = {CascadeType.ALL})
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

    public DivisionTeamAssignmentEntity(Long precursorId, Date dateFrom,
                                        Date dateTo, AssignmentTypeEntity type,
                                        EmployeeEntity employee, Long divisionTeamRoleId,
                                        String fullName, String shortName, String abbreviation,
                                        AssignmentStatusEntity status, String externalId) {
        this.precursorId = precursorId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.type = type;
        this.employee = employee;
        this.divisionTeamRoleId = divisionTeamRoleId;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.status = status;
        this.externalId = externalId;
    }

    @Override
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
