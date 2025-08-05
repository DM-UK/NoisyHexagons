package noisyhexagons;

import compositegrid.ui.HexagonGridPane;

import noisyhexagons.grid.DemoController;
import noisyhexagons.grid.DemoGrid;
import noisyhexagons.render.NoisyHexagonRenderer;
import noisyhexagons.ui.GridSelectionTypeModel;
import noisyhexagons.ui.SelectionPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NoisyHexagonApp {
    private JFrame frame = new JFrame("Demo");
    private DemoGrid demoGrid = new DemoGrid(50, 50);
    private NoisyHexagonRenderer renderer = new NoisyHexagonRenderer(demoGrid, 0, 0, 90, 0, 12);
    private HexagonGridPane gridPane = new HexagonGridPane(1200, 800, demoGrid, renderer);
    private GridSelectionTypeModel gridSelectionTypeModel = new GridSelectionTypeModel();
    private SelectionPane selectionPane = new SelectionPane(gridSelectionTypeModel);
    private GridSelectionMouseAdapter gridSelectionMouseAdapter = new GridSelectionMouseAdapter();
    private DemoController demoController = new DemoController(demoGrid, gridSelectionTypeModel);

    public NoisyHexagonApp(){
        buildGUI();
        selectionPane.addMouseListener(gridSelectionMouseAdapter);
        gridPane.addGridSelectionListener(demoController);
    }

    private void buildGUI() {
        JPanel panel = new JPanel(new BorderLayout());
        gridPane.add(selectionPane);
        selectionPane.setPreferredSize(new Dimension(1200, 100));
        panel.add(gridPane, BorderLayout.CENTER);
        panel.add(selectionPane, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(panel);
        frame.pack();
    }

    //todo move this elsewhere...
    public class GridSelectionMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            checkForPanelClick(e.getPoint());
            selectionPane.repaint();
        }

        public boolean checkForPanelClick(Point p){
            if (selectionPane.LAND_HEXAGON.contains(p))
                gridSelectionTypeModel.setSelection(GridSelectionTypeModel.LAND_SELECTION);
            else if (selectionPane.SEA_HEXAGON.contains(p))
                gridSelectionTypeModel.setSelection(GridSelectionTypeModel.SEA_SELECTION);
            else if (selectionPane.ROAD_HEXAGON.contains(p))
                gridSelectionTypeModel.setSelection(GridSelectionTypeModel.ROAD_SELECTION);
            else if (selectionPane.RIVER_HEXAGON.contains(p))
                gridSelectionTypeModel.setSelection(GridSelectionTypeModel.RIVER_SELECTION);
            else if (selectionPane.SETTLEMENT_HEXAGON.contains(p))
                gridSelectionTypeModel.setSelection(GridSelectionTypeModel.SETTLEMENT_SELECTION);
            else
                return false;

            return true;
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NoisyHexagonApp();
            }
        });
    }
}
