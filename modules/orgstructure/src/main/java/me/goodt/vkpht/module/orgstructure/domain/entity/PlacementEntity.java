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
@Table(name = "org_placement")
public class PlacementEntity extends DomainObject implements ArchivableEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "precursor_id")
    private PlacementEntity precursor;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "workplace_id")
    private WorkplaceEntity workplace;
    @Column(name = "code", nullable = false, length = 32)
    private String code;
    @Column(name = "name", nullable = false, length = 64)
    private String name;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public PlacementEntity(PlacementEntity precursor, Date dateFrom, Date dateTo,
                           WorkplaceEntity workplace, String code, String name) {
        this.precursor = precursor;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.workplace = workplace;
        this.code = code;
        this.name = name;
    }
}
