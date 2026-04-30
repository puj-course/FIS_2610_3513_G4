package com.ceiba.fashtoll.utilities.enums;

public enum Role {
    CLIENT,
    BRAND,
    ADMIN;

    public static Role categorize(String role) {
        switch (role) {
            case "ADMIN":
                return ADMIN;
            case "BRAND":
                return BRAND;
            case "CLIENT":
                return CLIENT;
        }

        return CLIENT;
    }
}
