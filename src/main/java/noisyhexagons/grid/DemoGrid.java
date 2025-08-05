package noisyhexagons.grid;

import compositegrid.coordinates.HexagonCoordinate;
import compositegrid.grid.CompositeGrid;
import noisyhexagons.noise.EdgeType;

public class DemoGrid extends CompositeGrid<HexagonTile, Object, EdgeTile> {
    public DemoGrid(int hexagonWidth, int hexagonHeight) {
        super(hexagonWidth, hexagonHeight, Object.class, EdgeTile.class, HexagonTile.class);
    }

    public void placeSettlement(HexagonCoordinate coordinate){
        if (isInBounds(coordinate)){
            var cell = getHexagonCell(coordinate);
            HexagonTile tile = cell.getHexagon();
            tile.setSettlement(true);
            tile.setOwner(tile);

            for (var neighbour: cell.getNeighbours()){
                if (neighbour != null)
                    neighbour.getHexagon().setOwner(tile);
            }
        }
    }

    public void removeSettlement(HexagonCoordinate coordinate){
        if (isInBounds(coordinate)){
            var cell = getHexagonCell(coordinate);
            HexagonTile tile = cell.getHexagon();
            tile.setSettlement(false);
            tile.setOwner(null);

            for (var neighbour: cell.getNeighbours()){
                if (neighbour != null)
                    neighbour.getHexagon().setOwner(null);
            }
        }
    }

    public EdgeType getTileTransition(HexagonCoordinate leftHexagonCoordinate, HexagonCoordinate rightHexagonCoordinate){
        HexagonType leftHexagon = null;
        HexagonType rightHexagon = null;

        if (isInBounds(leftHexagonCoordinate))
            leftHexagon = getHexagonCell(leftHexagonCoordinate).getHexagon().getTileType();

        if (isInBounds(rightHexagonCoordinate))
            rightHexagon = getHexagonCell(rightHexagonCoordinate).getHexagon().getTileType();

        if (leftHexagon == rightHexagon)
            return EdgeType.STANDARD;

        if ((leftHexagon == null || rightHexagon == null))
            return EdgeType.GRID_EDGE;

        if ((leftHexagon == HexagonType.SEA || leftHexagon == HexagonType.LAND))
            return EdgeType.COASTLINE;

        return EdgeType.STANDARD;
    }

    public boolean doesHaveDifferentOwners(HexagonCoordinate leftHexagonCoordinate, HexagonCoordinate rightHexagonCoordinate) {
        if (!isInBounds(leftHexagonCoordinate) || !isInBounds(rightHexagonCoordinate))
            return true;

        HexagonTile leftHexagon = getHexagonCell(leftHexagonCoordinate).getHexagon().getOwner();
        HexagonTile rightHexagon = getHexagonCell(rightHexagonCoordinate).getHexagon().getOwner();

        return (leftHexagon != rightHexagon);
    }
}
