package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionStatusDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.PositionStatusCrudService;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.vkpht.common.dictionary.core.controller.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/position-status")
@SurProtected(entityName = "org_position_status", operation = SurOperation.UNIT)
public class PositionStatusDictController extends UnfilteredDictController<Integer, PositionStatusDto> {

    private final PositionStatusCrudService service;

    public PositionStatusDictController(DictionaryMetaGenerator metaGenerator, PositionStatusCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<PositionStatusDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        Sort sort = Sort.unsorted();
        if (sortBy != null) {
            sort = Sort.by(sortDirection, sortBy);
        }
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return service.findAll(pageRequest);
    }

    @Override
    @GetMapping("/{id}")
    public PositionStatusDto get(@PathVariable("id") Integer integer) {
        return service.getById(integer);
    }

    @Override
    @PostMapping
    public PositionStatusDto create(@RequestBody PositionStatusDto request) {
        return service.create(request);
    }

    @Override
    @PutMapping("/{id}")
    public PositionStatusDto update(@PathVariable("id") Integer integer,
                                    @RequestBody PositionStatusDto request) {
        return service.update(integer, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer integer) {
        service.delete(integer);
        return ResponseEntity.noContent().build();
    }
}

