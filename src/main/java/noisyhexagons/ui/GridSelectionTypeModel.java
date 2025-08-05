package noisyhexagons.ui;

public class GridSelectionTypeModel {
    public static final int NO_SELECTION = 0;
    public static final int LAND_SELECTION = 1;
    public static final int SEA_SELECTION = 2;
    public static final int ROAD_SELECTION = 3;
    public static final int RIVER_SELECTION = 4;
    public static final int SETTLEMENT_SELECTION = 5;
    private int selection = NO_SELECTION;

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public int getSelection(){
        return selection;
    }
}
