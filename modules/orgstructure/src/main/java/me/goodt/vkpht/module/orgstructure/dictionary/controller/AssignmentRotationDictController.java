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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.AssignmentRotationDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.AssignmentRotationCrudService;
import com.goodt.drive.rtcore.utils.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/assignment-rotation")
@SurProtected(entityName = "org_assignment_rotation", operation = SurOperation.UNIT)
public class AssignmentRotationDictController extends UnfilteredDictController<Integer, AssignmentRotationDto> {

    @Getter
    private final AssignmentRotationCrudService service;

    @Autowired
    public AssignmentRotationDictController(DictionaryMetaGenerator metaGenerator, AssignmentRotationCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<AssignmentRotationDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public AssignmentRotationDto get(Integer id) {
        return service.getById(id);
    }

    @Override
    public AssignmentRotationDto create(AssignmentRotationDto request) {
        return service.create(request);
    }

    @Override
    public AssignmentRotationDto update(Integer id, AssignmentRotationDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

