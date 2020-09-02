package com.tsel.neiro.data;

public enum WheelColor {
    BLACK(0),
    RED(1),
    BLUE(2),
    GOLD(3);

    private final int number;

    WheelColor(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static WheelColor getColor(Integer number) {
        switch (number) {
            case 0:
                return BLACK;
            case 1:
                return RED;
            case 2:
                return BLUE;
            case 3:
                return GOLD;
            default:
                return null;
        }
    }
}
