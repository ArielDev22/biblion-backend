package com.projeto_integrado_biblioteca.user;

import lombok.Getter;

@Getter
public enum UserRole {
    COMMON("common"),
    ADMIN("admin");

    private String name;

    UserRole(String name) {
        this.name = name;
    }
}
