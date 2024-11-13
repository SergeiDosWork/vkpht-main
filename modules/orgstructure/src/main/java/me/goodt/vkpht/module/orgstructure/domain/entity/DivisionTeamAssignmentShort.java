package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@Immutable
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "org_division_team_assignment")
public class DivisionTeamAssignmentShort extends DomainObject {

    @Column(name = "precursor_id")
    private Long precursorId;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private AssignmentTypeEntity type;

    @Column(name = "division_team_role_id")
    private Long divisionTeamRoleId;

    @Column(name = "date_from", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;

    @Column(name = "date_to", columnDefinition = "TIMESTAMP DEFAULT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    @Column(name = "employee_id", insertable = false, updatable = false)
    private Long employeeId;

    @Column(name = "full_name", nullable = false, length = 512)
    private String fullName;

    @Column(name = "short_name", nullable = false, length = 256)
    private String shortName;

    @Column(name = "abbreviation", nullable = false, length = 128)
    private String abbreviation;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "status_id")
    private AssignmentStatusEntity status;

    @Column(name = "external_id", length = 128)
    private String externalId;
}
