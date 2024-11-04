package org.multiple.datasource.configuration.db;

import com.fasterxml.jackson.core.type.TypeReference;
import org.multiple.datasource.configuration.db.request.Request;
import org.multiple.datasource.entity.User;

import java.util.List;

public interface DataSourceDefinition {

    default <T> T execute(Request request, TypeReference<T> typeReturn){
        throw new UnsupportedOperationException("This method is for a future");
    } //for this we need more data and factory mapping

    List<User> execute(Request request);
}
