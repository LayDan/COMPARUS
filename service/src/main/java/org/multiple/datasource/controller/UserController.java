package org.multiple.datasource.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.multiple.datasource.entity.User;
import org.multiple.datasource.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            operationId = "getUsers",
            summary = "Get all users",
            description = "Return all users from different DBs",
            tags = {"user-controller"}
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/users",
            produces = {"application/json"}
    )
    public ResponseEntity<List<User>> getUsers(Filters filters) {
        var users = userService.getUsers(filters);
        return ResponseEntity.ok(users);
    }


}
