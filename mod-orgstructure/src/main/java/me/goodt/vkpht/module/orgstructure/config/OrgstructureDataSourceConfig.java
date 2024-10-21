package me.goodt.vkpht.module.orgstructure.config;

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
    basePackages = "me.goodt.vkpht.module.orgstructure",
    entityManagerFactoryRef = "orgstructureEntityManagerFactory",
    transactionManagerRef = "orgstructureTransactionManager"
)
@Import({OrgstructureDataSourceConfig.SpringLiquibaseDependsOnPostProcessor.class})
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

    @Component
    public static class OrgstructureSchemaInitBean implements InitializingBean {

        private final DataSource dataSource;
        private final String schemaName = "orgstructure";

        public OrgstructureSchemaInitBean(@Qualifier("orgstructureDataSource") DataSource dataSource) {
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

    @ConditionalOnBean(OrgstructureSchemaInitBean.class)
    static class SpringLiquibaseDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
        SpringLiquibaseDependsOnPostProcessor() {
            // Configure the 3rd party SpringLiquibase bean to depend on our SchemaInitBean
            super(SpringLiquibase.class, OrgstructureSchemaInitBean.class);
        }
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
