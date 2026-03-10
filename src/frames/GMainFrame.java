package frames;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import javax.swing.JFrame;
import global.Constants;
import menus.GColorMenu;
import menus.GEditMenu;
import menus.GTextMenu;

public class GMainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // 컴포넌트들
    private GMenuBar menuBar;
    private GShapeToolBar shapeToolBar;    
    private GDrawingPanel drawingPanel;
    private GEditMenu editMenu;
    private GColorMenu colorMenu;
    private GTextMenu textMenu;
    
    // 생성자
    public GMainFrame() {
        // 속성 설정
        this.setSize(Constants.GMainFrame.WIDTH, Constants.GMainFrame.HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 컴포넌트 생성
        LayoutManager layoutManager = new BorderLayout();
        this.setLayout(layoutManager);
    
        this.menuBar = new GMenuBar();
        this.setJMenuBar(this.menuBar);        

        this.shapeToolBar = new GShapeToolBar();
        this.add(shapeToolBar, BorderLayout.NORTH);
        
        this.drawingPanel = new GDrawingPanel();
        this.add(drawingPanel, BorderLayout.CENTER);
        
        // 컴포넌트들 연결
        this.menuBar.associate(this.drawingPanel);
        this.shapeToolBar.associate(this.drawingPanel);
    }
    
    // 메서드들
    public void initialize() {
        this.menuBar.initialize();
        this.shapeToolBar.intitialize();
        this.drawingPanel.intitialize();        
    }
}
