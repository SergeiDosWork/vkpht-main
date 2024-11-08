package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "org_division")
public class DivisionEntity extends DomainObject implements ArchivableEntity {

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private DivisionEntity parent;
    //DivisionEntity
    @Column(name = "parent_id")
    private Long parentId;
    //DivisionEntity
    @Column(name = "precursor_id", insertable = false, updatable = false)
    private Long precursorId;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Column(name = "date_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    @Column(name = "date_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    @Column(name = "date_start_confirm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStartConfirm;
    @Column(name = "date_end_confirm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEndConfirm;
    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_entity_id", insertable = false, updatable = false)
    private LegalEntityEntity legalEntityEntity;
    @Column(name = "legal_entity_id")
    private Long legalEntityId;
    @ManyToOne
    @JoinColumn(name = "structure_id")
    private StructureEntity structure;
    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;
    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;
    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;
    @Column(name = "description", length = 2048)
    private String description;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "status_id")
    private DivisionStatusEntity status;
    //DivisionGroupEntity
    @Column(name = "group_id", insertable = false, updatable = false)
    private Long groupId;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "cost_center_id")
    private CostCenterEntity costCenter;
    @Column(name = "update_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public DivisionEntity(Long parent, Long precursor,
                          Date dateFrom, Date dateTo, String fullName, String shortName,
                          Long legalEntity, StructureEntity structure,
                          String abbreviation, DivisionStatusEntity status, Long group,
                          String externalId, CostCenterEntity costCenter,
                          Date updateDate, Long authorEmployeeId, Long updateEmployeeId) {

        this.parentId = parent;
        this.precursorId = precursor;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.legalEntityId = legalEntity;
        this.structure = structure;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.status = status;
        this.groupId = group;
        this.externalId = externalId;
        this.costCenter = costCenter;
        this.updateDate = updateDate;
        this.authorEmployeeId = authorEmployeeId;
        this.updateEmployeeId = updateEmployeeId;
    }

    @Override
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
