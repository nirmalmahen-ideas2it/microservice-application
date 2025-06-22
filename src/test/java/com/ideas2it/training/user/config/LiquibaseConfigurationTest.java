package com.ideas2it.training.user.config;

import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LiquibaseConfigurationTest {
    @Test
    void testLiquibaseBeanCreation() {
        Environment env = mock(Environment.class);
        LiquibaseConfiguration config = new LiquibaseConfiguration(env);
        Executor executor = mock(Executor.class);
        LiquibaseProperties liquibaseProperties = mock(LiquibaseProperties.class);
        ObjectProvider<DataSource> liquibaseDataSource = mock(ObjectProvider.class);
        ObjectProvider<DataSource> dataSource = mock(ObjectProvider.class);
        ApplicationProperties applicationProperties = mock(ApplicationProperties.class);
        DataSourceProperties dataSourceProperties = mock(DataSourceProperties.class);
        when(applicationProperties.getLiquibase()).thenReturn(new ApplicationProperties.Liquibase());
        SpringLiquibase liquibase = config.liquibase(executor, liquibaseProperties,liquibaseDataSource,dataSource,
                applicationProperties, dataSourceProperties);
        assertNotNull(liquibase);
    }
}