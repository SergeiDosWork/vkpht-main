package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.LegalEntityTeamAssignmentConflictRolesAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.LegalEntityTeamAssignmentConflictRolesDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.LegalEntityTeamAssignmentConflictRoleCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/legal-entity-team-assignment-conflict-role")
@SurProtected(entityName = "org_legal_entity_team_assignment_conflict_role")
public class LegalEntityTeamAssignmentConflictRoleDictController extends AbstractDictionaryController<Long, LegalEntityTeamAssignmentConflictRolesDto> {

    @Getter
    @Autowired
    private LegalEntityTeamAssignmentConflictRoleCrudService service;
    @Getter
    @Autowired
    private LegalEntityTeamAssignmentConflictRolesAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return List.of(
            WebMvcLinkBuilder.linkTo(RoleDictController.class)
                .withRel("divisionTeamAssignmentRoleIdAssigned").withName("id").withTitle("code"),
            WebMvcLinkBuilder.linkTo(RoleDictController.class)
                .withRel("legalEntityTeamAssignmentRoleIdConflicted").withName("id").withTitle("code"),
            WebMvcLinkBuilder.linkTo(RoleDictController.class)
                .withRel("legalEntityTeamAssignmentRoleIdAssigned").withName("id").withTitle("code")
        );
    }
}
