package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.LegalEntityStatusAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.LegalEntityStatusDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.LegalEntityStatusCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/legal-entity-status")
@SurProtected(entityName = "org_legal_entity_status")
public class LegalEntityStatusDictController extends AbstractDictionaryController<Integer, LegalEntityStatusDto> {

    @Getter
    @Autowired
    private LegalEntityStatusCrudService service;
    @Getter
    @Autowired
    private LegalEntityStatusAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

