package shapeTools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RectangularShape;

import shapeTools.GShape.EDrawingStyle;

public class GRectangle extends GShape {
	
    // 생성자: EDrawingStyle.e2PStyle 스타일을 사용하고 사각형을 생성
    public GRectangle() {
        super(EDrawingStyle.e2PStyle, new Rectangle());
    }
	
    // 도형을 복제하는 메서드
    @Override
    public GShape clone() {
        GRectangle cloned = (GRectangle) super.clone();
        return cloned;
    }

    // 드래그하여 사각형을 그리는 메서드
    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());

        // 기존 사각형 지우기
        RectangularShape shape = (RectangularShape) this.shape;
        shape.setFrame(x1, y1, ox2 - x1, oy2 - y1);
        graphics2D.draw(shape);
		
        // 새로운 사각형 그리기
        shape.setFrame(x1, y1, x2 - x1, y2 - y1);
        super.draw(graphics2D); // 색상 적용
    }
}
