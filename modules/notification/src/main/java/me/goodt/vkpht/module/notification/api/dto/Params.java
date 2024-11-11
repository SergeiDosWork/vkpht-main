package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Params {

	private String color;

	public Params() {
	}

	public Params(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	@JsonSetter(DtoTagConstants.COLOR)
	public void setColor(String color) {
		this.color = color;
	}

}
