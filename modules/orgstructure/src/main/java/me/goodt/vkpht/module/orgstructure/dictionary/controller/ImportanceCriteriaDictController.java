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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.ImportanceCriteriaDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.ImportanceCriteriaCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/importance-criteria")
@SurProtected(entityName = "org_importance_criteria", operation = SurOperation.UNIT)
public class ImportanceCriteriaDictController extends UnfilteredDictController<Long, ImportanceCriteriaDto> {
    private final ImportanceCriteriaCrudService service;

    @Autowired
    public ImportanceCriteriaDictController(DictionaryMetaGenerator metaGenerator, ImportanceCriteriaCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return List.of(
                WebMvcLinkBuilder.linkTo(ImportanceCriteriaGroupDictController.class)
                        .withRel("groupId").withName("id").withTitle("name"),
                WebMvcLinkBuilder.linkTo(CalculationMethodDictController.class)
                        .withRel("calculationMethodId").withName("id").withTitle("name")
        );
    }

    @Override
    public Page<ImportanceCriteriaDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public ImportanceCriteriaDto get(Long id) {
        return service.getById(id);
    }

    @Override
    public ImportanceCriteriaDto create(ImportanceCriteriaDto request) {
        return service.create(request);
    }

    @Override
    public ImportanceCriteriaDto update(Long id, ImportanceCriteriaDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
