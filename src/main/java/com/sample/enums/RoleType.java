package com.sample.enums;

public enum RoleType {
    ADMIN,
    USER,
    MAINTAINER;

    public String toValue() {
        return this.name();
    }
}
