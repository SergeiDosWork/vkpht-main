package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.CitizenshipAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.CitizenshipDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.CitizenshipCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/citizenship")
@SurProtected(entityName = "org_citizenship")
public class CitizenshipDictController extends AbstractDictionaryController<Long, CitizenshipDto> {

    @Getter
    @Autowired
    private CitizenshipCrudService service;
    @Getter
    @Autowired
    private CitizenshipAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

