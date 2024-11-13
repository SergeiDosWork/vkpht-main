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

@Getter
@Setter
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "org_division_team")
public class DivisionTeamEntity extends DomainObject {

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "parent_id")
    private DivisionTeamEntity parent;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "precursor_id")
    private DivisionTeamEntity precursor;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "division_id")
    private DivisionEntity division;
    @Column(name = "division_id", insertable = false, updatable = false)
    private Long divisionId;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "type_id")
    private TeamTypeEntity type;
    @Column(name = "type_id", insertable = false, updatable = false)
    private Integer typeId;
    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;
    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;
    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "status_id")
    private TeamStatusEntity status;
    @Column(name = "is_head", nullable = false)
    private Integer isHead;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", nullable = false)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public DivisionTeamEntity(DivisionTeamEntity parent, DivisionTeamEntity precursor,
                              Date dateFrom, Date dateTo, DivisionEntity division,
                              TeamTypeEntity type, String fullName, String shortName, String abbreviation,
                              TeamStatusEntity status, Integer isHead, String externalId,
                              Date updateDate, Long authorEmployeeId, Long updateEmployeeId) {
        this.parent = parent;
        this.precursor = precursor;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.division = division;
        this.type = type;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.status = status;
        this.isHead = isHead;
        this.externalId = externalId;
        this.updateDate = updateDate;
        this.authorEmployeeId = authorEmployeeId;
        this.updateEmployeeId = updateEmployeeId;
    }
}
