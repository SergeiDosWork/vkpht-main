package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.ProjectTeamStatusAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamStatusDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.ProjectTeamStatusCrudService;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/project-team-status")
@SurProtected(entityName = "org_project_team_status")
public class ProjectTeamStatusDictController extends AbstractDictionaryController<Long, ProjectTeamStatusDto> {

    @Getter
    @Autowired
    private ProjectTeamStatusCrudService service;
    @Getter
    @Autowired
    private ProjectTeamStatusAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

