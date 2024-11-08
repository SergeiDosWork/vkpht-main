package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.ProjectTeamRoleStatusAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamRoleStatusDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.ProjectTeamRoleStatusCrudService;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/project-team-role-status")
@SurProtected(entityName = "org_project_team_role_status")
public class ProjectTeamRoleStatusDictController extends AbstractDictionaryController<Long, ProjectTeamRoleStatusDto> {

    @Getter
    @Autowired
    private ProjectTeamRoleStatusCrudService service;
    @Getter
    @Autowired
    private ProjectTeamRoleStatusAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

