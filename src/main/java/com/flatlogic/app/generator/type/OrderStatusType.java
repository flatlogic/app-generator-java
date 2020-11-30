package com.flatlogic.app.generator.type;

public enum OrderStatusType {

    IN_CART("in cart"), BOUGHT("bought");

    private final String status;

    OrderStatusType(String status) {
        this.status = status;
    }

    public String getStatusValue() {
        return status;
    }

    public static OrderStatusType valueOfStatus(String status) {
        for (OrderStatusType entry : values()) {
            if (entry.status.equals(status)) {
                return entry;
            }
        }
        return null;
    }

}
