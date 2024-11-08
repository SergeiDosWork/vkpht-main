package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.PositionGradeCrudService;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionGradeDto;
import me.goodt.vkpht.common.application.util.CoreUtils;

import me.goodt.vkpht.common.controller.UnfilteredDictController;

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
@RequestMapping("/api/dict/position-grade")
@SurProtected(entityName = "org_position_grade", operation = SurOperation.UNIT)
public class PositionGradeDictController extends UnfilteredDictController<Long, PositionGradeDto> {

    private final PositionGradeCrudService service;

    public PositionGradeDictController(DictionaryMetaGenerator metaGenerator, PositionGradeCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<PositionGradeDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        Pageable pageable = CoreUtils.asPageable(page, size, sortBy, sortDirection);
        return service.findAll(pageable);
    }

    @Override
    public PositionGradeDto create(PositionGradeDto request) {
        return service.create(request);
    }

    @Override
    public PositionGradeDto get(Long id) {
        return service.get(id);
    }

    @Override
    public PositionGradeDto update(Long id, PositionGradeDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

