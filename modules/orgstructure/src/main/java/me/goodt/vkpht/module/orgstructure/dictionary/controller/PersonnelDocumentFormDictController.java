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

import java.util.Collection;
import java.util.Collections;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonnelDocumentFormDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.PersonnelDocumentFormCrudService;
import com.goodt.drive.rtcore.utils.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/personnel-document-form")
@SurProtected(entityName = "org_personnel_document_form", operation = SurOperation.UNIT)
public class PersonnelDocumentFormDictController extends UnfilteredDictController<Integer, PersonnelDocumentFormDto> {

    private final PersonnelDocumentFormCrudService service;

    public PersonnelDocumentFormDictController(DictionaryMetaGenerator metaGenerator, PersonnelDocumentFormCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<PersonnelDocumentFormDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    @GetMapping("/{id}")
    public PersonnelDocumentFormDto get(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @Override
    @PostMapping
    public PersonnelDocumentFormDto create(@RequestBody PersonnelDocumentFormDto request) {
        return service.create(request);
    }

    @Override
    @PutMapping("/{id}")
    public PersonnelDocumentFormDto update(@PathVariable("id") Integer id,
                                           @RequestBody PersonnelDocumentFormDto request) {
        return service.update(id, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

