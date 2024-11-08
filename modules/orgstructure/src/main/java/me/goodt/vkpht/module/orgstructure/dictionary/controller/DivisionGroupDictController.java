package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.DivisionGroupAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.DivisionGroupDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.DivisionGroupCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/division-group")
@SurProtected(entityName = "org_division_group")
public class DivisionGroupDictController extends AbstractDictionaryController<Long, DivisionGroupDto> {

    @Getter
    @Autowired
    private DivisionGroupCrudService service;
    @Getter
    @Autowired
    private DivisionGroupAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

