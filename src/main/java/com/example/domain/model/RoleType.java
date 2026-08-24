package com.example.domain.model;

public enum RoleType {

    WEB("web"),
    REST("rest");

    private final String roleName;

    RoleType(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
