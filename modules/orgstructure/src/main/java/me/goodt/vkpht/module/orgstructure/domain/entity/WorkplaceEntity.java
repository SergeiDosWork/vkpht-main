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

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_workplace")
public class WorkplaceEntity extends DomainObject implements ArchivableEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "precursor_id")
    private WorkplaceEntity precursor;
    @Column(name = "code", nullable = false, length = 32)
    private String code;
    @Column(name = "hash", nullable = false, length = 64)
    private String hash;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "location_id")
    private LocationEntity location;
    @Column(name = "number", nullable = false)
    private Integer number;
    @Column(name = "is_base", nullable = false)
    private Integer isBase;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;
    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    public WorkplaceEntity(WorkplaceEntity precursor, String code, String hash, LocationEntity location,
                           Integer number, Integer isBase) {
        this.precursor = precursor;
        this.code = code;
        this.hash = hash;
        this.location = location;
        this.number = number;
        this.isBase = isBase;
    }
}
