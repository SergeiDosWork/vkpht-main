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
@Table(name = "org_assignment_readiness")
public class AssignmentReadinessEntity extends IntDomainObject implements ArchivableEntity {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "unit_code", nullable = false, length = 128)
    private String unitCode;
    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    public AssignmentReadinessEntity(Integer id, String name, Date dateFrom, Date dateTo) {
        setId(id);
        this.name = name;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
