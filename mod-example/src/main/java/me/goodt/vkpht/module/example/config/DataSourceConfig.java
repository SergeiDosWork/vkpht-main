package me.goodt.vkpht.module.example.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
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
    entityManagerFactoryRef = "exampleEntityManagerFactory",
    transactionManagerRef = "exampleTransactionManager"
)
public class DataSourceConfig {

    @Bean(name = "exampleDataSource")
    @ConfigurationProperties(prefix = "datasources.example")
    public DataSource exampleDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "exampleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean exampleEntityManagerFactory(@Qualifier("exampleDataSource") DataSource exampleDataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(exampleDataSource);
        em.setPackagesToScan("me.goodt.vkpht.module.example"); // Укажите пакет для сущностей
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return em;
    }

    @Bean(name = "exampleTransactionManager")
    public PlatformTransactionManager exampleTransactionManager(@Qualifier("exampleEntityManagerFactory") EntityManagerFactory exampleEntityManagerFactory) {
        return new JpaTransactionManager(exampleEntityManagerFactory);
    }

    @Bean(name = "exampleLiquibaseProperties")
    @ConfigurationProperties(prefix = "liquibase.example")
    public LiquibaseProperties exampleLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase exampleLiquibase(@Qualifier("exampleDataSource") DataSource exampleDataSource,
                                                 @Qualifier("exampleLiquibaseProperties") LiquibaseProperties exampleLiquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(exampleDataSource);
        liquibase.setChangeLog(exampleLiquibaseProperties.getChangeLog());
        liquibase.setContexts(exampleLiquibaseProperties.getContexts());
        liquibase.setDefaultSchema(exampleLiquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(exampleLiquibaseProperties.getLiquibaseSchema());
        liquibase.setDropFirst(exampleLiquibaseProperties.isDropFirst());
        liquibase.setChangeLogParameters(exampleLiquibaseProperties.getParameters());
        return liquibase;
    }

}
