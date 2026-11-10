package com.p3319.lab2.imports;

import lombok.Getter;

@Getter
public enum ImportResourceType {
    PRODUCTS("products"),
    ORGANIZATIONS("organizations"),
    PERSONS("persons");

    private final String value;

    ImportResourceType(String value) {
        this.value = value;
    }

    public static ImportResourceType fromValue(String value) {
        for (ImportResourceType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
