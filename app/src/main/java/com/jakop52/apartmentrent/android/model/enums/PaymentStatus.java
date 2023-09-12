package com.jakop52.apartmentrent.android.model.enums;

public enum PaymentStatus {
    NOT_PAID,
    PENDING,
    PAID,
    FAILED;

    public static String[] toStringArray() {
        PaymentStatus[] values = PaymentStatus.values();
        String[] stringArray = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            stringArray[i] = values[i].toString();
        }
        return stringArray;
    }
}

