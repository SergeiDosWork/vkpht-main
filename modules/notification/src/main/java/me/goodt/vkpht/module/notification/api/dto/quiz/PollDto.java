package me.goodt.vkpht.module.notification.api.dto.quiz;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollDto {
    private Long id;
    @JsonSetter(DtoTagConstants.DATE_START_TAG)
    private Date dateStart;
    @JsonSetter(DtoTagConstants.DATE_END_TAG)
    private Date dateEnd;
    private String name;
    private String description;
    @JsonSetter(DtoTagConstants.IS_ANONYMOUS_TAG)
    private Integer isAnonymous;
    private Boolean startable;
    @JsonSetter(DtoTagConstants.POLL_TYPE_TAG)
    private PollTypeDto pollType;
    @JsonSetter(DtoTagConstants.POLL_PARTY_TAG)
    private PollPartyDto pollParty;
}
