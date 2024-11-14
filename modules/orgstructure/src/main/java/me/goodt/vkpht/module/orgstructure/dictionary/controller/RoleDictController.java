package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.RoleAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.RoleDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.RoleCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/role")
@SurProtected(entityName = "org_role")
public class RoleDictController extends AbstractDictionaryController<Long, RoleDto> {

    @Getter
    @Autowired
    private RoleCrudService service;
    @Getter
    @Autowired
    private RoleAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return List.of(
            WebMvcLinkBuilder.linkTo(SystemRoleDictController.class)
                .withRel("systemRoleId").withName("id").withTitle("name")
        );
    }
}

