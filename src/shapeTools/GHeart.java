package shapeTools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import shapeTools.GShape.EDrawingStyle;

public class GHeart extends GShape {

    // 생성자: EDrawingStyle.e2PStyle 스타일을 사용하고 하트를 생성
    public GHeart() {
        super(EDrawingStyle.e2PStyle, new Path2D.Double());
    }

    // 도형을 복제하는 메서드
    @Override
    public GShape clone() {
        GHeart cloned = (GHeart) super.clone();
        return cloned;
    }

    // 드래그하여 하트를 그리는 메서드
    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());

        // 기존 하트 지우기
        Path2D heart = (Path2D) this.shape;
        heart.reset();
        drawHeart(heart, x1, y1, ox2 - x1, oy2 - y1);
        graphics2D.draw(heart);

        // 새로운 하트 그리기
        heart.reset();
        drawHeart(heart, x1, y1, x2 - x1, y2 - y1);
        super.draw(graphics2D); // 색상 적용
    }

    // 하트를 그리는 메서드
    private void drawHeart(Path2D path, double x, double y, double width, double height) {
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        double topCurveHeight = height * 0.3;

        path.moveTo(centerX, centerY + height / 4);
        path.curveTo(centerX + width / 2, centerY - topCurveHeight, centerX + width / 2, centerY + height / 2, centerX, centerY + height);
        path.curveTo(centerX - width / 2, centerY + height / 2, centerX - width / 2, centerY - topCurveHeight, centerX, centerY + height / 4);
        path.closePath();
    }
}
