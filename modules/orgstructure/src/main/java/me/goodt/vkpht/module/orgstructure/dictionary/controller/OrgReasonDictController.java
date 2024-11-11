package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ReasonDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.OrgReasonCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.vkpht.common.dictionary.core.controller.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/org-reason")
@SurProtected(entityName = "org_reason", operation = SurOperation.UNIT)
public class OrgReasonDictController extends UnfilteredDictController<Long, ReasonDto> {
    private final OrgReasonCrudService service;

    @Autowired
    public OrgReasonDictController(DictionaryMetaGenerator metaGenerator, OrgReasonCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return List.of(
                WebMvcLinkBuilder.linkTo(OrgReasonTypeDictController.class)
                        .withRel("typeId").withName("id").withTitle("name")
        );
    }

    @Override
    public Page<ReasonDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public ReasonDto get(Long id) {
        return service.getById(id);
    }

    @Override
    public ReasonDto create(ReasonDto request) {
        return service.create(request);
    }

    @Override
    public ReasonDto update(Long id, ReasonDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
