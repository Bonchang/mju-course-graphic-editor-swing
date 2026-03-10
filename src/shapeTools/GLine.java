package shapeTools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class GLine extends GShape {

    // 생성자: EDrawingStyle.e2PStyle 스타일을 사용하고 선을 생성
    public GLine() {
        super(EDrawingStyle.e2PStyle, new Line2D.Float());
    }

    // 도형을 복제하는 메서드
    @Override
    public GLine clone() {
        GLine cloned = (GLine) super.clone();
        // Line2D.Float 복제
        if (this.shape instanceof Line2D.Float) {
            Line2D.Float original = (Line2D.Float) this.shape;
            cloned.shape = new Line2D.Float(original.x1, original.y1, original.x2, original.y2);
        }
        return cloned;
    }

    // 드래그하여 선을 그리는 메서드
    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());

        // 기존 선 지우기
        Line2D.Float shape = (Line2D.Float) this.shape;
        shape.setLine(x1, y1, ox2, oy2);
        graphics2D.draw(shape);

        // 새로운 선 그리기
        shape.setLine(x1, y1, x2, y2);
        graphics2D.draw(shape);    
    }
}
