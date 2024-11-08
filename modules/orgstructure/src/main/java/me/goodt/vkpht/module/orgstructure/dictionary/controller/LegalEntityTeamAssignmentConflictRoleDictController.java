package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.LegalEntityTeamAssignmentConflictRoleCrudService;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.LegalEntityTeamAssignmentConflictRolesAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.LegalEntityTeamAssignmentConflictRolesDto;
import lombok.Getter;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

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
