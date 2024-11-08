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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ImportanceCriteriaGroupDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.ImportanceCriteriaGroupCrudService;
import com.goodt.drive.rtcore.utils.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/importance-criteria-group")
@SurProtected(entityName = "org_importance_criteria_group", operation = SurOperation.UNIT)
public class ImportanceCriteriaGroupDictController extends UnfilteredDictController<Long, ImportanceCriteriaGroupDto> {
    private final ImportanceCriteriaGroupCrudService service;

    @Autowired
    public ImportanceCriteriaGroupDictController(DictionaryMetaGenerator metaGenerator, ImportanceCriteriaGroupCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return List.of(
                WebMvcLinkBuilder.linkTo(ImportanceCriteriaGroupTypeDictController.class)
                        .withRel("typeId").withName("id").withTitle("name")
        );
    }

    @Override
    public Page<ImportanceCriteriaGroupDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public ImportanceCriteriaGroupDto get(Long id) {
        return service.getById(id);
    }

    @Override
    public ImportanceCriteriaGroupDto create(ImportanceCriteriaGroupDto request) {
        return service.create(request);
    }

    @Override
    public ImportanceCriteriaGroupDto update(Long id, ImportanceCriteriaGroupDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
