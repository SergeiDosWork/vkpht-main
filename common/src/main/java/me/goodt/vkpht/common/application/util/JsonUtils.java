package me.goodt.vkpht.common.application.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

@UtilityClass
@Slf4j
public class JsonUtils {

	public String getJsonValueByKey(String jsonString, String key) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			return jsonObject.getString(key);
		} catch (JSONException e) {
			log.error("Ошибка разбора json строки {}", jsonString);
			return null;
		}
	}

	public boolean checkJsonValue(String jsonString, String key, String checkValue) {
		return Objects.equals(getJsonValueByKey(jsonString, key), checkValue);
	}

}
