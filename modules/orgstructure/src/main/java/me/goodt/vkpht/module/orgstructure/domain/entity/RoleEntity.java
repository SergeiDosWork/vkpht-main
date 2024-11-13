package me.goodt.vkpht.module.orgstructure.domain.entity;

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
@Table(name = "org_role")
public class RoleEntity extends DomainObject implements ArchivableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_role_id")
    private SystemRoleEntity systemRole;
    @Column(name = "system_role_id", insertable = false, updatable = false)
    private Integer systemRoleId;
    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;
    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;
    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;
    @Column(name = "code", length = 128)
    private String code;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", nullable = false)
    private Date updateDate;
    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;
    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;
    @Column(name = "external_id", length = 128)
    private String externalId;

    public RoleEntity(SystemRoleEntity systemRole, String fullName, String shortName, String abbreviation, String code,
                      Date dateFrom, Date dateTo, Date updateDate, Long authorEmployeeId, Long updateEmployeeId) {
        this.systemRole = systemRole;
        this.fullName = fullName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
        this.code = code;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.updateDate = updateDate;
        this.authorEmployeeId = authorEmployeeId;
        this.updateEmployeeId = updateEmployeeId;
    }
}
