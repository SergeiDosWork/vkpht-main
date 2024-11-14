package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.FamilyStatusAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.FamilyStatusDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.FamilyStatusCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/family-status")
@SurProtected(entityName = "org_family_status")
public class FamilyStatusDictController extends AbstractDictionaryController<Integer, FamilyStatusDto> {

    @Getter
    @Autowired
    private FamilyStatusCrudService service;
    @Getter
    @Autowired
    private FamilyStatusAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

