package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.OrgReasonTypeAsm;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.OrgReasonTypeCrudService;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ReasonTypeDto;
import lombok.Getter;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/dict/org-reason-type")
@SurProtected(entityName = "org_reason_type")
public class OrgReasonTypeDictController extends AbstractDictionaryController<Long, ReasonTypeDto> {

    @Getter
    @Autowired
    private OrgReasonTypeCrudService service;
    @Getter
    @Autowired
    private OrgReasonTypeAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

