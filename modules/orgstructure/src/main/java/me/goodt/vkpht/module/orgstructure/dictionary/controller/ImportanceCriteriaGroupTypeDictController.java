package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.ImportanceCriteriaGroupTypeAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ImportanceCriteriaGroupTypeDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.ImportanceCriteriaGroupTypeCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/importance-criteria-group-type")
@SurProtected(entityName = "org_importance_criteria_group_type")
public class ImportanceCriteriaGroupTypeDictController extends
    AbstractDictionaryController<Long, ImportanceCriteriaGroupTypeDto> {

    @Getter
    @Autowired
    private ImportanceCriteriaGroupTypeCrudService service;
    @Getter
    @Autowired
    private ImportanceCriteriaGroupTypeAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

