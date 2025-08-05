package noisyhexagons.noise;

import midpointdisplacement.MidpointDisplacement;

import java.awt.*;

public enum EdgeType {
    STANDARD(Color.BLACK, 3.0f, Constants.STANDARD_DISPLACEMENT),
    COASTLINE(Color.BLACK, 3.0f, Constants.COASTLINE_DISPLACEMENT),
    RIVER(Color.BLUE, 7.0f, Constants.RIVER_DISPLACEMENT),
    ROAD(Color.GRAY, 4.0f, Constants.ROAD_DISPLACEMENT),
    GRID_EDGE(Color.RED, 2.0f, Constants.GRID_EDGE_DISPLACEMENT),
    OWNER_BORDER(Color.BLACK, 2.0f, Constants.OWNER_DISPLACEMENT);

    private static class Constants {
        static MidpointDisplacement STANDARD_DISPLACEMENT = new MidpointDisplacement(3, 12, 0.6);
        static MidpointDisplacement COASTLINE_DISPLACEMENT = new MidpointDisplacement(4, 28, 0.5);
        static MidpointDisplacement ROAD_DISPLACEMENT = new MidpointDisplacement(4, 8, 0.9);
        static MidpointDisplacement RIVER_DISPLACEMENT = new MidpointDisplacement(2, 22, 0.45);
        static MidpointDisplacement GRID_EDGE_DISPLACEMENT = new MidpointDisplacement(2, 10, 0.55);
        static MidpointDisplacement OWNER_DISPLACEMENT = new MidpointDisplacement(2, 10, 0.75);
    }

    private final Color color;
    private final float thickness;
    private final MidpointDisplacement midpointDisplacement;

    EdgeType(Color color, float thickness, MidpointDisplacement midpointDisplacement) {
        this.color = color;
        this.thickness = thickness;
        this.midpointDisplacement = midpointDisplacement;
    }

    public Color getColor() {
        return color;
    }

    public float getThickness() {
        return thickness;
    }

    public MidpointDisplacement getMidpointDisplacement() {
        return midpointDisplacement;
    }
}