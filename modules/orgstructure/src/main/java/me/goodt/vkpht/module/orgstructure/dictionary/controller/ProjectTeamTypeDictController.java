package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.ProjectTeamTypeAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamTypeDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.ProjectTeamTypeCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/project-team-type")
@SurProtected(entityName = "org_project_team_type")
public class ProjectTeamTypeDictController extends AbstractDictionaryController<Long, ProjectTeamTypeDto> {

    @Getter
    @Autowired
    private ProjectTeamTypeCrudService service;
    @Getter
    @Autowired
    private ProjectTeamTypeAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

