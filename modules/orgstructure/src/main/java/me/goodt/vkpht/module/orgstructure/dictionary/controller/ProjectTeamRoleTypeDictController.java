package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.ProjectTeamRoleTypeAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamRoleTypeDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.ProjectTeamRoleTypeCrudService;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/project-team-role-type")
@SurProtected(entityName = "org_project_team_role_type")
public class ProjectTeamRoleTypeDictController extends AbstractDictionaryController<Long, ProjectTeamRoleTypeDto> {

    @Getter
    @Autowired
    private ProjectTeamRoleTypeCrudService service;
    @Getter
    @Autowired
    private ProjectTeamRoleTypeAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

