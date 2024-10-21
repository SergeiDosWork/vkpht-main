package me.goodt.vkpht.module.common.config;

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
    basePackages = "me.goodt.vkpht.module.common",
    entityManagerFactoryRef = "commonEntityManagerFactory",
    transactionManagerRef = "commonTransactionManager"
)
public class CommonDataSourceConfig {

    @Bean(name = "commonDataSourceProperties")
    @ConfigurationProperties("datasources.common")
    public DataSourceProperties commonDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "commonDataSource")
    public DataSource orgstructureDataSource(@Qualifier("commonDataSourceProperties") DataSourceProperties commonDataSourceProperties) {
        HikariDataSource dataSource = commonDataSourceProperties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();

        return dataSource;
    }

    @Bean(name = "commonEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean commonEntityManagerFactory(@Qualifier("commonDataSource") DataSource commonDataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(commonDataSource);
        em.setPackagesToScan("me.goodt.vkpht.module.common"); // Укажите пакет для сущностей
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean(name = "commonTransactionManager")
    public PlatformTransactionManager commonTransactionManager(@Qualifier("commonEntityManagerFactory") EntityManagerFactory commonEntityManagerFactory) {
        return new JpaTransactionManager(commonEntityManagerFactory);
    }

    @Bean(name = "commonLiquibaseProperties")
    @ConfigurationProperties(prefix = "liquibase.common")
    public LiquibaseProperties commonLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase commonLiquibase(@Qualifier("commonDataSource") DataSource commonDataSource,
                                           @Qualifier("commonLiquibaseProperties") LiquibaseProperties commonLiquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(commonDataSource);
        liquibase.setChangeLog(commonLiquibaseProperties.getChangeLog());
        liquibase.setContexts(commonLiquibaseProperties.getContexts());
        liquibase.setDefaultSchema(commonLiquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(commonLiquibaseProperties.getLiquibaseSchema());
        liquibase.setDropFirst(commonLiquibaseProperties.isDropFirst());
        liquibase.setChangeLogParameters(commonLiquibaseProperties.getParameters());
        return liquibase;
    }

}
