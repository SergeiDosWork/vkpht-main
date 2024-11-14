package me.goodt.vkpht.module.notification.api.dto.quiz;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PollRawDto {
    private Long id;
    @JsonSetter(DtoTagConstants.DATE_FROM_TAG)
    private Date dateFrom;
    @JsonSetter(DtoTagConstants.DATE_TO_TAG)
    private Date dateTo;
    @JsonSetter(DtoTagConstants.DATE_START_TAG)
    private Date dateStart;
    @JsonSetter(DtoTagConstants.DATE_END_TAG)
    private Date dateEnd;
    private String name;
    private String description;
    @JsonSetter(DtoTagConstants.IS_ANONYMOUS_TAG)
    private Integer isAnonymous;
    private Boolean startable;
    @JsonSetter(DtoTagConstants.POLL_TYPE_ID_TAG)
    private Long pollTypeId;
    @JsonSetter(DtoTagConstants.POLL_PARTY_ID_TAG)
    private Long pollPartyId;
}
