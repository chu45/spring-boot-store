package com.codewithmosh.store.users;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserDto {
    private Long id;
    private String name;
    private String email;
    // Constructors
    public UserDto() {}

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;

    }

    // Getter methods
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Setter methods
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}