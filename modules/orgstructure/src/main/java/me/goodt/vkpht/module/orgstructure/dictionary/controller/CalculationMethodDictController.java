package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.CalculationMethodAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.CalculationMethodDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.CalculationMethodCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/calculation-method")
@SurProtected(entityName = "org_calculation_method")
public class CalculationMethodDictController extends AbstractDictionaryController<Long, CalculationMethodDto> {

    @Getter
    @Autowired
    private CalculationMethodCrudService service;
    @Getter
    @Autowired
    private CalculationMethodAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

