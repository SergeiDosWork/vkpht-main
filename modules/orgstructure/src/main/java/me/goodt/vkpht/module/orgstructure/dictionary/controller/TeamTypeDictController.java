package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.TeamTypeAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.TeamTypeDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.TeamTypeCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/team-type")
@SurProtected(entityName = "org_team_type")
public class TeamTypeDictController extends AbstractDictionaryController<Integer, TeamTypeDto> {

    @Getter
    @Autowired
    private TeamTypeCrudService service;
    @Getter
    @Autowired
    private TeamTypeAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

