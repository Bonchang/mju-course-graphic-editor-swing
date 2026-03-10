package shapeTools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import shapeTools.GShape.EDrawingStyle;

public class GPencil extends GShape {
    private static final long serialVersionUID = 1L;

    public GPencil() {
        super(EDrawingStyle.eNPStyle, new Path2D.Float());
    }

    @Override
    public GShape clone() {
        GPencil cloned = (GPencil) super.clone();
        cloned.shape = (Path2D) ((Path2D) this.shape).clone();
        return cloned;
    }

    @Override
    public void drag(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());

        Path2D path = (Path2D) this.shape;
        path.lineTo(x2, y2);

        super.draw(graphics2D); // 색상 적용
    }

    @Override
    public void setOrigin(int x1, int y1) {
        super.setOrigin(x1, y1);
        Path2D path = (Path2D) this.shape;
        path.moveTo(x1, y1);
    }

    @Override
    public void movePoint(int x2, int y2) {
        super.movePoint(x2, y2);
        Path2D path = (Path2D) this.shape;
        path.lineTo(x2, y2);
    }
}
