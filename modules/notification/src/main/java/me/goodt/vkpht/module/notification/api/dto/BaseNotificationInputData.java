package me.goodt.vkpht.module.notification.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

import me.goodt.vkpht.module.notification.application.utils.DataUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseNotificationInputData {
	// необязательный параметр
	// список пар токен=значение, которые подставляются в итоговый json
	@JsonSetter(DtoTagConstants.TOKEN_VALUES_TAG)
	private Map<String, Object> tokenValues;
	// обязательный параметр
	// код (notification_template.code), по нему происходит поиск нужной информации из БД
	private String code;
	// необязательный параметр
	// список пар имя_параметра=значение, необходим для поиска значений токенов
	private Map<String, Object> parameters;

	@Override
	public String toString() {
		return "BaseNotificationInputData{" +
			"tokenValues=" + DataUtils.mapToString(tokenValues) +
			", code='" + code + '\'' +
			", parameters=" + DataUtils.mapToString(parameters) +
			'}';
	}
}
