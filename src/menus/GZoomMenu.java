package menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import frames.GDrawingPanel;

public class GZoomMenu extends JMenu {
    private static final long serialVersionUID = 1L;
    private GDrawingPanel drawingPanel;

    public GZoomMenu(String text) {
        super(text);
        initializeMenu();
    }

    private void initializeMenu() {
        JMenuItem zoomInItem = new JMenuItem("Zoom In");
        zoomInItem.addActionListener(e -> {
            if (drawingPanel != null) {
                drawingPanel.zoomIn();
            }
        });

        JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
        zoomOutItem.addActionListener(e -> {
            if (drawingPanel != null) {
                drawingPanel.zoomOut();
            }
        });

        JMenuItem resetZoomItem = new JMenuItem("Reset Zoom");
        resetZoomItem.addActionListener(e -> {
            if (drawingPanel != null) {
                drawingPanel.resetZoom();
            }
        });

        this.add(zoomInItem);
        this.add(zoomOutItem);
        this.add(resetZoomItem);
    }

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
}
