package org.multiple.datasource.configuration.db;

import org.multiple.datasource.configuration.db.DataBaseLayer;
import org.multiple.datasource.configuration.db.DataSourceConfiguration;
import org.multiple.datasource.configuration.db.DataSourceDefinition;
import org.multiple.datasource.configuration.db.DataSourceFactory;
import org.multiple.datasource.configuration.db.request.Request;
import org.multiple.datasource.entity.User;

import java.util.List;

public final class MultipleDataBaseLayer implements DataBaseLayer {

    private final List<DataSourceDefinition> dataSources;

    public MultipleDataBaseLayer(DataSourceConfiguration dataSourceConfiguration) {
        this.dataSources = parseConfig(dataSourceConfiguration);
    }

    private List<DataSourceDefinition> parseConfig(DataSourceConfiguration config) {
        return config.getDataSources()
                .stream()
                .map(DataSourceFactory::initDataSource)
                .toList();
    }

    @Override
    public List<User> makeRequest(Request request) {
        return dataSources.stream().map(
                db -> db.execute(request)
        ).flatMap(List::stream).toList();
    }
}
