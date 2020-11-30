package com.flatlogic.app.generator.type;

public enum RoleType {

    USER("user"), ADMIN("admin");

    private String role;

    RoleType(String role) {
        this.role = role;
    }

    public String getRoleValue() {
        return role;
    }

    public static RoleType valueOfRole(String role) {
        for (RoleType entry : values()) {
            if (entry.role.equals(role)) {
                return entry;
            }
        }
        return null;
    }
}
