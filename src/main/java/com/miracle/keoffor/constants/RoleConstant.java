package com.miracle.keoffor.constants;

public enum RoleConstant {
    ROLE("ROLE_"),
    USER("USER"),
    ADMIN("ADMIN");
    private final String roleName;
    RoleConstant(String roleName) {

        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
