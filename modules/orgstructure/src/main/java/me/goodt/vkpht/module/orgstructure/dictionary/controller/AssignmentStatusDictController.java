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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentStatusDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.AssignmentStatusCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/assignment-status")
@SurProtected(entityName = "org_assignment_status", operation = SurOperation.UNIT)
public class AssignmentStatusDictController extends UnfilteredDictController<Integer, AssignmentStatusDto> {

    private final AssignmentStatusCrudService service;

    public AssignmentStatusDictController(DictionaryMetaGenerator metaGenerator, AssignmentStatusCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<AssignmentStatusDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    @GetMapping("/{id}")
    public AssignmentStatusDto get(@PathVariable("id") Integer integer) {
        return service.getById(integer);
    }

    @Override
    @PostMapping
    public AssignmentStatusDto create(@RequestBody AssignmentStatusDto request) {
        return service.create(request);
    }

    @Override
    @PutMapping("/{id}")
    public AssignmentStatusDto update(@PathVariable("id") Integer integer,
                                      @RequestBody AssignmentStatusDto request) {
        return service.update(integer, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer integer) {
        service.delete(integer);
        return ResponseEntity.noContent().build();
    }
}

