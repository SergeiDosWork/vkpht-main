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

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.SubstitutionTypeDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.SubstitutionTypeCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/substitution-type")
@SurProtected(entityName = "org_substitution_type")
public class SubstitutionTypeDictController extends UnfilteredDictController<Integer, SubstitutionTypeDto> {

    @Getter
    @Autowired
    private SubstitutionTypeCrudService service;

    @Autowired
    public SubstitutionTypeDictController(DictionaryMetaGenerator metaGenerator, SubstitutionTypeCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<SubstitutionTypeDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public SubstitutionTypeDto get(Integer id) {
        return service.getById(id);
    }

    @Override
    public SubstitutionTypeDto create(SubstitutionTypeDto request) {
        return service.create(request);
    }

    @Override
    public SubstitutionTypeDto update(Integer id, SubstitutionTypeDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

