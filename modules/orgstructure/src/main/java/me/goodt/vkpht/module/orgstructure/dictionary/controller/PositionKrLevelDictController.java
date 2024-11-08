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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionKrLevelDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.PositionKrLevelCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

import jakarta.validation.Valid;

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

