package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.Date;

import me.goodt.vkpht.common.domain.entity.ArchivableEntity;
import me.goodt.vkpht.common.domain.entity.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "org_unit")
public class UnitEntity extends AbstractEntity<String> implements ArchivableEntity {

    @Id
    @Column(name = "code", nullable = false, length = 128)
    @Access(AccessType.PROPERTY)
    private String code;

    @Column(name = "date_from", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "description", nullable = false, length = 2048)
    private String description;

    @Column(name = "is_system", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isSystem;

    @Column(name = "update_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;

    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;

    @Column(name = "external_id", length = 128)
    private String externalId;

    @Override
    public String getId() {
        return code;
    }
}
