package noisyhexagons.grid;

import compositegrid.coordinates.FractionalTriangleCoordinate;
import compositegrid.coordinates.HexagonCoordinate;
import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;
import compositegrid.ui.GridSelectionListener;
import noisyhexagons.ui.GridSelectionTypeModel;

public class DemoController implements GridSelectionListener {
    private final DemoGrid grid;
    private final GridSelectionTypeModel gridSelectionTypeModel;

    public DemoController(DemoGrid grid, GridSelectionTypeModel gridSelectionTypeModel){
        this.gridSelectionTypeModel = gridSelectionTypeModel;
        this.grid = grid;
    }

    @Override
    public void vertexSelected(TriangleCoordinate coordinate) {

    }

    @Override
    public void edgeSelected(TriangleCoordinate coordinate, TriangleDirection direction) {
        if (grid.isEdgeInBounds(coordinate, direction)){
            EdgeTile edge = grid.getTriangleCell(coordinate).getEdges()[direction.ordinal()];

            if (gridSelectionTypeModel.getSelection() == GridSelectionTypeModel.RIVER_SELECTION){
                if (edge.hasRiver())
                    edge.setRiver(false);
                else
                    edge.setRiver(true);
            }

            if (gridSelectionTypeModel.getSelection() == GridSelectionTypeModel.ROAD_SELECTION){
                if (edge.hasRoad())
                    edge.setRoad(false);
                else
                    edge.setRoad(true);
            }
        }
    }

    @Override
    public void hexagonSelected(HexagonCoordinate coordinate) {
        if (grid.isInBounds(coordinate)){
            HexagonTile hexagon = grid.getHexagonCell(coordinate).getHexagon();

            if (gridSelectionTypeModel.getSelection() == GridSelectionTypeModel.LAND_SELECTION)
                hexagon.setTileType(HexagonType.LAND);

            if (gridSelectionTypeModel.getSelection() == GridSelectionTypeModel.SEA_SELECTION)
                hexagon.setTileType(HexagonType.SEA);

            if (gridSelectionTypeModel.getSelection() == GridSelectionTypeModel.SETTLEMENT_SELECTION){
                if (hexagon.hasSettlement())
                    grid.removeSettlement(coordinate);
                else
                    grid.placeSettlement(coordinate);
            }

        }
    }

    @Override
    public void gridSelected(FractionalTriangleCoordinate coordinate) {
    }
}
