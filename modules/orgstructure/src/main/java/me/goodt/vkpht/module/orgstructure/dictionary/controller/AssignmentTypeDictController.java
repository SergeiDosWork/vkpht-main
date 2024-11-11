package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentTypeDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.AssignmentTypeCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.vkpht.common.dictionary.core.controller.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/assignment-type")
@SurProtected(entityName = "org_assignment_type", operation = SurOperation.UNIT)
public class AssignmentTypeDictController extends UnfilteredDictController<Integer, AssignmentTypeDto> {

    private final AssignmentTypeCrudService service;

    public AssignmentTypeDictController(DictionaryMetaGenerator metaGenerator, AssignmentTypeCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<AssignmentTypeDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    @GetMapping("/{id}")
    public AssignmentTypeDto get(@PathVariable("id") Integer integer) {
        return service.getById(integer);
    }

    @Override
    @PostMapping
    public AssignmentTypeDto create(@RequestBody AssignmentTypeDto request) {
        return service.create(request);
    }

    @Override
    @PutMapping("/{id}")
    public AssignmentTypeDto update(@PathVariable("id") Integer integer,
                                    @RequestBody AssignmentTypeDto request) {
        return service.update(integer, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer integer) {
        service.delete(integer);
        return ResponseEntity.noContent().build();
    }
}

