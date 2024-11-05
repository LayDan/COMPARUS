package org.multiple.datasource.controller;


import org.multiple.datasource.entity.User;
import org.multiple.datasource.service.UserService;
import org.openapitools.api.UsersApi;
import org.openapitools.model.Filters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController implements UsersApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Override
    public ResponseEntity<List<org.openapitools.model.User>> getUsers(Filters filters) {
        var users = userService.getUsers(filters);
        return ResponseEntity.ok(map(users));
    }

    private List<org.openapitools.model.User> map(List<User> users) {
        return users.stream().map(user -> {
            var u = new org.openapitools.model.User();
            u.setId(user.getId());
            u.setName(user.getName());
            u.setSurname(user.getSurname());
            u.setUsername(user.getUsername());
            return u;
        }).collect(Collectors.toList());
    }
}
