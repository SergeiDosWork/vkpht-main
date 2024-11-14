package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.goodt.vkpht.module.orgstructure.domain.dao.CitizenshipIntegrationDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.CitizenshipIntegrationEntity;
import me.goodt.vkpht.common.dictionary.core.service.AbstractDictionaryService;

@Service
public class CitizenshipIntegrationCrudService extends AbstractDictionaryService<CitizenshipIntegrationEntity, Long> {

    @Getter
    @Autowired
    private CitizenshipIntegrationDao dao;
}
