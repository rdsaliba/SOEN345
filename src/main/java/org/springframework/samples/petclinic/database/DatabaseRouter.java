package org.springframework.samples.petclinic.database;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class DatabaseRouter {
    @Bean
    @ConfigurationProperties(prefix="spring.datasource.mysql")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix="spring.datasource.postgres")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        Map<Object, Object> targetDatasources = new HashMap<Object, Object>(){{
            put(Database.SECONDARY, secondaryDataSource());
            put(Database.PRIMARY, primaryDataSource());
        }};
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(primaryDataSource());
        routingDataSource.setTargetDataSources(targetDatasources);
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

}