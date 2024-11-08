package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.SystemRoleCrudService;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.SystemRoleAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.SystemRoleDto;
import lombok.Getter;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

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

