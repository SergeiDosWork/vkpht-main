package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Setter
@Getter
@Entity
@Immutable
@Table(name = "org_division_team_role")
public class DivisionTeamRoleShort extends DomainObject {

    @Column(name = "division_team_id", insertable = false, updatable = false)
    private Long divisionTeamId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "position_importance_id")
    private Long positionImportanceId;

    @Column(name = "external_id", length = 128)
    private String externalId;
}
