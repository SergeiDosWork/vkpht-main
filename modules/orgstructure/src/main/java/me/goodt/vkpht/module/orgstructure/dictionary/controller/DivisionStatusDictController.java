package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.DivisionStatusAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.DivisionStatusDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.DivisionStatusCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/division-status")
@SurProtected(entityName = "org_division_status")
public class DivisionStatusDictController extends AbstractDictionaryController<Integer, DivisionStatusDto> {

    @Getter
    @Autowired
    private DivisionStatusCrudService service;
    @Getter
    @Autowired
    private DivisionStatusAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

