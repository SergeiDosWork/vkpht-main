package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.service.PositionCategoryCrudService;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionCategoryDto;
import com.goodt.drive.rtcore.utils.CoreUtils;

import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;

import me.goodt.micro.core.util.DictionaryMetaGenerator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/dict/position-category")
@SurProtected(entityName = "org_position_category", operation = SurOperation.UNIT)
public class PositionCategoryDictController extends UnfilteredDictController<Long, PositionCategoryDto> {

    private final PositionCategoryCrudService service;

    public PositionCategoryDictController(DictionaryMetaGenerator metaGenerator, PositionCategoryCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<PositionCategoryDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        Pageable pageable = CoreUtils.asPageable(page, size, sortBy, sortDirection);
        return service.findAll(pageable);
    }

    @Override
    public PositionCategoryDto create(PositionCategoryDto request) {
        return service.create(request);
    }

    @Override
    public PositionCategoryDto get(Long id) {
        return service.get(id);
    }

    @Override
    public PositionCategoryDto update(Long id, PositionCategoryDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

