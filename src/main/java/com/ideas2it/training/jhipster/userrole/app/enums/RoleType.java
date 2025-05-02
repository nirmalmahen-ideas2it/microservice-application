package com.ideas2it.training.jhipster.userrole.app.enums;

public enum RoleType {
    ADMIN,
    USER,
    MAINTAINER;

    public String toValue() {
        return this.name();
    }
}
