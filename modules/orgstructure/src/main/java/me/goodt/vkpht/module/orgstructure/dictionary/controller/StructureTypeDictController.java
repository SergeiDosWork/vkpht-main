package me.goodt.vkpht.module.orgstructure.dictionary.controller;

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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.StructureTypeDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.StructureTypeCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.vkpht.common.dictionary.core.controller.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/structure-type")
@SurProtected(entityName = "org_structure_type", operation = SurOperation.UNIT)
public class StructureTypeDictController extends UnfilteredDictController<Integer, StructureTypeDto> {
    private final StructureTypeCrudService service;

    @Autowired
    public StructureTypeDictController(DictionaryMetaGenerator metaGenerator, StructureTypeCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<StructureTypeDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public StructureTypeDto get(Integer id) {
        return service.getById(id);
    }

    @Override
    public StructureTypeDto create(StructureTypeDto request) {
        return service.create(request);
    }

    @Override
    public StructureTypeDto update(Integer id, StructureTypeDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

