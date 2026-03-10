package shapeTools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class GPolygon extends GShape {

    private static final long serialVersionUID = 1L;

    // 생성자: EDrawingStyle.eNPStyle 스타일을 사용하고 다각형을 생성
    public GPolygon() {
        super(EDrawingStyle.eNPStyle, new Polygon());
    }

    // 도형을 복제하는 메서드
    @Override
    public GPolygon clone() {
        GPolygon cloned = (GPolygon) super.clone();
        if (this.shape instanceof Polygon) {
            Polygon original = (Polygon) this.shape;
            cloned.shape = new Polygon(original.xpoints, original.ypoints, original.npoints);
        }
        return cloned;
    }

    // 드래그하여 다각형을 그리는 메서드
    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());
        // 기존 도형 지우기
        Polygon polygon = (Polygon) this.shape;
        graphics.drawLine(
                polygon.xpoints[polygon.npoints - 2], polygon.ypoints[polygon.npoints - 2],
                polygon.xpoints[polygon.npoints - 1], polygon.ypoints[polygon.npoints - 1]
        );
        polygon.xpoints[polygon.npoints - 1] = x2;
        polygon.ypoints[polygon.npoints - 1] = y2;
        // 새로운 도형 그리기
        graphics.drawLine(
                polygon.xpoints[polygon.npoints - 2], polygon.ypoints[polygon.npoints - 2],
                polygon.xpoints[polygon.npoints - 1], polygon.ypoints[polygon.npoints - 1]
        );
    }

    // 도형의 시작점을 설정하는 메서드
    @Override
    public void setOrigin(int x, int y) {
        Polygon polygon = (Polygon) this.shape;
        polygon.addPoint(x, y);
        polygon.addPoint(x, y);
    }

    // 도형에 점을 추가하는 메서드
    @Override
    public void addPoint(int x, int y) {
        Polygon polygon = (Polygon) this.shape;
        polygon.addPoint(x, y);
    }
}
