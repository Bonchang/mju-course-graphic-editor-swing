package frames;

import javax.swing.JMenuBar;

import menus.GColorMenu;
import menus.GEditMenu;
import menus.GFileMenu;
import menus.GTextMenu;
import menus.GZoomMenu;

public class GMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	// 파일 메뉴 객체
	public GFileMenu fileMenu;
	// 편집 메뉴 객체
	public GEditMenu editMenu;
	// 연결된 그리기 패널
	private GDrawingPanel drawingPanel;
    // 색상 메뉴 객체
	public GColorMenu colorMenu;
	// text 메뉴 객체
	public GTextMenu textMenu;
	// zoom 메뉴 객체
	public GZoomMenu zoomMenu;
	
	// 생성자
	public GMenuBar() {
		// 파일 메뉴 생성 및 추가
		this.fileMenu = new GFileMenu("file");
		this.add(this.fileMenu);
		this.editMenu = new GEditMenu("edit");
		this.add(this.editMenu);
		this.colorMenu = new GColorMenu("color");
		this.add(this.colorMenu);
		this.textMenu = new GTextMenu("text");
		this.add(this.textMenu);
		this.zoomMenu = new GZoomMenu("zoom");
		this.add(this.zoomMenu);
	}
	
	// 그리기 패널과 연결
	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		this.fileMenu.associate(this.drawingPanel);
		this.editMenu.associate(this.drawingPanel);
		this.colorMenu.associate(this.drawingPanel);
		this.textMenu.associate(this.drawingPanel);
		this.zoomMenu.associate(this.drawingPanel);
	}
	
	// 초기화 메서드
	public void initialize() {
		
	}
}
