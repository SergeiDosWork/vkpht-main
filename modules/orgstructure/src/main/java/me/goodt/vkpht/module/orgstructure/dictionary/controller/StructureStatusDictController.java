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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.StructureStatusDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.StructureStatusCrudService;
import com.goodt.drive.rtcore.utils.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/structure-status")
@SurProtected(entityName = "org_structure_status", operation = SurOperation.UNIT)
public class StructureStatusDictController extends UnfilteredDictController<Integer, StructureStatusDto> {
    private final StructureStatusCrudService service;

    @Autowired
    public StructureStatusDictController(DictionaryMetaGenerator metaGenerator, StructureStatusCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<StructureStatusDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public StructureStatusDto get(Integer id) {
        return service.getById(id);
    }

    @Override
    public StructureStatusDto create(StructureStatusDto request) {
        return service.create(request);
    }

    @Override
    public StructureStatusDto update(Integer id, StructureStatusDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

