package global;

import shapeTools.GHeart;
import shapeTools.GLine;
import shapeTools.GOval;
import shapeTools.GPencil;
import shapeTools.GPolygon;
import shapeTools.GRectangle;
import shapeTools.GShape;

public class Constants {
	// 도형 버튼들을 정의한 열거형
	public enum EShapeButtons {
		eNone("none", null),
		eRectangle("rectangle", new GRectangle()),
		eOval("oval", new GOval()), 
		eLine("line", new GLine()),
		ePolygon("polygon", new GPolygon()),
		ePencil("Heart", new GHeart());
		
		// 각 버튼의 텍스트와 도형 도구를 저장
		private String text;
		private GShape shapeTool;
		
		// 생성자
		private EShapeButtons(String text, GShape shapeTool) {
			this.text = text;
			this.shapeTool = shapeTool;
		}
		
		// 텍스트를 반환하는 메서드
		public String getText() { return this.text; }
		
		// 도형 도구를 반환하는 메서드
		public GShape getShapeTool() { return this.shapeTool; }
	}
	
	// 상수 정의
	public final static int NUM_POINTS = 20; 
	
	// 메인 프레임 관련 상수 정의
	public static class GMainFrame {
		public final static int WIDTH = 500;
		public final static int HEIGHT = 700;
	}
	
	// 메뉴 바 관련 상수 정의
	public static class GMenuBar {
		// 메뉴 항목들을 정의한 열거형
		public enum EMemu {
			eFile("파일"),
			eEdit("편집");
			
			
			// 각 메뉴의 텍스트를 저장
			private String text;
			
			// 생성자
			private EMemu(String text) {
				this.text = text;
			}
			
			// 텍스트를 반환하는 메서드
			public String getText() { return this.text; }
		}
	}
}
