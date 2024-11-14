package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.asm.TeamStatusAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.TeamStatusDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.TeamStatusCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/team-status")
@SurProtected(entityName = "org_team_status")
public class TeamStatusDictController extends AbstractDictionaryController<Integer, TeamStatusDto> {

    @Getter
    @Autowired
    private TeamStatusCrudService service;
    @Getter
    @Autowired
    private TeamStatusAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }
}

