package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionTypeDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.PositionTypeCrudService;
import com.goodt.drive.rtcore.utils.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/position-type")
@SurProtected(entityName = "org_position_type", operation = SurOperation.UNIT)
public class PositionTypeDictController extends UnfilteredDictController<Long, PositionTypeDto> {

    private final PositionTypeCrudService service;

    public PositionTypeDictController(DictionaryMetaGenerator metaGenerator, PositionTypeCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<PositionTypeDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        Pageable pageable = CoreUtils.asPageable(page, size, sortBy, sortDirection);
        return service.findAll(pageable);
    }

    @Override
    public PositionTypeDto get(Long id) {
        return service.getById(id);
    }

    @Override
    public PositionTypeDto create(PositionTypeDto request) {
        return service.create(request);
    }

    @Override
    public PositionTypeDto update(Long id, PositionTypeDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

