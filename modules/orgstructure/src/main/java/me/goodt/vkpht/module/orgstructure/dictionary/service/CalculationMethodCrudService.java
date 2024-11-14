package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.goodt.vkpht.module.orgstructure.domain.dao.CalculationMethodDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.CalculationMethodEntity;
import me.goodt.vkpht.common.dictionary.core.service.AbstractDictionaryService;

@Service
public class CalculationMethodCrudService extends AbstractDictionaryService<CalculationMethodEntity, Long> {

    @Getter
    @Autowired
    private CalculationMethodDao dao;
}
