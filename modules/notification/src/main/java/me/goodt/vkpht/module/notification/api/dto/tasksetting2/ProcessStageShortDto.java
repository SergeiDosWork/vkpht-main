package me.goodt.vkpht.module.notification.api.dto.tasksetting2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

@Getter
@Setter
public class ProcessStageShortDto {
	private Long id;
	private String params;
    @JsonProperty(DtoTagConstants.STAGE_TYPE_TAG)
	private StageTypeDto stageType;
    @JsonProperty(DtoTagConstants.DATE_FROM_TAG)
    private Date dateFrom;
    @JsonProperty(DtoTagConstants.DATE_START_CONFIRM_TAG)
    private Date dateStartConfirm;
    @JsonProperty(DtoTagConstants.DATE_TO_TAG)
    private Date dateTo;
    @JsonProperty(DtoTagConstants.DATE_END_CONFIRM_TAG)
    private Date dateEndConfirm;
    @JsonProperty(DtoTagConstants.DATE_START_TAG)
    private Date dateStart;
    @JsonProperty(DtoTagConstants.DATE_END_TAG)
    private Date dateEnd;
    @JsonProperty(DtoTagConstants.EXTERNAL_ID_TAG)
    private String externalId;
}
