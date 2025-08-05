package noisyhexagons.grid;

public class HexagonTile {
    private HexagonType hexagonType;
    private boolean settlement;
    private HexagonTile owner;

    public HexagonTile() {
        hexagonType = HexagonType.LAND;
    }

    public void setTileType(HexagonType hexagonType){
        this.hexagonType = hexagonType;
    }

    public HexagonType getTileType(){
        return hexagonType;
    }

    public void setSettlement(boolean settlement) {
        this.settlement = settlement;
    }

    public void setOwner(HexagonTile owner) {
        this.owner = owner;
    }

    public HexagonTile getOwner() {
        return owner;
    }

    public boolean hasSettlement() {
        return settlement;
    }
}
