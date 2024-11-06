package me.goodt.vkpht.module.tasksetting.config;

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
    basePackages = "me.goodt.vkpht.module.example", 
    entityManagerFactoryRef = "exampleEntityManagerFactory",
    transactionManagerRef = "exampleTransactionManager"
)
@Import({ExampleDataSourceConfig.SpringLiquibaseDependsOnPostProcessor.class})
public class ExampleDataSourceConfig {

    @Bean(name = "exampleDataSourceProperties")
    @ConfigurationProperties("datasources.example")
    public DataSourceProperties exampleDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "exampleDataSource")
    public DataSource orgstructureDataSource(@Qualifier("exampleDataSourceProperties") DataSourceProperties exampleDataSourceProperties) {
        HikariDataSource dataSource = exampleDataSourceProperties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();

        return dataSource;
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

    @Component
    public static class ExampleSchemaInitBean implements InitializingBean {

        private final DataSource dataSource;
        private final String schemaName = "example";

        public ExampleSchemaInitBean(@Qualifier("exampleDataSource") DataSource dataSource) {
            this.dataSource = dataSource;
    }

        @Override
        public void afterPropertiesSet() {
            try (Connection conn = dataSource.getConnection();
                 Statement statement = conn.createStatement()) {
                log.info("Going to create DB schema '{}' if not exists.", schemaName);
                statement.execute("create schema if not exists " + schemaName);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create schema '" + schemaName + "'", e);
            }
        }
    }

    @ConditionalOnBean(ExampleSchemaInitBean.class)
    static class SpringLiquibaseDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
        SpringLiquibaseDependsOnPostProcessor() {
            // Configure the 3rd party SpringLiquibase bean to depend on our SchemaInitBean
            super(SpringLiquibase.class, ExampleSchemaInitBean.class);
        }
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
