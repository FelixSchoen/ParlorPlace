package com.fschoen.parlorplace.backend.integration.utility;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.H2Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

import javax.sql.DataSource;

import static org.hibernate.cfg.AvailableSettings.DATASOURCE;
import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.PHYSICAL_NAMING_STRATEGY;

@Configuration
public class TestIsolationServiceFactory {

    @Bean
    public static TestIsolationService testIsolationService(DataSource dataSource) {
        return new TestIsolationService(createMetaData(dataSource));
    }

    private static Metadata createMetaData(DataSource dataSource) {
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
                .applySetting(DATASOURCE, dataSource)
                .applySetting(DIALECT, H2Dialect.class)
                .applySetting(PHYSICAL_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        MetadataSources metadataSources = new MetadataSources(registryBuilder.build());

        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        new LocalSessionFactoryBuilder(null, resourceLoader, metadataSources)
                .scanPackages("com.fschoen.parlorplace.backend.entity");

        return metadataSources.buildMetadata();
    }

}
