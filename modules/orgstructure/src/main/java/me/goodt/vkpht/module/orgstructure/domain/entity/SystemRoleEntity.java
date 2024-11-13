package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.domain.entity.IntDomainObject;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_system_role")
public class SystemRoleEntity extends IntDomainObject implements ArchivableEntity {

    @Column(name = "name", nullable = false, unique = true, length = 128)
    private String name;

    @Column(name = "is_assignable", nullable = false)
    private Integer isAssignable;

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

    public void setAssignable(Boolean isAssignable) {
        this.isAssignable = Boolean.TRUE.equals(isAssignable) ? 1 : 0;
    }

    public Boolean getAssignable() {
        return isAssignable != null && isAssignable == 1;
    }
}
