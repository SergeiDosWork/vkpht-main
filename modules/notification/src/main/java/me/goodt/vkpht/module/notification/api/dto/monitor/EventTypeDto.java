/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.goodt.vkpht.module.notification.api.dto.monitor;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

import me.goodt.vkpht.module.notification.api.dto.DtoTagConstants;

public class EventTypeDto {

	private Long id;
	private Long type;
	private String name;
	private String description;
	private Integer isImportant;
	private EventTypeManifestationDto manifestation;
	private List<EstimationCriteriaDto> criteries;

	public EventTypeDto() {
	}

	public EventTypeDto(Long id, Long type, String name, String description, Integer isImportant,
						EventTypeManifestationDto manifestation, List<EstimationCriteriaDto> criteries) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.description = description;
		this.isImportant = isImportant;
		this.manifestation = manifestation;
		this.criteries = criteries;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsImportant() {
		return isImportant;
	}

	@JsonSetter(DtoTagConstants.IS_IMPORTANT_TAG)
	public void setIsImportant(Integer isImportant) {
		this.isImportant = isImportant;
	}

	public EventTypeManifestationDto getManifestation() {
		return manifestation;
	}

	public void setManifestation(EventTypeManifestationDto manifestation) {
		this.manifestation = manifestation;
	}

	public List<EstimationCriteriaDto> getCriteries() {
		return criteries;
	}

	public void setCriteries(List<EstimationCriteriaDto> criteries) {
		this.criteries = criteries;
	}
}
