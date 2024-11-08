package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.CalculationMethodAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.CalculationMethodDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.CalculationMethodCrudService;
import lombok.Getter;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

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

