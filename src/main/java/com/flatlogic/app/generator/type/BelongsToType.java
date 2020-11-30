package com.flatlogic.app.generator.type;

public enum BelongsToType {

    PRODUCTS("products"), USERS("users");

    private String type;

    BelongsToType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
