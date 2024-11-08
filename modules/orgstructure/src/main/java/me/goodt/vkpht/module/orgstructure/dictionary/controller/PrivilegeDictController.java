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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PrivilegeDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.PrivilegeCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

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
