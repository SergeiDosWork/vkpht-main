package me.goodt.vkpht.module.orgstructure.config;

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
    basePackages = "me.goodt.vkpht.module.orgstructure",
    entityManagerFactoryRef = "orgstructureEntityManagerFactory",
    transactionManagerRef = "orgstructureTransactionManager"
)
public class OrgstructureDataSourceConfig {

    @Bean(name = "orgstructureDataSourceProperties")
    @ConfigurationProperties("datasources.orgstructure")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "orgstructureDataSource")
    public DataSource orgstructureDataSource(@Qualifier("orgstructureDataSourceProperties") DataSourceProperties orgstructureDataSourceProperties) {
        HikariDataSource dataSource = orgstructureDataSourceProperties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();

        return dataSource;
    }

    @Bean(name = "orgstructureEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean orgstructureEntityManagerFactory(@Qualifier("orgstructureDataSource") DataSource orgstructureDataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(orgstructureDataSource);
        em.setPackagesToScan("me.goodt.vkpht.module.orgstructure"); // Укажите пакет для сущностей
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return em;
    }

    @Bean(name = "orgstructureTransactionManager")
    public PlatformTransactionManager orgstructureTransactionManager(@Qualifier("orgstructureEntityManagerFactory") EntityManagerFactory orgstructureEntityManagerFactory) {
        return new JpaTransactionManager(orgstructureEntityManagerFactory);
    }

    @Bean(name = "orgstructureLiquibaseProperties")
    @ConfigurationProperties(prefix = "liquibase.orgstructure")
    public LiquibaseProperties orgstructureLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase orgstructureLiquibase(@Qualifier("orgstructureDataSource") DataSource orgstructureDataSource,
                                                 @Qualifier("orgstructureLiquibaseProperties") LiquibaseProperties orgstructureLiquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(orgstructureDataSource);
        liquibase.setChangeLog(orgstructureLiquibaseProperties.getChangeLog());
        liquibase.setContexts(orgstructureLiquibaseProperties.getContexts());
        liquibase.setDefaultSchema(orgstructureLiquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(orgstructureLiquibaseProperties.getLiquibaseSchema());
        liquibase.setDropFirst(orgstructureLiquibaseProperties.isDropFirst());
        liquibase.setChangeLogParameters(orgstructureLiquibaseProperties.getParameters());
        return liquibase;
    }

}
