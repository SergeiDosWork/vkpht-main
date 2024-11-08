package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.CostCenterAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.CostCenterDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.CostCenterCrudService;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/cost-center")
@SurProtected(entityName = "org_cost_center")
public class CostCenterDictController extends AbstractDictionaryController<Long, CostCenterDto> {

    @Getter
    @Autowired
    private CostCenterCrudService service;
    @Getter
    @Autowired
    private CostCenterAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

