package org.multiple.datasource.configuration.db;

import com.fasterxml.jackson.core.type.TypeReference;
import org.multiple.datasource.configuration.db.request.Request;
import org.multiple.datasource.entity.User;

import java.util.List;

public interface DataBaseLayer {

    default <T> T makeRequest(Request request, TypeReference<T> typeReturn) {
        throw new UnsupportedOperationException("This method is for the future");
    }

    List<User> makeRequest(Request request);
}
