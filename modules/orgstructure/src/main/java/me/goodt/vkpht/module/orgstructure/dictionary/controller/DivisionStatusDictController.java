package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.DivisionStatusAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.DivisionStatusDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.DivisionStatusCrudService;
import lombok.Getter;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

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

