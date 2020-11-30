package com.flatlogic.app.generator.type;

public enum BelongsToColumnType {

    AVATAR("avatar"), IMAGE("image");

    private String type;

    BelongsToColumnType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
