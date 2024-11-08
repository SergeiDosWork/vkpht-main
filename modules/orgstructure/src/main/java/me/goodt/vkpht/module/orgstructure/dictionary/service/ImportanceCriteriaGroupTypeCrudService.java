package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.goodt.vkpht.module.orgstructure.domain.dao.ImportanceCriteriaGroupTypeDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.ImportanceCriteriaGroupTypeEntity;
import me.goodt.micro.core.service.AbstractDictionaryService;

@Service
public class ImportanceCriteriaGroupTypeCrudService extends
        AbstractDictionaryService<ImportanceCriteriaGroupTypeEntity, Long> {

    @Getter
    @Autowired
    private ImportanceCriteriaGroupTypeDao dao;

}
