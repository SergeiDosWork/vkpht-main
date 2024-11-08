package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.ProjectTeamTypeAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTeamTypeDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.ProjectTeamTypeCrudService;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;

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

