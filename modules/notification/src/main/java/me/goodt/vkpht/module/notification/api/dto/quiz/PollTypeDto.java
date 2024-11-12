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
public class PollTypeDto {
	private Long id;
	@JsonSetter(DtoTagConstants.DATE_FROM_TAG)
	private Date dateFrom;
	@JsonSetter(DtoTagConstants.DATE_TO_TAG)
	private Date dateTo;
	private String name;
	private Long code;
}
