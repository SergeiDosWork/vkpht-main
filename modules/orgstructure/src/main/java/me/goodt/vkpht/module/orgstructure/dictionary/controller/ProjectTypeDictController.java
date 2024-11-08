package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.ProjectTypeAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectTypeDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.ProjectTypeCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/project-type")
@SurProtected(entityName = "org_project_type")
public class ProjectTypeDictController extends AbstractDictionaryController<Integer, ProjectTypeDto> {

    @Getter
    @Autowired
    private ProjectTypeCrudService service;
    @Getter
    @Autowired
    private ProjectTypeAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

