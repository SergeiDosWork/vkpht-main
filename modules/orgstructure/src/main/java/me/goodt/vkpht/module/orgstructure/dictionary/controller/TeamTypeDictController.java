package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.TeamTypeCrudService;
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.TeamTypeAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.TeamTypeDto;
import lombok.Getter;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

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

