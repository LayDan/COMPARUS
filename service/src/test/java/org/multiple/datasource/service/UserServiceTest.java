package org.multiple.datasource.service;

import org.junit.jupiter.api.*;
import org.multiple.datasource.configuration.db.DataBaseLayer;
import org.multiple.datasource.configuration.db.DataSourceConfiguration;
import org.multiple.datasource.configuration.db.MultipleDataBaseLayer;
import org.multiple.datasource.configuration.db.Strategy;
import org.multiple.datasource.entity.User;
import org.openapitools.model.Filters;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.List;

class UserServiceTest {

    static PostgreSQLContainer<?> postgres1;
    static PostgreSQLContainer<?> postgres2;

    private UserService userService;

    @BeforeAll
    static void beforeAll() {
        postgres1 = new PostgreSQLContainer<>(
                "postgres:16-alpine"
        ).withDatabaseName("test")
                .withInitScript("docker1.sql");
        postgres1.start();
        postgres2 = new PostgreSQLContainer<>(
                "postgres:16-alpine"
        ).withDatabaseName("test_2")
                .withInitScript("docker2.sql");
        postgres2.start();
    }

    @AfterAll
    static void afterAll() {
        postgres1.stop();
        postgres2.stop();
    }

    @BeforeEach
    void setUp() {
        var dataSourceConfiguration = new DataSourceConfiguration();
        DataSourceConfiguration.DataSourceConfig dataSourceConfig1 = new DataSourceConfiguration.DataSourceConfig(
                "test-1",
                Strategy.POSTGRES,
                postgres1.getJdbcUrl(),
                "users_more",
                postgres1.getUsername(),
                postgres1.getPassword(),
                new HashMap<>() {{
                    put("id", "test_id");
                    put("name", "nano_name");
                    put("username", "username_super");
                    put("surname", "surname_with_bao");
                }}
        );
        DataSourceConfiguration.DataSourceConfig dataSourceConfig2 = new DataSourceConfiguration.DataSourceConfig(
                "test-1",
                Strategy.POSTGRES,
                postgres2.getJdbcUrl(),
                "users",
                postgres2.getUsername(),
                postgres2.getPassword(),
                new HashMap<>() {{
                    put("id", "id_chicken");
                    put("name", "good_name_man");
                    put("username", "username_from_mail_ru");
                    put("surname", "surname_with_black_cat");
                }}
        );
        dataSourceConfiguration.setDataSources(List.of(dataSourceConfig1, dataSourceConfig2));
        DataBaseLayer dataBaseLayer = new MultipleDataBaseLayer(dataSourceConfiguration);
        this.userService = new UserService(dataBaseLayer);
    }


    @Test
    void getUsers() {
        List<User> users = userService.getUsers(new Filters());
        Assertions.assertEquals(users.size(), 2);
    }
}