package menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import frames.GDrawingPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GEditMenu extends JMenu {
    private static final long serialVersionUID = 1L;
    
    private GDrawingPanel drawingPanel;
    
    public GEditMenu(String text) {
        super(text);
        
        // Cut
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.cut();
            }
        });
        this.add(cutItem);

        // Copy
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.copy();
            }
        });
        this.add(copyItem);

        // Paste
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.paste();
            }
        });
        this.add(pasteItem);

        // Delete
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.delete();
            }
        });
        this.add(deleteItem);

        // Bring to Front
        JMenuItem bringToFrontItem = new JMenuItem("Bring to Front");
        bringToFrontItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.bringToFront();
            }
        });
        this.add(bringToFrontItem);

        // Send to Back
        JMenuItem sendToBackItem = new JMenuItem("Send to Back");
        sendToBackItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.sendToBack();
            }
        });
        this.add(sendToBackItem);
        
        // 회전 기능 추가
        JMenuItem rotateLeftItem = new JMenuItem("Rotate Left");
        rotateLeftItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.rotateCurrentShape(-90);
            }
        });
        this.add(rotateLeftItem);

        JMenuItem rotateRightItem = new JMenuItem("Rotate Right");
        rotateRightItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.rotateCurrentShape(90);
            }
        });
        this.add(rotateRightItem);
    }
    
    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
}
