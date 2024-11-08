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
import com.goodt.drive.rtcore.dictionary.orgstructure.asm.LocationGroupAsm;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.LocationGroupDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.LocationGroupCrudService;
import me.goodt.vkpht.common.controller.AbstractDictionaryController;

@RestController
@RequestMapping("/api/dict/location-group")
@SurProtected(entityName = "org_location_group")
public class LocationGroupDictController extends AbstractDictionaryController<Long, LocationGroupDto> {

    @Getter
    @Autowired
    private LocationGroupCrudService service;
    @Getter
    @Autowired
    private LocationGroupAsm asm;

    @Override
    protected Collection<Link> getRelatedLinks() {
        return List.of(
                WebMvcLinkBuilder.linkTo(LocationGroupDictController.class)
                        .withRel("parentId").withName("id").withTitle("name")
        );
    }
}

