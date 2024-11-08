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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentStatusDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.AssignmentStatusCrudService;
import com.goodt.drive.rtcore.utils.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
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

