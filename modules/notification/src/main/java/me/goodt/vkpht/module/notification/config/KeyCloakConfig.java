/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.goodt.vkpht.module.notification.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KeyCloakConfig {
	@Value("${appConfig.keyCloak.using.baseUrl}")
	private String baseUrl;

	@Value("${appConfig.keyCloak.openidAccessTokenUrl}")
	private String tokenUrl;

	@Value("${appConfig.keyCloak.using.clientId}")
	private String clientId;

	@Value("${appConfig.keyCloak.using.clientSecret}")
	private String clientSecret;

	@Value("${appConfig.keyCloak.using.serviceUsername}")
	private String username;

	@Value("${appConfig.keyCloak.using.servicePassword}")
	private String password;

	@Value("${appConfig.keyCloak.using.dbUrl}")
	private String keyCloakDatabaseUrl;

	@Value("${appConfig.keyCloak.using.dbUsername}")
	private String dbUsername;

	@Value("${appConfig.keyCloak.using.dbPassword}")
	private String dbPassword;
}
