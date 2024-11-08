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

import com.goodt.drive.auth.sur.service.SurOperation;
import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.WorkWeekTypeDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.WorkWeekTypeCrudService;
import me.goodt.vkpht.common.application.util.CoreUtils;
import me.goodt.vkpht.common.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/work-week-type")
@SurProtected(entityName = "org_work_week_type", operation = SurOperation.UNIT)
public class WorkWeekTypeDictController extends UnfilteredDictController<Integer, WorkWeekTypeDto> {

    @Getter
    private final WorkWeekTypeCrudService service;

    @Autowired
    public WorkWeekTypeDictController(DictionaryMetaGenerator metaGenerator, WorkWeekTypeCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<WorkWeekTypeDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public WorkWeekTypeDto get(Integer integer) {
        return service.getById(integer);
    }

    @Override
    public WorkWeekTypeDto create(WorkWeekTypeDto request) {
        return service.create(request);
    }

    @Override
    public WorkWeekTypeDto update(Integer integer, WorkWeekTypeDto request) {
        return service.update(integer, request);
    }

    @Override
    public ResponseEntity<?> delete(Integer integer) {
        service.delete(integer);
        return ResponseEntity.noContent().build();
    }
}

