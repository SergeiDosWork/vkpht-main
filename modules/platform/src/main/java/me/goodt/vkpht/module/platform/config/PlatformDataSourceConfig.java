package me.goodt.vkpht.module.platform.config;

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
    basePackages = "me.goodt.vkpht.module.platform",
    entityManagerFactoryRef = "platformEntityManagerFactory",
    transactionManagerRef = "platformTransactionManager"
)
@Import({PlatformDataSourceConfig.SpringLiquibaseDependsOnPostProcessor.class})
public class PlatformDataSourceConfig {

    @Bean(name = "platformDataSourceProperties")
    @ConfigurationProperties("datasources.platform")
    public DataSourceProperties platformDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "platformDataSource")
    public DataSource orgstructureDataSource(@Qualifier("platformDataSourceProperties") DataSourceProperties platformDataSourceProperties) {
        return platformDataSourceProperties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean(name = "platformEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean platformEntityManagerFactory(@Qualifier("platformDataSource") DataSource platformDataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(platformDataSource);
        em.setPackagesToScan("me.goodt.vkpht.module.platform"); // Укажите пакет для сущностей
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean(name = "platformTransactionManager")
    public PlatformTransactionManager platformTransactionManager(@Qualifier("platformEntityManagerFactory") EntityManagerFactory platformEntityManagerFactory) {
        return new JpaTransactionManager(platformEntityManagerFactory);
    }

    @Bean(name = "platformLiquibaseProperties")
    @ConfigurationProperties(prefix = "liquibase.platform")
    public LiquibaseProperties platformLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase platformLiquibase(@Qualifier("platformDataSource") DataSource platformDataSource,
                                             @Qualifier("platformLiquibaseProperties") LiquibaseProperties platformLiquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(platformDataSource);
        liquibase.setChangeLog(platformLiquibaseProperties.getChangeLog());
        liquibase.setContexts(platformLiquibaseProperties.getContexts());
        liquibase.setDefaultSchema(platformLiquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(platformLiquibaseProperties.getLiquibaseSchema());
        liquibase.setDropFirst(platformLiquibaseProperties.isDropFirst());
        liquibase.setChangeLogParameters(platformLiquibaseProperties.getParameters());
        return liquibase;
    }

    @Component
    public static class PlatformSchemaInitBean implements InitializingBean {

        private final DataSource dataSource;
        private static final String SCHEMA_NAME = "platform";

        public PlatformSchemaInitBean(@Qualifier("platformDataSource") DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public void afterPropertiesSet() {
            try (Connection conn = dataSource.getConnection();
                 Statement statement = conn.createStatement()) {
                log.info("Going to create DB schema '{}' if not exists.", SCHEMA_NAME);
                statement.execute("CREATE SCHEMA IF NOT EXISTS " + SCHEMA_NAME);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create schema '" + SCHEMA_NAME + "'", e);
            }
        }
    }

    @ConditionalOnBean(PlatformSchemaInitBean.class)
    static class SpringLiquibaseDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
        SpringLiquibaseDependsOnPostProcessor() {
            // Configure the 3rd party SpringLiquibase bean to depend on our SchemaInitBean
            super(SpringLiquibase.class, PlatformSchemaInitBean.class);
        }
    }

}
