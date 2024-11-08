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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonnelDocumentDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.PersonnelDocumentCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/personnel-document")
@SurProtected(entityName = "org_personnel_document", operation = SurOperation.UNIT)
public class PersonnelDocumentDictController extends UnfilteredDictController<Long, PersonnelDocumentDto> {
    private final PersonnelDocumentCrudService service;

    public PersonnelDocumentDictController(DictionaryMetaGenerator metaGenerator, PersonnelDocumentCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    public Page<PersonnelDocumentDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    @GetMapping("/{id}")
    public PersonnelDocumentDto get(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @Override
    @PostMapping
    public PersonnelDocumentDto create(@RequestBody PersonnelDocumentDto request) {
        return service.create(request);
    }

    @Override
    @PutMapping("/{id}")
    public PersonnelDocumentDto update(@PathVariable("id") Long id,
                                       @RequestBody PersonnelDocumentDto request) {
        return service.update(id, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
