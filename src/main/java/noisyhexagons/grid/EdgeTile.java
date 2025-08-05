package noisyhexagons.grid;

public class EdgeTile {
    private boolean road;
    private boolean river;

    public void setRoad(boolean road) {
        this.road = road;
    }

    public void setRiver(boolean river) {
        this.river = river;
    }

    public boolean hasRoad() {
        return road;
    }

    public boolean hasRiver() {
        return river;
    }
}
