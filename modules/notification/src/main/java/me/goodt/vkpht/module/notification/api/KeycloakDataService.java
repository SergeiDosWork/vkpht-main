package me.goodt.vkpht.module.notification.api;

import java.util.Collection;
import java.util.List;

public interface KeycloakDataService {
	List<String> getUsersKeycloakIds(Collection<String> employeeExternalIds);
}
