package noisyhexagons.grid;

import java.awt.*;

public enum HexagonType {
    LAND(Color.GREEN),
    SEA(Color.BLUE);

    private final Color color;

    HexagonType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
