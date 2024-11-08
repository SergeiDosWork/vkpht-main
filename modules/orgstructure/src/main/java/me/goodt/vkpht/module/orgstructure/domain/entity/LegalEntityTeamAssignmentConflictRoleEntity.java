package me.goodt.vkpht.module.orgstructure.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import me.goodt.vkpht.common.domain.entity.DomainObject;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "org_legal_entity_team_assignment_conflict_roles")
public class LegalEntityTeamAssignmentConflictRoleEntity extends DomainObject {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_entity_team_assignment_role_id_assigned")
    private RoleEntity legalEntityTeamAssignmentRoleIdAssigned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_entity_team_assignment_role_id_conflicted")
    private RoleEntity legalEntityTeamAssignmentRoleIdConflicted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_team_assignment_role_id_assigned")
    private RoleEntity divisionTeamAssignmentRoleIdAssigned;

}
