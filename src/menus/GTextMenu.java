package menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import frames.GDrawingPanel;
import shapeTools.GText;

public class GTextMenu extends JMenu {
    private static final long serialVersionUID = 1L;
    private GDrawingPanel drawingPanel;

    public GTextMenu(String text) {
        super(text);
        initializeMenu();
    }

    private void initializeMenu() {
        JMenuItem addTextItem = new JMenuItem("Add Text");
        addTextItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addText();
            }
        });
        this.add(addTextItem);
        
        JMenuItem addDateTimeItem = new JMenuItem("Add Date & Time");
        addDateTimeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDateTime();
            }
        });
        this.add(addDateTimeItem);
    }

    private void addText() {
        String text = JOptionPane.showInputDialog(this, "Enter the text:");
        if (text != null && drawingPanel != null) {
            GText gText = new GText();
            gText.setText(text);
            drawingPanel.setShapeTool(gText);
        }
    }

    private void addDateTime() {
        if (drawingPanel != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = formatter.format(new Date());
            GText gText = new GText();
            gText.setText(dateTime);
            drawingPanel.setShapeTool(gText);
        }
    }

    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
}
