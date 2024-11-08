package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import java.util.List;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.PersonPrivilegeDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.PersonPrivilegeCrudService;
import com.goodt.drive.rtcore.utils.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/person-privilege")
@SurProtected(entityName = "org_person_privilege", operation = SurOperation.UNIT)
public class PersonPrivilegeDictController extends UnfilteredDictController<Long, PersonPrivilegeDto> {
    private final PersonPrivilegeCrudService service;

    public PersonPrivilegeDictController(
        DictionaryMetaGenerator metaGenerator, PersonPrivilegeCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    public Page<PersonPrivilegeDto> findAll(int page, int size, String sortBy, Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return List.of(
            WebMvcLinkBuilder.linkTo(PrivilegeDictController.class)
                .withRel("privilegeId").withName("id").withTitle("name")
        );
    }

    @Override
    @GetMapping("/{id}")
    public PersonPrivilegeDto get(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @Override
    @PostMapping
    public PersonPrivilegeDto create(@RequestBody PersonPrivilegeDto request) {
        return service.create(request);
    }

    @Override
    @PutMapping("/{id}")
    public PersonPrivilegeDto update(@PathVariable("id") Long id,
                                     @RequestBody PersonPrivilegeDto request) {
        return service.update(id, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
