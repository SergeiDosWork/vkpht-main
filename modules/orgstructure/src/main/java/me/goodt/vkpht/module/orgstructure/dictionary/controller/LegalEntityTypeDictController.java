package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.LegalEntityTypeAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.LegalEntityTypeDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.LegalEntityTypeCrudService;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/legal-entity-type")
@SurProtected(entityName = "org_legal_entity_type")
public class LegalEntityTypeDictController extends AbstractDictionaryController<Integer, LegalEntityTypeDto> {

    @Getter
    @Autowired
    private LegalEntityTypeCrudService service;
    @Getter
    @Autowired
    private LegalEntityTypeAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

