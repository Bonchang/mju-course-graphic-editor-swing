package frames;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import global.Constants.EShapeButtons;

public class GShapeToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;
	// 연결된 그리기 패널
	private GDrawingPanel drawingPanel;
	
	// 생성자
	public GShapeToolBar() {
		// 버튼 그룹 생성
		ButtonGroup buttonGroup = new ButtonGroup();
		// 액션 핸들러 생성
		ShapeActionHandler shapeActionHandler = new ShapeActionHandler();
		
		// EShapeButtons enum을 사용하여 라디오 버튼 생성 및 설정
		for (EShapeButtons eShapeButtons: EShapeButtons.values()) {
			JRadioButton button = new JRadioButton(eShapeButtons.getText());
			button.addActionListener(shapeActionHandler);
			button.setActionCommand(eShapeButtons.toString());
			// 이미지 아이콘 추가 및 크기 조정
            URL imageUrl = getClass().getResource("/resource/button.png");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image image = icon.getImage();
                Image newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); // 크기를 20x20으로 조정
                icon = new ImageIcon(newimg);
                button.setIcon(icon);
            }
			add(button);
			buttonGroup.add(button);
		}
	}

	// 초기화 메서드
	public void intitialize() {
		// 기본 버튼 클릭
		JRadioButton defaultButton 
			= (JRadioButton)(this.getComponent(EShapeButtons.eNone.ordinal()));
		defaultButton.doClick();
	}

	// 그리기 패널과 연결
	public void associate(GDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;		
	}
	
	// 도형 도구 설정
	private void setShapeTool(EShapeButtons eShapeButton) {
		this.drawingPanel.setShapeTool(eShapeButton.getShapeTool());		
	}
	
	// 도형 액션 핸들러 클래스
	private class ShapeActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			EShapeButtons eShapeButton = EShapeButtons.valueOf(e.getActionCommand());
			setShapeTool(eShapeButton);
		}
	}
}
