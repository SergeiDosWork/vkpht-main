package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.ProjectTeamAssignmentStatusAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamAssignmentStatusDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.ProjectTeamAssignmentStatusCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/project-team-assignment-status")
@SurProtected(entityName = "org_project_team_assignment_status")
public class ProjectTeamAssignmentStatusDictController extends AbstractDictionaryController<Long, ProjectTeamAssignmentStatusDto> {

    @Getter
    @Autowired
    private ProjectTeamAssignmentStatusCrudService service;
    @Getter
    @Autowired
    private ProjectTeamAssignmentStatusAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

