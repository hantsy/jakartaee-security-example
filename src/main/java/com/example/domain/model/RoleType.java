package com.example.domain.model;

public enum RoleType {

    USER("user"),
    ADMIN("admin");

    private final String roleName;

    RoleType(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
