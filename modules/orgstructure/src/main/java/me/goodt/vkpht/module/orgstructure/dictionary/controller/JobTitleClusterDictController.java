package me.goodt.vkpht.module.orgstructure.dictionary.controller;

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
import me.goodt.vkpht.module.orgstructure.dictionary.dto.JobTitleClusterDto;
import me.goodt.vkpht.module.orgstructure.dictionary.service.JobTitleClusterCrudService;
import com.goodt.drive.rtcore.utils.CoreUtils;
import me.goodt.vkpht.common.dictionary.core.controller.UnfilteredDictController;
import me.goodt.micro.core.util.DictionaryMetaGenerator;

@RestController
@RequestMapping("/api/dict/job-title-cluster")
@SurProtected(entityName = "org_job_title_cluster")
public class JobTitleClusterDictController extends UnfilteredDictController<Long, JobTitleClusterDto> {
    private final JobTitleClusterCrudService service;

    @Autowired
    public JobTitleClusterDictController(DictionaryMetaGenerator metaGenerator, JobTitleClusterCrudService service) {
        super(metaGenerator);
        this.service = service;
    }

    @Override
    protected Collection<Link> getRelatedLinks() {
        return Collections.emptyList();
    }

    @Override
    public Page<JobTitleClusterDto> findAll(int page, int size, String sortBy, Sort.Direction sortDirection) {
        return service.findAll(CoreUtils.asPageable(page, size, sortBy, sortDirection));
    }

    @Override
    public JobTitleClusterDto get(Long id) {
        return service.getById(id);
    }

    @Override
    public JobTitleClusterDto create(JobTitleClusterDto request) {
        return service.create(request);
    }

    @Override
    public JobTitleClusterDto update(Long id, JobTitleClusterDto request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

