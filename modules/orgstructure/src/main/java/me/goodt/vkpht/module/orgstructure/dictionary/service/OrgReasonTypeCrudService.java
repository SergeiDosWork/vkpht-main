package me.goodt.vkpht.module.orgstructure.dictionary.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.goodt.vkpht.module.orgstructure.domain.dao.OrgReasonTypeDao;
import me.goodt.vkpht.module.orgstructure.domain.entity.OrgReasonTypeEntity;
import me.goodt.micro.core.service.AbstractDictionaryService;

@Service
public class OrgReasonTypeCrudService extends AbstractDictionaryService<OrgReasonTypeEntity, Long> {

	@Getter
	@Autowired
	private OrgReasonTypeDao dao;

}
