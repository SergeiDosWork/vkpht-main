package me.goodt.vkpht.module.orgstructure.dictionary.controller;

import me.goodt.vkpht.common.application.util.CoreUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

import com.goodt.drive.auth.sur.service.SurProtected;
import me.goodt.vkpht.module.orgstructure.dictionary.dto.JobTitleDto;
import com.goodt.drive.rtcore.dictionary.orgstructure.service.JobTitleCrudService;
import me.goodt.vkpht.common.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/job-title")
@SurProtected(entityName = "org_job_title")
public class JobTitleDictController extends UnfilteredDictController<Long, JobTitleDto> {
    private final JobTitleCrudService service;

    @Autowired
    public JobTitleDictController(DictionaryMetaGenerator metaGenerator, JobTitleCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return List.of(
                WebMvcLinkBuilder.linkTo(JobTitleClusterDictController.class)
                        .withRel("clusterId").withName("id").withTitle("name"),
                WebMvcLinkBuilder.linkTo(JobTitleDictController.class)
                        .withRel("precursorId").withName("id").withTitle("code")
        );
    }

    @Override
    public Page<JobTitleDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public JobTitleDto get(Long id) {
        return service.getById(id);
    }

    @Override
    public JobTitleDto create(JobTitleDto request) {
        return service.create(request);
    }

    @Override
    public JobTitleDto update(Long id, JobTitleDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
