package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Immutable
@NoArgsConstructor
@Table(name = "org_legal_entity")
public class LegalShort extends DomainObject {

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "precursor_id")
    private Long precursorId;

    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "is_affiliate", nullable = false)
    private Integer isAffiliate;

    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;

    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;

    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;

    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "external_id", length = 128)
    private String externalId;

    @Column(name = "cost_center_id")
    private Long costCenterId;
}
