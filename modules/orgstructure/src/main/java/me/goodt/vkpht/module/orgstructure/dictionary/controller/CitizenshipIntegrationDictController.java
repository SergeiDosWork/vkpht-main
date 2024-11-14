package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.CitizenshipIntegrationAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.CitizenshipIntegrationDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.CitizenshipIntegrationCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/citizenship-integration")
@SurProtected(entityName = "org_citizenship_integration")
public class CitizenshipIntegrationDictController extends AbstractDictionaryController<Long, CitizenshipIntegrationDto> {

    @Getter
    @Autowired
    private CitizenshipIntegrationCrudService service;
    @Getter
    @Autowired
    private CitizenshipIntegrationAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}
