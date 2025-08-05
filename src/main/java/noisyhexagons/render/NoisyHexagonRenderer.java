package noisyhexagons.render;

import compositegrid.coordinates.*;
import compositegrid.grid.HexagonCell;
import compositegrid.grid.TriangleCell;
import compositegrid.render.AbstractGridRenderer;
import compositegrid.render.GridOverlayRenderer;
import midpointdisplacement.MidpointDisplacement;
import noisyhexagons.grid.DemoGrid;
import noisyhexagons.grid.EdgeTile;
import noisyhexagons.grid.HexagonTile;
import noisyhexagons.grid.HexagonType;
import noisyhexagons.noise.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class NoisyHexagonRenderer extends AbstractGridRenderer<HexagonTile, Object, EdgeTile> {
    private final DemoGrid demoGrid;
    private float[] dashPattern = {12, 12};
    private Stroke dashedStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashPattern, 0);
    public final static BufferedImage SETTLEMENT = ImageUtils.loadImage("src/main/resources/settlement.png");

    public NoisyHexagonRenderer(DemoGrid hexagonGrid, int canvasWidthPixels, int canvasHeightPixels, double edgeLength, int vertexDiameter, double edgeThickness) {
        super(hexagonGrid, canvasWidthPixels, canvasHeightPixels, edgeLength, vertexDiameter, edgeThickness);
        this.demoGrid = hexagonGrid;
    }

    @Override
    protected void drawHexagon(Graphics2D g2d, HexagonCoordinate coordinate, HexagonCell<HexagonTile, Object, EdgeTile> cell, Shape hexagonShape, Point2D vertexPosition) {
        if (cell != null) {
            Color colour = cell.getHexagon().getTileType().getColor();
            HexagonTile owner = cell.getHexagon().getOwner();
            if (owner == null && cell.getHexagon().getTileType() == HexagonType.LAND)
                colour = ImageUtils.darkern(colour, 0.85);

            g2d.setColor(colour);
            g2d.fill(hexagonShape);
        }
    }

    @Override
    protected void drawEdge(Graphics2D g2d, TriangleCoordinate coordinate, EdgeTile edge, TriangleDirection direction, Point2D lineStart, Point2D lineFinish) {
        if (edge != null && edge.hasRiver()){
            Shape river = createNoisyEdgeShape(coordinate, direction, EdgeType.RIVER);
            g2d.setColor(EdgeType.RIVER.getColor());
            g2d.fill(river);
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(river);
        }

        if (edge != null && edge.hasRoad()){
            Shape road = createNoisyEdgeShape(coordinate, direction, EdgeType.ROAD);
            g2d.setColor(EdgeType.ROAD.getColor());
            g2d.fill(road);
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(road);
        }
    }

    @Override
    protected void drawHexagonBorderEdge(Graphics2D g2d, TriangleCoordinate coordinate, EdgeTile edge, TriangleDirection direction, Point2D lineStart, Point2D lineFinish) {
        HexagonCoordinate[] adjacents = CoordinateUtils.getAdjacentHexagonsOfEdge(coordinate, direction);
        EdgeType edgeType = demoGrid.getTileTransition(adjacents[0], adjacents[1]);

        if (edgeType != EdgeType.STANDARD) {
            Shape terrainTransisition = createNoisyEdgeShape(coordinate, direction, edgeType);
            g2d.setColor(edgeType.getColor());
            g2d.fill(terrainTransisition);
        }

        if (edge != null){
            //Shape noisyEdge = createNoisyPath(coordinate, direction, EdgeType.STANDARD);
            //g2d.setColor(Color.black);
            //g2d.setStroke(new BasicStroke(2));
            //g2d.draw(noisyEdge);

            if (demoGrid.doesHaveDifferentOwners(adjacents[0], adjacents[1])){
                g2d.setStroke(dashedStroke);
                g2d.setColor(Color.black);
                //Shape borderTransisition = createNoisyPath(coordinate, direction, edgeType);
                Shape borderTransisition = createNoisyPath(coordinate, direction, EdgeType.OWNER_BORDER);
                g2d.setColor(Color.BLACK);
                g2d.draw(borderTransisition);
            }
        }
    }

    @Override
    protected void drawVertices(Graphics2D g2d, TriangleCoordinate coordinate, TriangleCell<Object, EdgeTile> cell, Point2D vertexPosition) {
    }

    @Override
    protected void drawHexagonCentreVertice(Graphics2D g2d, TriangleCoordinate coordinate, TriangleCell<Object, EdgeTile> cell, Point2D vertexPosition) {
        if (demoGrid.isInBounds(coordinate.convert()))
            if (demoGrid.getHexagonCell(coordinate.convert()).getHexagon().hasSettlement())
                g2d.drawImage(SETTLEMENT, null, (int)vertexPosition.getX()-SETTLEMENT.getWidth()/2, (int)vertexPosition.getY()-SETTLEMENT.getHeight()/2);
    }


    /** Create a hexagon with a random noise along each edge based on the tile transition with its hexagon neighbour. */
    @Override
    public Shape constructHexagon(HexagonCoordinate coordinate) {
        Point2D hexagonCentre = getCameraAdjustedScreenPosition(coordinate.convert().toFractional());

        int[] seeds = new int[6];
        MidpointDisplacement[] transitions = new MidpointDisplacement[6];
        TriangleCoordinate[] neighbours = coordinate.convert().neighbours();
        TriangleDirection direction = TriangleDirection.NORTHEAST;

        //index the 6 edge transitions and their seeds for NoisyEdgeGenerator
        //start at hexagon vertice 0 and follow the border clockwise (NORTHEAST/EAST etc..)
        for (int i=0; i < 6; i++){
            transitions[i] = demoGrid.getTileTransition(coordinate, coordinate.neighbours()[i]).getMidpointDisplacement();
            seeds[i] = neighbours[i].hashCode(direction);
            direction = direction.rotate(1);
        }

        return NoisyEdgeGenerator.hexagon(hexagonCentre, geometry.getVertices(), seeds, transitions);
    }

        /** Create a 2D noisy edge shape from an EdgeType's MidpointDisplacement and it's thickness  */
    private Shape createNoisyEdgeShape(TriangleCoordinate coordinate, TriangleDirection direction, EdgeType edgeType) {
        Path2D edge = createNoisyPath(coordinate, direction, edgeType);
        return new Area(new BasicStroke((float) edgeType.getThickness()).createStrokedShape(edge));
    }

    /** Create a 1D noisy edge path from an EdgeType's MidpointDisplacement */
    private Path2D createNoisyPath(TriangleCoordinate coordinate, TriangleDirection direction, EdgeType edgeType) {
        Point2D lineStart = getCameraAdjustedScreenPosition(coordinate.toFractional());
        Point2D lineFinish = getCameraAdjustedScreenPosition(coordinate.direction(direction).toFractional());
        int seed = coordinate.hashCode(direction);

        Path2D edge;

        //Since hexagons share edges (a NW edge on one hexagon is a SW edge on another) this needs to be factored for
        //The bottom edges (SW,W,NW) are drawn in reverse order to maintain continuity
        if (direction == TriangleDirection.SOUTHWEST || direction == TriangleDirection.WEST || direction == TriangleDirection.NORTHWEST)
            edge = NoisyEdgeGenerator.edge(lineFinish, lineStart, seed, edgeType.getMidpointDisplacement());
        else
            edge = NoisyEdgeGenerator.edge(lineStart, lineFinish, seed, edgeType.getMidpointDisplacement());

        return edge;
    }
}
