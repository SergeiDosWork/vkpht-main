package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.ProjectStatusAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ProjectStatusDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.ProjectStatusCrudService;
import lombok.Getter;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;
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

