package me.goodt.vkpht.module.common.config;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "datasources.common")
    public DataSource taskSettingDataSource() {
        return new HikariDataSource();
    }

    @Bean
    @ConfigurationProperties(prefix = "liquibase.common")
    public LiquibaseProperties tasksettingLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase tasksettingLiquibase(DataSource taskSettingDataSource, LiquibaseProperties tasksettingLiquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(taskSettingDataSource);
        liquibase.setChangeLog(tasksettingLiquibaseProperties.getChangeLog());
        liquibase.setDefaultSchema(tasksettingLiquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(tasksettingLiquibaseProperties.getLiquibaseSchema());
        return liquibase;
    }

}
