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
@Table(name = "org_position_group")
public class PositionGroupEntity extends DomainObject implements ArchivableEntity {

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "date_from", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @Column(name = "unit_code", nullable = false)
    private String unitCode;
}
