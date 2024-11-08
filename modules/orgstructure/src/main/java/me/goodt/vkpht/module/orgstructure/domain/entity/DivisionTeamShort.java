package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Setter
@Getter
@Entity
@Immutable
@Table(name = "org_division_team")
public class DivisionTeamShort extends DomainObject {

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

    @Column(name = "division_id")
    private Long divisionId;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;

    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;

    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;

    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "is_head", nullable = false)
    private Integer isHead;

    @Column(name = "external_id", length = 128)
    private String externalId;
}
