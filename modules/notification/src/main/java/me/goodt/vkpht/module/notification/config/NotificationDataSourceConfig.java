package me.goodt.vkpht.module.notification.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "me.goodt.vkpht.module.notification",
    entityManagerFactoryRef = "notificationEntityManagerFactory",
    transactionManagerRef = "notificationTransactionManager"
)
@Import({NotificationDataSourceConfig.SpringLiquibaseDependsOnPostProcessor.class})
public class NotificationDataSourceConfig {

    @Bean(name = "notificationDataSourceProperties")
    @ConfigurationProperties("datasources.notification")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "notificationDataSource")
    public DataSource notificationDataSource(@Qualifier("notificationDataSourceProperties") DataSourceProperties notificationDataSourceProperties) {
        HikariDataSource dataSource = notificationDataSourceProperties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();

        return dataSource;
    }

    @Bean(name = "notificationEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean notificationEntityManagerFactory(@Qualifier("notificationDataSource") DataSource notificationDataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(notificationDataSource);
        em.setPackagesToScan("me.goodt.vkpht.module.notification"); // Укажите пакет для сущностей
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return em;
    }

    @Bean(name = "notificationTransactionManager")
    public PlatformTransactionManager notificationTransactionManager(@Qualifier("notificationEntityManagerFactory") EntityManagerFactory notificationEntityManagerFactory) {
        return new JpaTransactionManager(notificationEntityManagerFactory);
    }

    @Bean(name = "notificationLiquibaseProperties")
    @ConfigurationProperties(prefix = "liquibase.notification")
    public LiquibaseProperties notificationLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase notificationLiquibase(@Qualifier("notificationDataSource") DataSource notificationDataSource,
                                                 @Qualifier("notificationLiquibaseProperties") LiquibaseProperties notificationLiquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(notificationDataSource);
        liquibase.setChangeLog(notificationLiquibaseProperties.getChangeLog());
        liquibase.setContexts(notificationLiquibaseProperties.getContexts());
        liquibase.setDefaultSchema(notificationLiquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(notificationLiquibaseProperties.getLiquibaseSchema());
        liquibase.setDropFirst(notificationLiquibaseProperties.isDropFirst());
        liquibase.setChangeLogParameters(notificationLiquibaseProperties.getParameters());
        return liquibase;
    }

    @Component
    public static class NotificationSchemaInitBean implements InitializingBean {

        private final DataSource dataSource;
        private final String schemaName = "notification";

        public NotificationSchemaInitBean(@Qualifier("notificationDataSource") DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public void afterPropertiesSet() {
            try (Connection conn = dataSource.getConnection();
                 Statement statement = conn.createStatement()) {
                log.info("Going to create DB schema '{}' if not exists.", schemaName);
                statement.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create schema '" + schemaName + "'", e);
            }
        }
    }

    @ConditionalOnBean(NotificationSchemaInitBean.class)
    static class SpringLiquibaseDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
        SpringLiquibaseDependsOnPostProcessor() {
            // Configure the 3rd party SpringLiquibase bean to depend on our SchemaInitBean
            super(SpringLiquibase.class, NotificationSchemaInitBean.class);
        }
    }

}
