package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.FamilyStatusAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.FamilyStatusDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.FamilyStatusCrudService;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;

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

