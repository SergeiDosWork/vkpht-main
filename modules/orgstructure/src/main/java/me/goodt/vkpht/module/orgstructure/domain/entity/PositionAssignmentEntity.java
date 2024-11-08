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
import java.io.Serializable;
import java.util.Date;

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "org_position_assignment")
public class PositionAssignmentEntity extends DomainObject implements Serializable, ArchivableEntity {

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "type_id", nullable = false)
    private AssignmentTypeEntity type;
    @Column(name = "type_id", insertable = false, updatable = false)
    private Integer typeId;
    /** join PositionAssignmentEntity */
    @Column(name = "precursor_id")
    private Long precursorId;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;
    @Setter(AccessLevel.NONE)
    @Column(name = "employee_id", insertable = false, updatable = false)
    private Long employeeId;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "position_id", nullable = false)
    private PositionEntity position;
    @Setter(AccessLevel.NONE)
    @Column(name = "position_id", insertable = false, updatable = false)
    private Long positionId;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "status_id", nullable = false)
    private AssignmentStatusEntity status;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "placement_id")
    private PlacementEntity placement;
    @ManyToOne
    @JoinColumn(name = "substitution_type_id")
    private SubstitutionTypeEntity substitutionType;
    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;
    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;
    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;
    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private AssignmentCategoryEntity category;
    @Column(name = "stake", nullable = false, precision = 4, scale = 2)
    private float stake;
    @Column(name = "probation_date_to", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date probationDateTo;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public PositionAssignmentEntity(AssignmentTypeEntity type, Long precursor,
                                    Date dateFrom, Date dateTo,
                                    EmployeeEntity employee, PositionEntity position, AssignmentStatusEntity status,
                                    PlacementEntity placement, SubstitutionTypeEntity substitutionType,
                                    String fullName, String shortName, String abbreviation, AssignmentCategoryEntity category,
                                    float stake, Date probationDateTo, String externalId) {
        this.type = type;
        this.precursorId = precursor;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.employee = employee;
        this.position = position;
        this.status = status;
        this.placement = placement;
        this.substitutionType = substitutionType;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.category = category;
        this.stake = stake;
        this.probationDateTo = probationDateTo;
        this.externalId = externalId;
    }

    public SubstitutionTypeEntity getSubstitutionType() {
        return substitutionType;
    }

    @Override
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
