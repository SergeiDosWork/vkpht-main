package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentCategoryDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.AssignmentCategoryCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.vkpht.common.dictionary.core.controller.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/assignment-category")
@SurProtected(entityName = "org_assignment_category", operation = SurOperation.UNIT)
public class AssignmentCategoryDictController extends UnfilteredDictController<Long, AssignmentCategoryDto> {

    private final AssignmentCategoryCrudService service;

    public AssignmentCategoryDictController(DictionaryMetaGenerator metaGenerator,
                                            AssignmentCategoryCrudService service) {
        super(metaGenerator);
        this.service = service;
    }


    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<AssignmentCategoryDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    @GetMapping("/{id}")
    public AssignmentCategoryDto get(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @Override
    @PostMapping
    public AssignmentCategoryDto create(@RequestBody AssignmentCategoryDto request) {
        return service.create(request);
    }

    @Override
    @PutMapping("/{id}")
    public AssignmentCategoryDto update(@PathVariable("id") Long id,
                                        @RequestBody AssignmentCategoryDto request) {
        return service.update(id, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

