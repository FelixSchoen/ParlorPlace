package com.fschoen.parlorplace.backend.integration.utility;

import org.hibernate.boot.*;
import org.hibernate.boot.registry.*;
import org.hibernate.dialect.*;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.*;
import org.springframework.orm.hibernate5.*;

import javax.sql.*;

import static org.hibernate.cfg.AvailableSettings.*;

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
