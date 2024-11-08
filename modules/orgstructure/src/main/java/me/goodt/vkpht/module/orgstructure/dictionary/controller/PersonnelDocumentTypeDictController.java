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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonnelDocumentTypeDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.PersonnelDocumentTypeCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/personnel-document-type")
@SurProtected(entityName = "org_personnel_document_type", operation = SurOperation.UNIT)
public class PersonnelDocumentTypeDictController extends UnfilteredDictController<Integer, PersonnelDocumentTypeDto> {

    private final PersonnelDocumentTypeCrudService service;

    public PersonnelDocumentTypeDictController(DictionaryMetaGenerator metaGenerator, PersonnelDocumentTypeCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }


    @Override
    public Page<PersonnelDocumentTypeDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    @GetMapping("/{id}")
    public PersonnelDocumentTypeDto get(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @Override
    @PostMapping
    public PersonnelDocumentTypeDto create(@RequestBody PersonnelDocumentTypeDto request) {
        return service.create(request);
    }

    @Override
    @PutMapping("/{id}")
    public PersonnelDocumentTypeDto update(@PathVariable("id") Integer id,
                                           @RequestBody PersonnelDocumentTypeDto request) {
        return service.update(id, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

