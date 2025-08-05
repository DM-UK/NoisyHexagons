A demonstration of how to achieve visually appealing hexagon border transitions of a [CompositeHexagonGrid](https://github.com/DM-UK/HexTriCompositeGrid) using the [MidpointDisplacement](https://github.com/DM-UK/MidpointDisplacement) algorithm.

Drawing a line along each edge of a regular hexagon using the Midpoint Displacement algorithm we get the following effect:
![](/src/main/resources/image20250805111428.png)
![](/src/main/resources/image20250805111441.png)

### Tile transitions

By assigning each hexagon tile a binary state and only drawing an edge if the edges neighbouring tiles differ we get this (note: different midpoint displacement parameters):

![](/src/main/resources/image20250805110154.png)

### Road / River (Triangle Grid)

Road and river effects by drawing a noisy line with a thickness, between each vertex of the triangle grid.

![](/src/main/resources/image20250805104619.png)

### Demo

A basic scrollable map editor is included (NoisyHexagonApp). Each different use (border/river/road) of the noisy edge algorithm uses separate parameters allowing for more versatile visuals.

To achieve consistency through each render call the noisy edge algorithm uses a hash calculated by its vertex coordinate and edge direction, for its seed.

As with CompositeHexagonGrid there is pixel perfect mouse click support.

Note: No attempt at making edge/hexagon shape creation CPU efficient has been made and every edge is created on each render call. A simple solution would be to implement a Least Recently Used Cache with each edges seed as a key.

![](/src/main/resources/image20250805104658.png)

![](/src/main/resources/image20250805104849.png)
