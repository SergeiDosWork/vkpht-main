package me.goodt.vkpht.module.tasksetting.config;

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
    basePackages = "me.goodt.vkpht.module.tasksetting",
    entityManagerFactoryRef = "tasksettingEntityManagerFactory",
    transactionManagerRef = "tasksettingTransactionManager"
)
@Import({TasksettingDataSourceConfig.SpringLiquibaseDependsOnPostProcessor.class})
public class TasksettingDataSourceConfig {

    @Bean(name = "tasksettingDataSourceProperties")
    @ConfigurationProperties("datasources.tasksetting")
    public DataSourceProperties tasksettingDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "tasksettingDataSource")
    public DataSource orgstructureDataSource(@Qualifier("tasksettingDataSourceProperties") DataSourceProperties tasksettingDataSourceProperties) {
        return tasksettingDataSourceProperties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean(name = "tasksettingEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tasksettingEntityManagerFactory(@Qualifier("tasksettingDataSource") DataSource tasksettingDataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(tasksettingDataSource);
        em.setPackagesToScan("me.goodt.vkpht.module.tasksetting"); // Укажите пакет для сущностей
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return em;
    }

    @Bean(name = "tasksettingTransactionManager")
    public PlatformTransactionManager tasksettingTransactionManager(@Qualifier("tasksettingEntityManagerFactory") EntityManagerFactory tasksettingEntityManagerFactory) {
        return new JpaTransactionManager(tasksettingEntityManagerFactory);
    }

    @Bean(name = "tasksettingLiquibaseProperties")
    @ConfigurationProperties(prefix = "liquibase.tasksetting")
    public LiquibaseProperties tasksettingLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase tasksettingLiquibase(@Qualifier("tasksettingDataSource") DataSource tasksettingDataSource,
                                                @Qualifier("tasksettingLiquibaseProperties") LiquibaseProperties tasksettingLiquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(tasksettingDataSource);
        liquibase.setChangeLog(tasksettingLiquibaseProperties.getChangeLog());
        liquibase.setContexts(tasksettingLiquibaseProperties.getContexts());
        liquibase.setDefaultSchema(tasksettingLiquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(tasksettingLiquibaseProperties.getLiquibaseSchema());
        liquibase.setDropFirst(tasksettingLiquibaseProperties.isDropFirst());
        liquibase.setChangeLogParameters(tasksettingLiquibaseProperties.getParameters());
        return liquibase;
    }

    @Component
    public static class TasksettingSchemaInitBean implements InitializingBean {

        private final DataSource dataSource;
        private final String schemaName = "tasksetting";

        public TasksettingSchemaInitBean(@Qualifier("tasksettingDataSource") DataSource dataSource) {
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

    @ConditionalOnBean(TasksettingSchemaInitBean.class)
    static class SpringLiquibaseDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
        SpringLiquibaseDependsOnPostProcessor() {
            // Configure the 3rd party SpringLiquibase bean to depend on our SchemaInitBean
            super(SpringLiquibase.class, TasksettingSchemaInitBean.class);
        }
    }

}
