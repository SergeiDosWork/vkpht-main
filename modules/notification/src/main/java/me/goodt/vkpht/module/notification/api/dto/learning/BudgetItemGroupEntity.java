package me.goodt.vkpht.module.notification.api.dto.learning;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_FROM_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.DATE_TO_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.EXTERNAL_ID_TAG;
import static me.goodt.vkpht.module.notification.api.dto.DtoTagConstants.PARENT_ID_TAG;

@Getter
@Setter
public class BudgetItemGroupEntity {
	private Long id;
	@JsonSetter(DATE_FROM_TAG)
	private Date dateFrom;
	@JsonSetter(DATE_TO_TAG)
	private Date dateTo;
	private String name;
	@JsonSetter(EXTERNAL_ID_TAG)
	private String externalId;
	@JsonSetter(PARENT_ID_TAG)
	private Long parentId;
}
