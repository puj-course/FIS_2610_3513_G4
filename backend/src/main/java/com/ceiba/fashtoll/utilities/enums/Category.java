package com.ceiba.fashtoll.utilities.enums;

public enum Category {
    TOPS,
    BOTTOMS,
    OUTERWEAR,
    FULL_BODY;

    public static Category categorize(String category) {
        switch(category) {
            case "FULL_BODY":
                return FULL_BODY;
            case "OUTERWEAR":
                return OUTERWEAR;
            case "BOTTOMS":
                return BOTTOMS;
            case "TOPS":
                return TOPS;
        }
        //lo va a devolver por default
        return TOPS;
    }
}
