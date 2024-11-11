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
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.DictionaryMetaGenerator;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PrivilegeDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.PrivilegeCrudService;

@RestController
@RequestMapping("/api/dict/privilege")
@SurProtected(entityName = "org_privilege", operation = SurOperation.UNIT)
public class PrivilegeDictController extends UnfilteredDictController<Long, PrivilegeDto> {

    private final PrivilegeCrudService service;

    public PrivilegeDictController(DictionaryMetaGenerator metaGenerator, PrivilegeCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    public Page<PrivilegeDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    @GetMapping("/{id}")
    public PrivilegeDto get(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @Override
    @PostMapping
    public PrivilegeDto create(@RequestBody PrivilegeDto request) {
        return service.create(request);
    }

    @Override
    @PutMapping("/{id}")
    public PrivilegeDto update(@PathVariable("id") Long id,
                               @RequestBody PrivilegeDto request) {
        return service.update(id, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
