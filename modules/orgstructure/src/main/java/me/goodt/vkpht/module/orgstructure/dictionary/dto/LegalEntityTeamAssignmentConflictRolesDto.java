package me.goodt.vkpht.module.orgstructure.dictionary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class LegalEntityTeamAssignmentConflictRolesDto {

    private Long legalEntityTeamAssignmentRoleIdAssigned;
    @NotNull
    private Long legalEntityTeamAssignmentRoleIdConflicted;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private Long divisionTeamAssignmentRoleIdAssigned;
}
