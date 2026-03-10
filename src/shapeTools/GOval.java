package shapeTools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;

import shapeTools.GShape.EDrawingStyle;

public class GOval extends GShape {

    // 생성자: EDrawingStyle.e2PStyle 스타일을 사용하고 타원을 생성
    public GOval() {
        super(EDrawingStyle.e2PStyle, new Ellipse2D.Float());
    }

    // 도형을 복제하는 메서드
    @Override
    public GOval clone() {
        GOval cloned = (GOval) super.clone();
        return cloned;
    }

    // 드래그하여 타원을 그리는 메서드
    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());

        // 기존 타원 지우기
        RectangularShape shape = (RectangularShape) this.shape;
        shape.setFrame(x1, y1, ox2 - x1, oy2 - y1);
        graphics2D.draw(shape);

        // 새로운 타원 그리기
        shape.setFrame(x1, y1, x2 - x1, y2 - y1);
        graphics2D.draw(shape);
    }
}
