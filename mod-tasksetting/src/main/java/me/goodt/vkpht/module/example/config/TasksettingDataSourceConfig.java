package me.goodt.vkpht.module.example.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "me.goodt.vkpht.module.example",
    entityManagerFactoryRef = "tasksettingEntityManagerFactory",
    transactionManagerRef = "tasksettingTransactionManager"
)
public class TasksettingDataSourceConfig {

    @Bean(name = "tasksettingDataSourceProperties")
    @ConfigurationProperties("datasources.common")
    public DataSourceProperties tasksettingDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "tasksettingDataSource")
    public DataSource orgstructureDataSource(@Qualifier("tasksettingDataSourceProperties") DataSourceProperties tasksettingDataSourceProperties) {
        HikariDataSource dataSource = tasksettingDataSourceProperties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();

        return dataSource;
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

}
