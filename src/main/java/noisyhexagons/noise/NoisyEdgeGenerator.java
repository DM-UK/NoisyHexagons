package noisyhexagons.noise;

import midpointdisplacement.MidpointDisplacedPath;
import midpointdisplacement.MidpointDisplacement;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class NoisyEdgeGenerator {
    /** Create a hexagon with a random noise along each edge.
     * Array index logic: starts at [0] west vertex and follows clockwise to finish at south-west vertex [5]
     * @param hexagonCentre The hexagon centre point
     * @param vertices The six vertices from the origin
     * @param seeds The six seed value for generating noise along each edge
     * @param displacements The six MidpointDisplacement parameters for each edge
     */
    public static Path2D hexagon(Point2D hexagonCentre, Point2D[] vertices, int[] seeds, MidpointDisplacement[] displacements) {
        //Since hexagons share edges (a NW edge on one hexagon is a SW edge on another) this needs to be factored for
        //The bottom edges (SW,W,NW) are drawn in reverse order to maintain continuity
        //The only way i've found to ensure a 'closed' hexagon for graphics fill() purposes is to:
        //'stitch' the top and bottom hexagon together by drawing in the vertice order: 0,1,2,3 and then 0,5,4,3
        Path2D hexagon = new Path2D.Double();
        hexagon.moveTo(
                hexagonCentre.getX() + vertices[0].getX(),
                hexagonCentre.getY() + vertices[0].getY()
        );

        for (int i = 0; i < 3; i++){
            Point2D lineTo = new Point2D.Double(
                    hexagonCentre.getX() + vertices[i + 1].getX(),
                    hexagonCentre.getY() + vertices[i + 1].getY()
            );

            Path2D edge = edge(hexagon.getCurrentPoint(), lineTo, seeds[i], displacements[i]);
            hexagon.append(edge, true);
        }

        hexagon.moveTo(
                hexagonCentre.getX() + vertices[0].getX(),
                hexagonCentre.getY() + vertices[0].getY()
        );

        //bottom half
        for (int i = 5; i >= 3; i--){
            Point2D lineTo = new Point2D.Double(
                    hexagonCentre.getX() + vertices[i].getX(),
                    hexagonCentre.getY() + vertices[i].getY()
            );

            Path2D edge = edge(hexagon.getCurrentPoint(), lineTo, seeds[i], displacements[i]);
            hexagon.append(edge, true);
        }

        return hexagon;
    }

    public static Path2D edge(Point2D lineFrom, Point2D lineTo, int seed, MidpointDisplacement displacement) {
        MidpointDisplacedPath path = new MidpointDisplacedPath(displacement, MidpointDisplacedPath.COMPOSITE_BEZIER_CURVE, seed);
        path.moveTo(lineFrom.getX(), lineFrom.getY());
        path.midpointDisplacedLineTo(lineTo.getX(), lineTo.getY());
        return path;
    }
}
