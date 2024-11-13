package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

/**
 * @author Pavel Khovaylo
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "org_division_team_role")
public class DivisionTeamRoleEntity extends DomainObject {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "division_team_id", nullable = false)
    private DivisionTeamEntity divisionTeam;

    @Column(name = "division_team_id", insertable = false, updatable = false)
    private Long divisionTeamId;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @Column(name = "role_id", insertable = false, updatable = false)
    private Long roleId;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "position_importance_id")
    private PositionImportanceEntity positionImportance;

    @Column(name = "external_id", length = 128)
    private String externalId;

    @Column(name = "update_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "author_employee_id", nullable = false)
    private Long authorEmployeeId;

    @Column(name = "update_employee_id", nullable = false)
    private Long updateEmployeeId;
}
