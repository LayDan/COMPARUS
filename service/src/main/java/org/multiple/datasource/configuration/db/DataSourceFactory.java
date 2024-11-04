package org.multiple.datasource.configuration.db;

import org.multiple.datasource.configuration.db.request.Request;
import org.multiple.datasource.entity.User;
import org.multiple.datasource.entity.UserBuilder;
import org.multiple.datasource.entity.UserDAO;
import org.multiple.datasource.exception.NoMappingInfo;
import org.multiple.datasource.exception.NoSelectedFieldsDetected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSourceFactory {

    public static DataSourceDefinition initDataSource(DataSourceConfiguration.DataSourceConfig config) {
        return switch (config.getStrategy()) {
            case POSTGRES -> new DataSource(config) {
                @Override
                public List<User> execute(final Request request) {
                    try (Connection connection = getConnection(); var statement = connection.createStatement()) {
                        String sqlQuery = generateSqlStatement(request);
                        ResultSet resultSet = statement.executeQuery(sqlQuery);

                        if (config.getMapping().entrySet().stream().anyMatch(entry -> request.getSelectedFields().contains(entry.getKey()))) {


                            List<User> users = new ArrayList<>();
                            while (resultSet.next()) {
                                UserBuilder userBuilder = new UserBuilder();
                                config.getMapping().entrySet().stream().filter(entry -> request.getSelectedFields().contains(entry.getKey())).forEach(entry -> {
                                    try {
                                        if (entry.getKey().equals(UserDAO.ID)) {
                                            userBuilder.id(resultSet.getString(entry.getValue()));
                                        }
                                        if (entry.getKey().equals(UserDAO.NAME)) {
                                            userBuilder.name(resultSet.getString(entry.getValue()));
                                        }
                                        if (entry.getKey().equals(UserDAO.USERNAME)) {
                                            userBuilder.username(resultSet.getString(entry.getValue()));
                                        }
                                        if (entry.getKey().equals(UserDAO.SURNAME)) {
                                            userBuilder.surname(resultSet.getString(entry.getValue()));
                                        }
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                var user = userBuilder.build();
                                users.add(user);
                            }
                            return users;
                        }
                        return List.of();
                    } catch (SQLException e) {
                        handleExceptions(e);
                        return null;
                    }
                }
            };
            default -> throw new UnsupportedOperationException("We do not support this type of DB");
        };
    }

    public static abstract class DataSource implements DataSourceDefinition {
        protected final Logger log;
        private final DataSourceConfiguration.DataSourceConfig config;
        private final Map<Class<?>, String> entityMapping;

        protected DataSource(DataSourceConfiguration.DataSourceConfig config) {
            this.config = config;
            this.log = LoggerFactory.getLogger(String.format("%s-%s", this.getClass().getName(), config.getName()));
            this.entityMapping = new HashMap<>() {{
                put(User.class, config.getTable()); //hardcode, because this solution for growing up, but nowaday I don't have enough information for it
            }}; //we need to update configuration to store more types here
            testConnect();
        }

        protected Connection getConnection() throws SQLException {
            return DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
        }

        protected String generateSqlStatement(Request request) {
            Class<?> from = request.getFrom();
            var table = entityMapping.get(from);
            if (table == null) {
                throw new NoMappingInfo("Class %s does not have table, please check your configuration".formatted(from.getSimpleName()));
                //Actually, in this case we need to use checked exception with log
            }
            var selectedMappingFields = request.getSelectedFields();
            if (selectedMappingFields.isEmpty()) {
                throw new NoSelectedFieldsDetected("Request to table %s does not have selected fields, please assign data to be selected".formatted(table));
            }
            var realFields = selectedMappingFields.stream().map(key -> config.getMapping().get(key)).toList();
            if (realFields.isEmpty() || realFields.size() != selectedMappingFields.size()) {
                throw new NoMappingInfo("Request to table %s does not have selected fields [mapping size = %s, selected size = %s] or mapping does not contain enough information about some fields, please check your configuration".formatted(table, realFields.size(), selectedMappingFields.size()));
            }

            var sqlQuery = """
                    SELECT %s from %s
                    """.formatted(String.join(",", realFields), table);
            String whereStatement = whereStatement(request);
            return whereStatement == null ? sqlQuery : sqlQuery.concat(whereStatement);
        }

        private String whereStatement(Request request) {
            return null;
        }

        private void testConnect() {
            try (var connection = getConnection(); var statement = connection.createStatement()) {
                log.info("Connection success, {}", config.getName());
                //test query
                statement.executeQuery(config.getStrategy().getTestQuery());
                log.info("Test query success");
            } catch (SQLException e) {
                handleExceptions(e);
            }
        }

        protected void handleExceptions(SQLException e) {
            switch (e.getSQLState()) {
                case "28P01" -> {
                    log.error("Password authentication failed, please check your credential - {}, {}", e.getSQLState(), e.getMessage());
                    throw new RuntimeException("Password authentication failed, please check your credential, code=%s, db=%s".formatted(e.getSQLState(), config.getName()));
                }
                default -> {
                    log.error(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }
}
