package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Table(name = "org_employee_status")
public class EmployeeStatusEntity extends DomainObject implements ArchivableEntity {

    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;

    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;

    @Column(name = "system_name", nullable = false, length = 64)
    private String systemName;

    @Column(name = "is_stake_free", nullable = false)
    private Boolean isFreeStake;

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
}
