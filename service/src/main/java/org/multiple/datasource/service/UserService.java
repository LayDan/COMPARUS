package org.multiple.datasource.service;

import org.multiple.datasource.configuration.db.DataBaseLayer;
import org.multiple.datasource.configuration.db.request.MultipleDataSourceRequest;
import org.multiple.datasource.controller.Filters;
import org.multiple.datasource.entity.User;
import org.multiple.datasource.entity.UserDAO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final DataBaseLayer dbLayer;

    public UserService(DataBaseLayer dbLayer) {
        this.dbLayer = dbLayer;
    }

    public List<User> getUsers(Filters filters) {
        var selectedFields = filters == null ? UserDAO.ALL() : Arrays.stream(filters.getSelectFields().split(",")).map(String::trim).toList();
        return dbLayer.makeRequest(
                MultipleDataSourceRequest.builder()
                        .select(selectedFields.toArray(String[]::new))
                        .from(User.class)
                        .build());
    }
}
