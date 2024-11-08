package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.DivisionGroupAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.DivisionGroupDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.DivisionGroupCrudService;
import lombok.Getter;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

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

