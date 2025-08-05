package noisyhexagons.ui;

import compositegrid.render.GridGeometry;
import midpointdisplacement.MidpointDisplacedPath;
import midpointdisplacement.MidpointDisplacement;
import noisyhexagons.render.NoisyHexagonRenderer;
import noisyhexagons.noise.EdgeType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

//quick and dirty implementation for demo purposes
public class SelectionPane extends JComponent {
    private final GridSelectionTypeModel gridSelectionTypeModel;
    private GridGeometry geometry;
    public static Shape SEA_HEXAGON;
    public static Shape LAND_HEXAGON;
    public static Shape ROAD_HEXAGON;
    public static Shape RIVER_HEXAGON;
    public static Shape SETTLEMENT_HEXAGON;
    private Shape road;
    private Shape river;

    public SelectionPane(GridSelectionTypeModel gridSelectionTypeModel){
        setBackground(Color.black);
        setOpaque(true);
        this.geometry = new GridGeometry(50);
        this.gridSelectionTypeModel = gridSelectionTypeModel;
        SEA_HEXAGON = createHexagon(60, 50);
        LAND_HEXAGON = createHexagon(180, 50);
        ROAD_HEXAGON = createHexagon(300, 50);
        RIVER_HEXAGON = createHexagon(420, 50);
        SETTLEMENT_HEXAGON = new Rectangle(500, 0, NoisyHexagonRenderer.SETTLEMENT.getWidth(), NoisyHexagonRenderer.SETTLEMENT.getHeight());
        road = createPathThroughHexagon(EdgeType.ROAD.getMidpointDisplacement(), 1, 4, 300, 50, 4f);
        river = createPathThroughHexagon(EdgeType.RIVER.getMidpointDisplacement(), 5, 2, 420, 50, 6f);
    }

    public Shape createHexagon(int x, int y){
        AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
        return transform.createTransformedShape(geometry.getHexagon());
    }

    private Shape createPathThroughHexagon(MidpointDisplacement midpointDisplacement, int vertexStart, int vertexStop, int originX, int originY, float thickness) {
        MidpointDisplacedPath path = new MidpointDisplacedPath(midpointDisplacement, MidpointDisplacedPath.COMPOSITE_BEZIER_CURVE, midpointDisplacement.hashCode());
        Point2D startPoint = geometry.getVertices()[vertexStart];
        Point2D stopPoint = geometry.getVertices()[vertexStop];
        path.moveTo(startPoint.getX() + originX, startPoint.getY() + originY);
        path.midpointDisplacedLineTo(stopPoint.getX() + originX, stopPoint.getY() + originY);
        return new BasicStroke(thickness).createStrokedShape(path);
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(6));
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.white);
        g2d.drawRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.blue);
        g2d.fill(SEA_HEXAGON);
        g2d.setColor(Color.green);
        g2d.fill(LAND_HEXAGON);
        g2d.fill(ROAD_HEXAGON);
        g2d.fill(RIVER_HEXAGON);
        g2d.setColor(Color.gray);
        g2d.fill(road);
        g2d.setColor(Color.blue);
        g2d.fill(river);
        g2d.setColor(Color.black);
        g2d.draw(SEA_HEXAGON);
        g2d.draw(LAND_HEXAGON);
        g2d.draw(ROAD_HEXAGON);
        g2d.draw(RIVER_HEXAGON);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(road);
        g2d.draw(river);
        g2d.setColor(Color.white);

        g2d.drawImage(NoisyHexagonRenderer.SETTLEMENT, null, SETTLEMENT_HEXAGON.getBounds().x, SETTLEMENT_HEXAGON.getBounds().y);

        if (gridSelectionTypeModel.getSelection() == GridSelectionTypeModel.LAND_SELECTION)
            g2d.draw(LAND_HEXAGON);
        else if (gridSelectionTypeModel.getSelection() == GridSelectionTypeModel.SEA_SELECTION)
            g2d.draw(SEA_HEXAGON);
        else if (gridSelectionTypeModel.getSelection() == GridSelectionTypeModel.ROAD_SELECTION)
            g2d.draw(ROAD_HEXAGON);
        else if (gridSelectionTypeModel.getSelection() == GridSelectionTypeModel.RIVER_SELECTION)
            g2d.draw(RIVER_HEXAGON);
        else if (gridSelectionTypeModel.getSelection() == GridSelectionTypeModel.SETTLEMENT_SELECTION)
            g2d.draw(SETTLEMENT_HEXAGON);
    }
}
