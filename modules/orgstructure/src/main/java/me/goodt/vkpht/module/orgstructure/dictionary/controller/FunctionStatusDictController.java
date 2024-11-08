package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.FunctionStatusCrudService;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.FunctionStatusDto;
import me.goodt.vkpht.common.application.util.CoreUtils;

import lombok.Getter;

import me.goodt.vkpht.common.controller.UnfilteredDictController;

import me.goodt.micro.core.util.DictionaryMetaGenerator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/dict/function-status")
@SurProtected(entityName = "org_function_status", operation = SurOperation.UNIT)
public class FunctionStatusDictController extends UnfilteredDictController<Integer, FunctionStatusDto> {

    @Getter
    private final FunctionStatusCrudService service;

    public FunctionStatusDictController(DictionaryMetaGenerator metaGenerator, FunctionStatusCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<FunctionStatusDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public FunctionStatusDto get(Integer id) {
        return service.get(id);
    }

    @Override
    public FunctionStatusDto create(FunctionStatusDto request) {
        return service.create(request);
    }

    @Override
    public FunctionStatusDto update(Integer id, FunctionStatusDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

