package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.SystemRoleAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.SystemRoleDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.SystemRoleCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/system-role")
@SurProtected(entityName = "org_system_role")
public class SystemRoleDictController extends AbstractDictionaryController<Integer, SystemRoleDto> {

    @Getter
    @Autowired
    private SystemRoleCrudService service;
    @Getter
    @Autowired
    private SystemRoleAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

