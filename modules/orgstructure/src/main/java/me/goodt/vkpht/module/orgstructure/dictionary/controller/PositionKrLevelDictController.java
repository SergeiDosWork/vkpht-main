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

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionKrLevelDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.PositionKrLevelCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.vkpht.common.dictionary.core.controller.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/position-kr-level")
@SurProtected(entityName = "org_position_kr_level", operation = SurOperation.UNIT)
public class PositionKrLevelDictController extends UnfilteredDictController<Long, PositionKrLevelDto> {

    private final PositionKrLevelCrudService service;

    public PositionKrLevelDictController(DictionaryMetaGenerator metaGenerator, PositionKrLevelCrudService service) {
        super(metaGenerator);
        this.service = service;
    }


    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<PositionKrLevelDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    @GetMapping("/{id}")
    public PositionKrLevelDto get(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @Override
    @PostMapping
    public PositionKrLevelDto create(@RequestBody @Valid PositionKrLevelDto request) {
        return service.create(request);
    }

    @Override
    @PutMapping("/{id}")
    public PositionKrLevelDto update(@PathVariable("id") Long id, PositionKrLevelDto request) {
        return service.update(id, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

