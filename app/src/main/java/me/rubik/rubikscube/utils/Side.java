package me.rubik.rubikscube.utils;

public enum Side {
    up(1, -1, new int[] {255, 255, 255}),
    right(2, -43008, new int[] {255, 88, 0}),
    down(3, -16737464, new int[] {0, 155, 72}),
    back(4, -4779468, new int[] {183, 18, 52}),
    left(5, -11008, new int[] {255, 88, 0}),
    front(6, -16759123, new int[] {0, 155, 72});

    private final int integerValue;
    private final int color;
    private final int[] rgbColor;

    Side(int integerValue, int color, int[] rgbColor) {
        this.integerValue = integerValue;
        this.color = color;
        this.rgbColor = rgbColor;
    }

    public int integerValue() {
        return integerValue;
    }
    public int color() {
        return color;
    }
    public int[] rgbColor() {
        return rgbColor;
    }

    public static Side colorToSide(int color) {
        for (Side side : Side.values()) {
            if (side.color == color) {
                return side;
            }
        }
        return null;
    }
    public static Side intToSide(int integer) {
        for (Side side : Side.values()) {
            if (side.integerValue == integer) {
                return side;
            }
        }
        return null;
    }

}
