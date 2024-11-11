package me.goodt.vkpht.module.notification.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LogInfoAboutMethod {
	private UUID hash;
	private Timestamp date;
	private String methodUrl;
	private String employeeId;
	private Map<String, Object> getParams;
	private Object postParams;
}
