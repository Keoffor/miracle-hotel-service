package com.miracle.keoffor.constants;

public enum JwtCredConstant {
    AUTHORIZATION("Authorization"),
    SECRET("E474FC584B265708C034298D997D35D996BC07717059C4ACB2C5DB675094812A"),
    BEARER("Bearer ");
    private final String jwtCred;
    JwtCredConstant(String jwtCred) {
        this.jwtCred = jwtCred;
    }

    public String getJwtCred() {
        return jwtCred;
    }
}
