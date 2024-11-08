package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.ProjectTeamAssignmentTypeCrudService;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.ProjectTeamAssignmentTypeAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamAssignmentTypeDto;
import lombok.Getter;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/dict/project-team-assignment-type")
@SurProtected(entityName = "org_project_team_assignment_type")
public class ProjectTeamAssignmentTypeDictController extends AbstractDictionaryController<Long, ProjectTeamAssignmentTypeDto> {

    @Getter
    @Autowired
    private ProjectTeamAssignmentTypeCrudService service;
    @Getter
    @Autowired
    private ProjectTeamAssignmentTypeAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

