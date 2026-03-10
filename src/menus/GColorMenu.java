package menus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import frames.GDrawingPanel;

public class GColorMenu extends JMenu {
    private static final long serialVersionUID = 1L;

    // 그리기 패널과 연결
    private GDrawingPanel drawingPanel;
    
    // 생성자
    public GColorMenu(String text) {
        super(text);
        initializeMenu();
    }
    
    // 메뉴 초기화
    private void initializeMenu() {
        JMenuItem colorChooserItem = new JMenuItem("Line Color");
        colorChooserItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseColor();
            }
        });
        this.add(colorChooserItem);
        
        JMenuItem fillColorChooserItem = new JMenuItem("Fill Color");
        fillColorChooserItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFillColor();
            }
        });
        this.add(fillColorChooserItem);

        JMenuItem backgroundColorChooserItem = new JMenuItem("Background Color");
        backgroundColorChooserItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseBackgroundColor();
            }
        });
        this.add(backgroundColorChooserItem);
    }

    // 선 색상 선택
    private void chooseColor() {
        SwingUtilities.invokeLater(() -> {
            if (this.isShowing()) {
                Color selectedColor = JColorChooser.showDialog(this, "Line Color", Color.BLACK);
                if (selectedColor != null && drawingPanel != null) {
                    drawingPanel.setCurrentColor(selectedColor);
                }
            } else {
                System.out.println("Component is not showing on the screen.");
            }
        });
    }

    // 채우기 색상 선택
    private void chooseFillColor() {
        SwingUtilities.invokeLater(() -> {
            if (this.isShowing()) {
                Color selectedFillColor = JColorChooser.showDialog(this, "Fill Color", Color.WHITE);
                if (selectedFillColor != null && drawingPanel != null) {
                    drawingPanel.setCurrentFillColor(selectedFillColor);
                }
            } else {
                System.out.println("Component is not showing on the screen.");
            }
        });
    }

    // 배경 색상 선택
    private void chooseBackgroundColor() {
        SwingUtilities.invokeLater(() -> {
            if (this.isShowing()) {
                Color selectedBackgroundColor = JColorChooser.showDialog(this, "Background Color", Color.WHITE);
                if (selectedBackgroundColor != null && drawingPanel != null) {
                    drawingPanel.setBackgroundColor(selectedBackgroundColor);
                }
            } else {
                System.out.println("Component is not showing on the screen.");
            }
        });
    }

    // 그리기 패널과 연결
    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
}
