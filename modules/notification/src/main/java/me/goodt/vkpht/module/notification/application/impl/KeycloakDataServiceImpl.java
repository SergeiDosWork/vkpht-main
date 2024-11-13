package me.goodt.vkpht.module.notification.application.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import me.goodt.vkpht.module.notification.api.KeycloakDataService;
import me.goodt.vkpht.module.notification.config.KeyCloakConfig;

@Slf4j
@Service
public class KeycloakDataServiceImpl implements KeycloakDataService {

    private final KeyCloakConfig keyCloakConfig;
    private final Boolean keycloakEnabled;

    public KeycloakDataServiceImpl(KeyCloakConfig keyCloakConfig, @Value("${appConfig.keyCloak.isEnabled:false}") Boolean keycloakEnabled) {
        this.keyCloakConfig = keyCloakConfig;
        this.keycloakEnabled = keycloakEnabled;
    }

    @Override
    public List<String> getUsersKeycloakIds(Collection<String> employeeExternalIds) {
        List<String> usersKeycloakIds = Collections.emptyList();

        if (keycloakEnabled) {
            usersKeycloakIds = new ArrayList<>();

            StringJoiner stringJoiner = new StringJoiner("', '", "'", "'");
            employeeExternalIds.forEach(stringJoiner::add);

            String sql = (
                "SELECT ue.id FROM user_entity AS ue " +
                    "INNER JOIN user_attribute AS ua ON ua.user_id = ue.id " +
                    "WHERE ue.enabled = true AND ue.realm_id = 'lukoil' AND ua.name = 'employee_id' AND ua.value IN (%s)").formatted(stringJoiner
            );

            try (Connection conn = DriverManager.getConnection(keyCloakConfig.getKeyCloakDatabaseUrl(), keyCloakConfig.getDbUsername(), keyCloakConfig.getDbPassword()); Statement st = conn.createStatement()) {
                try (ResultSet rs = st.executeQuery(sql)) {
                    while (rs.next()) {
                        usersKeycloakIds.add(rs.getString("id"));
                    }
                }
            } catch (SQLException e) {
                log.error("Failed to get keycloak id from db. Error: {}", e.getMessage());
            }
        }

        return usersKeycloakIds;
    }
}
