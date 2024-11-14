package me.goodt.vkpht.module.notification.api.dto.learning;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_FROM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_TO_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DIVISION_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.EXTERNAL_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.IS_ANCHOR_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.IS_BLACKLISTED_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.IS_HEAD_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.PROVIDER_URL_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.SHORT_NAME_TAG;

@Getter
@Setter
public class StudyProviderEntity {
    private Long id;
    @JsonSetter(DATE_FROM_TAG)
    private Date dateFrom;
    @JsonSetter(DATE_TO_TAG)
    private Date dateTo;
    @JsonSetter(EXTERNAL_ID_TAG)
    private String externalId;
    private String name;
    @JsonSetter(SHORT_NAME_TAG)
    private String shortName;
    private String inn;
    @JsonSetter(IS_ANCHOR_TAG)
    private Boolean isAnchor;
    @JsonSetter(IS_HEAD_TAG)
    private Boolean isHead;
    private String description;
    private Long code;
    @JsonSetter(PROVIDER_URL_TAG)
    private String providerUrl;
    @JsonSetter(DIVISION_ID_TAG)
    private Long divisionId;
    @JsonSetter(IS_BLACKLISTED_TAG)
    private Boolean isBlacklisted;
}
