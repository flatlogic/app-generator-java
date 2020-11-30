package com.flatlogic.app.generator.type;

public enum ProductStatusType {

    IN_STOCK("in stock"), OUT_OF_STOCK("out of stock");

    private String status;

    ProductStatusType(String status) {
        this.status = status;
    }

    public String getStatusValue() {
        return status;
    }

    public static ProductStatusType valueOfStatus(String status) {
        for (ProductStatusType entry : values()) {
            if (entry.status.equals(status)) {
                return entry;
            }
        }
        return null;
    }

}
