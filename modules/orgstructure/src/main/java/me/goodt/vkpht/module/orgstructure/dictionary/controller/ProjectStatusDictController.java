package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.ProjectStatusAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectStatusDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.ProjectStatusCrudService;
import lombok.Getter;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/dict/project-status")
@SurProtected(entityName = "org_project_status")
public class ProjectStatusDictController extends AbstractDictionaryController<Integer, ProjectStatusDto> {

    @Getter
    @Autowired
    private ProjectStatusCrudService service;
    @Getter
    @Autowired
    private ProjectStatusAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

