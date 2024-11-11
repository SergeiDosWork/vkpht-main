package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PositionImportanceDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.PositionImportanceCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.vkpht.common.dictionary.core.controller.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/position-importance")
@SurProtected(entityName = "org_position_importance", operation = SurOperation.UNIT)
public class PositionImportanceDictController extends UnfilteredDictController<Integer, PositionImportanceDto> {

    @Getter
    private final PositionImportanceCrudService service;

    @Autowired
    public PositionImportanceDictController(DictionaryMetaGenerator metaGenerator, PositionImportanceCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<PositionImportanceDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public PositionImportanceDto get(Integer id) {
        return service.getById(id);
    }

    @Override
    public PositionImportanceDto create(PositionImportanceDto request) {
        return service.create(request);
    }

    @Override
    public PositionImportanceDto update(Integer id, PositionImportanceDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

