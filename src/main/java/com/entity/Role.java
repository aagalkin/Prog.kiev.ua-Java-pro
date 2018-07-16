package com.entity;

public enum Role {
    ADMIN, USER, GUEST;


    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
