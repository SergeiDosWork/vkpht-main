package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessDto {
    private Long id;
    private ProcessTypeDto type;
    private String name;
    @JsonProperty(DtoTagConstants.IS_STARTABLE_TAG)
    private Integer isStartable;
    @JsonProperty(DtoTagConstants.DATE_FROM_TAG)
    private Date dateFrom;
    @JsonProperty(DtoTagConstants.DATE_START_CONFIRM_TAG)
    private Date dateStartConfirm;
    @JsonProperty(DtoTagConstants.DATE_TO_TAG)
    private Date dateTo;
    @JsonProperty(DtoTagConstants.DATE_END_CONFIRM_TAG)
    private Date dateEndConfirm;
    @JsonProperty(DtoTagConstants.DATE_BLOCK_TAG)
    private Date dateBlock;
    private String params;
    @JsonProperty(DtoTagConstants.DATE_START_TAG)
    private Date dateStart;
    @JsonProperty(DtoTagConstants.DATE_END_TAG)
    private Date dateEnd;
    @JsonProperty(DtoTagConstants.EXTERNAL_ID_TAG)
    private String externalId;
}
