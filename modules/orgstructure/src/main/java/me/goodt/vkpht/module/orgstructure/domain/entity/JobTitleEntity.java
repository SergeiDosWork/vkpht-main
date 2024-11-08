package me.goodt.vkpht.module.orgstructure.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "org_job_title")
public class JobTitleEntity extends DomainObject implements ArchivableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "precursor_id")
    private JobTitleEntity precursor;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateTo;
    @Column(name = "unit_code", nullable = false, length = 128)
    private String unitCode;
    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;
    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;
    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;
    @Column(name = "code", nullable = false, length = 32)
    private String code;
    @Column(name = "hash", nullable = false, length = 64)
    private String hash;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id")
    private JobTitleClusterEntity cluster;
    @Column(name = "is_required_certificate", nullable = false)
    private Integer isRequiredCertificate;
    @Column(name = "external_id", length = 128)
    private String externalId;
    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    public JobTitleEntity(JobTitleEntity precursor, Date dateFrom, Date dateTo,
                          String fullName, String shortName, String abbreviation,
                          String code, String hash, JobTitleClusterEntity cluster,
                          Integer isRequiredCertificate, String externalId) {
        this.precursor = precursor;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.code = code;
        this.hash = hash;
        this.cluster = cluster;
        this.isRequiredCertificate = isRequiredCertificate;
        this.externalId = externalId;
    }
}
