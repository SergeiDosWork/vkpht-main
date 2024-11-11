package me.goodt.vkpht.module.notification.application.impl;

import lombok.Getter;

@Getter
public class TokenWithValues {
	private final String basicValue;
	private String[] constAndValue;

	public TokenWithValues(String fullValue) {
		String[] array = fullValue.split("[.]");
		this.basicValue = array[0];
		if (array.length > 1) {
			constAndValue = new String[array.length - 1];
			int j = 0;
			for (int i = 1; i < array.length; i++) {
				constAndValue[j] = array[i];
				j++;
			}
		}
	}
}
