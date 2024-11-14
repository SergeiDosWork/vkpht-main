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
@Table(name = "org_project")
public class ProjectEntity extends DomainObject {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "parent_id")
    private ProjectEntity parent;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Column(name = "date_start", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    @Column(name = "date_end", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    @Column(name = "date_start_confirm", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStartConfirm;
    @Column(name = "date_end_confirm", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEndConfirm;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "type_id")
    private ProjectTypeEntity type;
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
    private ProjectStatusEntity projectStatus;
    @Column(name = "code", nullable = false, length = 128)
    private String code;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "cost_center_id")
    private CostCenterEntity costCenter;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public ProjectEntity(ProjectEntity parent, Date dateFrom, Date dateTo, Date dateStart, Date dateEnd,
                         Date dateStartConfirm, Date dateEndConfirm, ProjectTypeEntity type, String fullName,
                         String shortName, String abbreviation, String description, ProjectStatusEntity projectStatus,
                         String code, String externalId, CostCenterEntity costCenter) {
        this.parent = parent;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.dateStartConfirm = dateStartConfirm;
        this.dateEndConfirm = dateEndConfirm;
        this.type = type;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.description = description;
        this.projectStatus = projectStatus;
        this.code = code;
        this.externalId = externalId;
        this.costCenter = costCenter;
    }
}
