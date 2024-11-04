package org.multiple.datasource.configuration.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataBaseConfig {

    @Bean
    public DataSourceConfiguration dataSourceConfiguration(){
        return new DataSourceConfiguration();
    }

    @Bean
    public DataBaseLayer dbLayer(){
        return new MultipleDataBaseLayer(dataSourceConfiguration());
    }

}
