package org.multiple.datasource.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.multiple.datasource.processing.Builder;
import org.multiple.datasource.processing.DataSourceProperty;

import java.io.Serializable;


@Builder
public class User implements Serializable {
    @DataSourceProperty("id")
    private String id;
    @DataSourceProperty("username")
    private String username;
    @DataSourceProperty("name")
    private String name;
    @DataSourceProperty("surname")
    private String surname;

    public User(String id, String username, String name, String surname) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
