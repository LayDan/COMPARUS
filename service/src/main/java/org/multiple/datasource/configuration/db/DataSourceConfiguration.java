package org.multiple.datasource.configuration.db;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties
public class DataSourceConfiguration {
    @Override
    public String toString() {
        return "DataSourceConfiguration{" +
                "dataSources=" + dataSources +
                '}';
    }

    private final Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

    public List<DataSourceConfig> dataSources;

    public List<DataSourceConfig> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceConfig> dataSources) {
        this.dataSources = dataSources;
    }

    public static class DataSourceConfig {

        @Override
        public String toString() {
            return "DataSourceConfig{" +
                    "name='" + name + '\'' +
                    ", strategy='" + strategy + '\'' +
                    ", url='" + url + '\'' +
                    ", table='" + table + '\'' +
                    ", user='" + user + '\'' +
                    ", password='" + password + '\'' +
                    ", mapping=" + mapping +
                    '}';
        }


        private String name;
        private Strategy strategy;
        private String url;
        private String table;
        private String user;
        private String password;
        private Map<String, String> mapping;

        // Getters and setters for each field

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Strategy getStrategy() {
            return strategy;
        }

        public void setStrategy(Strategy strategy) {
            this.strategy = strategy;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Map<String, String> getMapping() {
            return mapping;
        }

        public void setMapping(Map<String, String> mapping) {
            this.mapping = mapping;
        }

        public DataSourceConfig(String name, Strategy strategy, String url, String table, String user, String password, Map<String, String> mapping) {
            this.name = name;
            this.strategy = strategy;
            this.url = url;
            this.table = table;
            this.user = user;
            this.password = password;
            this.mapping = mapping;
        }

        public DataSourceConfig() {
        }
    }

    @PostConstruct
    public void log() {
        logger.info(this.toString());
    }
}
