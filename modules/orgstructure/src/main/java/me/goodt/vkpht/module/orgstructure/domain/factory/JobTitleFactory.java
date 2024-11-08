package me.goodt.vkpht.module.orgstructure.domain.factory;

import lombok.experimental.UtilityClass;

import me.goodt.vkpht.module.orgstructure.api.dto.JobTitleDto;
import me.goodt.vkpht.module.orgstructure.domain.entity.JobTitleEntity;

@UtilityClass
public class JobTitleFactory {

    public static JobTitleDto create(JobTitleEntity jobTitle) {
        return new JobTitleDto(jobTitle.getId(),
                               jobTitle.getPrecursor() == null ? null : jobTitle.getPrecursor().getId(),
                               jobTitle.getDateFrom(), jobTitle.getDateTo(),
                               jobTitle.getFullName(), jobTitle.getShortName(), jobTitle.getAbbreviation(), jobTitle.getCode(),
                               jobTitle.getHash(),
                               jobTitle.getCluster() == null ? null : jobTitle.getCluster().getId(), jobTitle.getIsRequiredCertificate(),
                               jobTitle.getExternalId());
    }

}
