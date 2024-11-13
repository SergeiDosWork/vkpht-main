package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.Date;

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Table(name = "org_education_type")
public class EducationTypeEntity extends DomainObject implements ArchivableEntity {

    private String name;

    @Column(name = "external_id", unique = true, length = 128)
    private String externalId;

    @Column(name = "update_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;

    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    @Column(name = "date_from", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
}
