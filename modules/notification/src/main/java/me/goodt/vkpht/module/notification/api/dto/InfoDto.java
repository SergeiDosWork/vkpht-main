package me.goodt.vkpht.module.notification.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoDto {

	private String apiVersion;

	private String environment;

	private String databaseDriver;

	private String databaseUrl;

	private String baseKeyCloakUrl;

	private String buildVersion;

	private String serverTime;
}
