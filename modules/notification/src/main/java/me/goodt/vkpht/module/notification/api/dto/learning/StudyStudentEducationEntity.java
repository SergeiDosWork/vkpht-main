package me.goodt.vkpht.module.notification.api.dto.learning;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_END_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_FROM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_START_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_TO_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.EXTERNAL_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.STUDY_PROVIDER_TAG;

@Getter
@Setter
public class StudyStudentEducationEntity {
    private Long id;
    @JsonSetter(DATE_FROM_TAG)
    private Date dateFrom;
    @JsonSetter(DATE_TO_TAG)
    private Date dateTo;
    @JsonSetter(EXTERNAL_ID_TAG)
    private String externalId;
    @JsonSetter(STUDY_PROVIDER_TAG)
    private StudyProviderEntity studyProvider;
    @JsonSetter(DATE_START_TAG)
    private Date dateStart;
    @JsonSetter(DATE_END_TAG)
    private Date dateEnd;
}
