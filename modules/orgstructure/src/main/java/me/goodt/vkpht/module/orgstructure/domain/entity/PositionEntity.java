package me.goodt.vkpht.module.orgstructure.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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

import java.io.Serializable;
import java.util.Date;

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "org_position")
public class PositionEntity extends DomainObject implements Serializable, ArchivableEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "precursor_id")
    private PositionEntity precursor;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateTo;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "division_id", nullable = false)
    private DivisionEntity division;
    @Column(name = "division_id", insertable = false, updatable = false)
    private Long divisionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_title_id", nullable = false)
    private JobTitleEntity jobTitle;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "job_title_id", insertable = false, updatable = false)
    private Long jobTitleId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "workplace_id")
    private WorkplaceEntity workplace;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "work_function_id")
    private WorkFunctionEntity workFunction;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "work_function_id", insertable = false, updatable = false)
    private Long workFunctionId;
    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;
    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;
    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "category_id")
    private PositionCategoryEntity category;
    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "rank_id")
    private PositionRankEntity rank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private PositionStatusEntity status;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "position_type_id")
    private PositionTypeEntity positionType;
    @Column(name = "stake", nullable = false, precision = 4, scale = 2)
    private float stake;
    @Column(name = "is_key", nullable = false)
    private Integer isKey;
    @Column(name = "is_variable", nullable = false)
    private Integer isVariable;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "position_importance_id")
    private PositionImportanceEntity positionImportance;
    @Column(name = "position_importance_id", insertable = false, updatable = false)
    private Integer positionImportanceId;
    @Column(name = "is_key_management", nullable = false)
    private Integer isKeyManagement;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "profstandard_code", length = 16)
    private String profstandardCode;
    @Column(name = "profstandard_work_function_code", length = 16)
    private String profstandardWorkFunctionCode;
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "cost_center_id")
    private CostCenterEntity costCenter;
    @Column(name = "update_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public PositionEntity(PositionEntity precursor, Date dateFrom, Date dateTo,
                          DivisionEntity division, JobTitleEntity jobTitle, WorkplaceEntity workplace,
                          WorkFunctionEntity workFunction, String fullName, String shortName, String abbreviation,
                          PositionCategoryEntity category, PositionRankEntity rank, PositionStatusEntity status,
                          PositionTypeEntity positionType, float stake, Integer isKey, Integer isVariable,
                          PositionImportanceEntity positionImportance, Integer isKeyManagement, String externalId,
                          String profstandardCode, String profstandardWorkFunctionCode, CostCenterEntity costCenter,
                          Date updateDate, Long authorEmployeeId, Long updateEmployeeId) {
        this.precursor = precursor;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.division = division;
        this.jobTitle = jobTitle;
        this.workplace = workplace;
        this.workFunction = workFunction;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.category = category;
        this.rank = rank;
        this.status = status;
        this.positionType = positionType;
        this.stake = stake;
        this.isKey = isKey;
        this.isVariable = isVariable;
        this.positionImportance = positionImportance;
        this.isKeyManagement = isKeyManagement;
        this.externalId = externalId;
        this.profstandardCode = profstandardCode;
        this.profstandardWorkFunctionCode = profstandardWorkFunctionCode;
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
