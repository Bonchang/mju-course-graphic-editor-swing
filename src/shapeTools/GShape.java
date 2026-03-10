package shapeTools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;

public abstract class GShape implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    // 도형 그리기 스타일을 나타내는 열거형
    public enum EDrawingStyle {
        e2PStyle, // 두 점으로 그리는 스타일
        eNPStyle  // 여러 점으로 그리는 스타일
    }

    // 현재 도형의 그리기 스타일을 저장
    private EDrawingStyle eDrawingStyle;

    public EDrawingStyle getEDrawingStyle() {
        return this.eDrawingStyle;
    }

    // 실제 도형을 나타내는 Shape 객체
    protected Shape shape;
    // 도형의 색상
    private Color color;
    // 도형의 채우기 색상
    private Color fillColor;
    // 도형의 시작점과 끝점 좌표
    protected int x1, y1, x2, y2, ox2, oy2;

    // 도형의 앵커를 나타내는 열거형
    public enum EAnchors {
        eRR(new Cursor(Cursor.HAND_CURSOR)),
        eNN(new Cursor(Cursor.N_RESIZE_CURSOR)),
        eSS(new Cursor(Cursor.S_RESIZE_CURSOR)),
        eEE(new Cursor(Cursor.E_RESIZE_CURSOR)),
        eWW(new Cursor(Cursor.W_RESIZE_CURSOR)),
        eNE(new Cursor(Cursor.NE_RESIZE_CURSOR)),
        eSE(new Cursor(Cursor.SE_RESIZE_CURSOR)),
        eNW(new Cursor(Cursor.NW_RESIZE_CURSOR)),
        eSW(new Cursor(Cursor.SW_RESIZE_CURSOR)),
        eMM(new Cursor(Cursor.CROSSHAIR_CURSOR));

        private Cursor cursor;

        private EAnchors(Cursor cursor) {
            this.cursor = cursor;
        }

        public Cursor getCursor() {
            return this.cursor;
        }
    }

    // 현재 선택된 앵커
    private EAnchors eSelectedAnchor;
    // 앵커 배열
    protected Ellipse2D.Float[] anchors;

    // 크기 조절을 위한 변수
    private double sx, sy;
    private double dx, dy;

    // 도형 선택 여부
    private boolean isSelected;

    // 도형 선택 여부를 설정하는 메서드
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    // 도형이 선택되었는지 여부를 반환하는 메서드
    public boolean isSelected() {
        return this.isSelected;
    }
    // 현재 선택된 앵커를 반환하는 메서드
    public EAnchors getSelectedAnchor() {
        return this.eSelectedAnchor;
    }
    // 현재 선택된 앵커의 커서를 반환하는 메서드
    public Cursor getCursor() {
        return this.eSelectedAnchor.getCursor();
    }
    // 도형의 색상을 설정하는 메서드
    public void setColor(Color color) {
        this.color = color;
    }
    // 도형의 채우기 색상을 설정하는 메서드
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
    
    // 생성자
    public GShape(EDrawingStyle eDrawingStyle, Shape shape) {
        this.eDrawingStyle = eDrawingStyle;
        this.shape = shape;
        this.anchors = null;
        this.eSelectedAnchor = null;

        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
        this.ox2 = 0;
        this.oy2 = 0;
        this.isSelected = false;
    }

    // 도형을 복제하는 메서드
    @Override
    public GShape clone() {
        try {
            GShape cloned = (GShape) super.clone();
            if (this.shape instanceof Rectangle) {
                cloned.shape = new Rectangle((Rectangle) this.shape);
            } else if (this.shape instanceof Ellipse2D.Float) {
                Ellipse2D.Float original = (Ellipse2D.Float) this.shape;
                cloned.shape = new Ellipse2D.Float(original.x, original.y, original.width, original.height);
            } else if (this.shape instanceof Line2D.Float) {
                Line2D.Float original = (Line2D.Float) this.shape;
                cloned.shape = new Line2D.Float(original.x1, original.y1, original.x2, original.y2);
            } else if (this.shape instanceof RoundRectangle2D.Double) {
                RoundRectangle2D.Double original = (RoundRectangle2D.Double) this.shape;
                cloned.shape = new RoundRectangle2D.Double(original.getX(), original.getY(), original.getWidth(), original.getHeight(), original.getArcWidth(), original.getArcHeight());
            } else if (this.shape instanceof Polygon) {
                Polygon original = (Polygon) this.shape;
                cloned.shape = new Polygon(original.xpoints, original.ypoints, original.npoints);
            } else if (this.shape instanceof Path2D.Double) {
                cloned.shape = (Path2D.Double) ((Path2D.Double) this.shape).clone();
            } else {
                throw new UnsupportedOperationException("Shape type not supported for cloning");
            }
            cloned.x1 = this.x1;
            cloned.y1 = this.y1;
            cloned.x2 = this.x2;
            cloned.y2 = this.y2;
            cloned.ox2 = this.ox2;
            cloned.oy2 = this.oy2;
            cloned.setSelected(this.isSelected());
            return cloned;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }


    // 도형을 그리는 메서드
    public void draw(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (fillColor != null) {
            graphics2D.setColor(fillColor);
            graphics2D.fill(shape);
        }
        if (color != null) {
            graphics2D.setColor(color);
            graphics2D.draw(shape);
        }
        // 선택된 도형일 경우 앵커를 그림
        if (isSelected) {
            drawAnchors(graphics2D);
        }
    }

    // 앵커를 그리는 메서드
    private void drawAnchors(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        Rectangle rectangle = this.shape.getBounds();
        int x = rectangle.x;
        int y = rectangle.y;
        int w = rectangle.width;
        int h = rectangle.height;
        int ANCHOR_SIZE = 10;

        // 앵커 배열 초기화
        this.anchors = new Ellipse2D.Float[EAnchors.values().length - 1];
        this.anchors[EAnchors.eRR.ordinal()] = new Ellipse2D.Float(x + w / 2 - ANCHOR_SIZE / 2, y - 30, ANCHOR_SIZE, ANCHOR_SIZE);
        this.anchors[EAnchors.eNN.ordinal()] = new Ellipse2D.Float(x + w / 2 - ANCHOR_SIZE / 2, y - ANCHOR_SIZE / 2, ANCHOR_SIZE, ANCHOR_SIZE);
        this.anchors[EAnchors.eSS.ordinal()] = new Ellipse2D.Float(x + w / 2 - ANCHOR_SIZE / 2, y + h - ANCHOR_SIZE / 2, ANCHOR_SIZE, ANCHOR_SIZE);
        this.anchors[EAnchors.eEE.ordinal()] = new Ellipse2D.Float(x + w - ANCHOR_SIZE / 2, y + h / 2 - ANCHOR_SIZE / 2, ANCHOR_SIZE, ANCHOR_SIZE);
        this.anchors[EAnchors.eWW.ordinal()] = new Ellipse2D.Float(x - ANCHOR_SIZE / 2, y + h / 2 - ANCHOR_SIZE / 2, ANCHOR_SIZE, ANCHOR_SIZE);
        this.anchors[EAnchors.eNW.ordinal()] = new Ellipse2D.Float(x - ANCHOR_SIZE / 2, y - ANCHOR_SIZE / 2, ANCHOR_SIZE, ANCHOR_SIZE);
        this.anchors[EAnchors.eNE.ordinal()] = new Ellipse2D.Float(x + w - ANCHOR_SIZE / 2, y - ANCHOR_SIZE / 2, ANCHOR_SIZE, ANCHOR_SIZE);
        this.anchors[EAnchors.eSW.ordinal()] = new Ellipse2D.Float(x - ANCHOR_SIZE / 2, y + h - ANCHOR_SIZE / 2, ANCHOR_SIZE, ANCHOR_SIZE);
        this.anchors[EAnchors.eSE.ordinal()] = new Ellipse2D.Float(x + w - ANCHOR_SIZE / 2, y + h - ANCHOR_SIZE / 2, ANCHOR_SIZE, ANCHOR_SIZE);

        // 모든 앵커를 그림
        for (Ellipse2D anchor : this.anchors) {
            graphics2D.draw(anchor);
        }
    }

    // 도형의 시작점을 설정하는 메서드
    public void setOrigin(int x1, int y1) {
        this.x1 = x1;
        this.y1 = y1;

        this.ox2 = x1;
        this.oy2 = y1;
        this.x2 = x1;
        this.y2 = y1;
    }

    // 도형의 끝점을 이동시키는 메서드
    public void movePoint(int x2, int y2) {
        this.ox2 = this.x2;
        this.oy2 = this.y2;
        this.x2 = x2;
        this.y2 = y2;
        
    }

    // 도형을 드래그하는 추상 메서드
    public abstract void drag(Graphics graphics);

    // 도형에 점을 추가하는 메서드
    public void addPoint(int x2, int y2) {
        this.x2 = x2;
        this.y2 = y2;
    }

    // 특정 좌표가 도형 내부에 있는지 확인하는 메서드
    public boolean onShape(int x, int y) {
        this.eSelectedAnchor = null;
        if (this.anchors != null) {
            for (int i = 0; i < EAnchors.values().length - 1; i++) {
                if (anchors[i].contains(x, y)) {
                    this.eSelectedAnchor = EAnchors.values()[i];
                    return true;
                }
            }
        }
        boolean isOnShape = false;
        if (this.shape instanceof Line2D) {
            Line2D line = (Line2D) this.shape;
            isOnShape = line.ptSegDist(x, y) < 5; // 히트 박스 크기 조절
        } else {
            isOnShape = this.shape.contains(x, y);
        }
        if (isOnShape) {
            this.eSelectedAnchor = EAnchors.eMM;
        }
        return isOnShape;
    }

    // 도형 이동을 시작하는 메서드
    public void startMove(Graphics graphics, int x, int y) {
        this.ox2 = x;
        this.oy2 = y;
        this.x2 = x;
        this.y2 = y;
    }

    // 도형을 계속 이동하는 메서드
    public void keepMove(Graphics graphics, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());
        graphics2D.draw(this.shape);

        this.ox2 = this.x2;
        this.oy2 = this.y2;
        this.x2 = x;
        this.y2 = y;

        int dx = x2 - ox2;
        int dy = y2 - oy2;
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToTranslation(dx, dy);
        this.shape = affineTransform.createTransformedShape(this.shape);

        graphics2D.draw(this.shape);
    }

    // 도형 이동을 종료하는 메서드
    public void stopMove(Graphics graphics, int x, int y) {
    }

    // 도형 크기 조정을 시작하는 메서드
    public void startResize(Graphics graphics, int x, int y) {
        this.ox2 = x;
        this.oy2 = y;
        this.x2 = x;
        this.y2 = y;
    }

    // 크기 조정 팩터를 계산하는 메서드
    private Point2D getResizeFactor() {
        sx = 1;
        sy = 1;
        dx = 0;
        dy = 0;

        double x = this.shape.getBounds2D().getX();
        double y = this.shape.getBounds2D().getY();
        double w = this.shape.getBounds().getWidth();
        double h = this.shape.getBounds().getHeight();

        switch (this.eSelectedAnchor) {
            case eEE:
                sx = (w + x2 - ox2) / w;
                dx = x;
                break;
            case eWW:
                sx = (w - x2 + ox2) / w;
                dx = x + w;
                break;
            case eSS:
                sy = (h + y2 - oy2) / h;
                dy = y;
                break;
            case eNN:
                sy = (h - y2 + oy2) / h;
                dy = y + h;
                break;
            case eSE:
                sx = (w + x2 - ox2) / w;
                sy = (h + y2 - oy2) / h;
                dx = x;
                dy = y;
                break;
            case eSW:
                sx = (w - x2 + ox2) / w;
                sy = (h + y2 - oy2) / h;
                dx = x + w;
                dy = y;
                break;
            case eNE:
                sx = (w + x2 - ox2) / w;
                sy = (h - y2 + oy2) / h;
                dx = x;
                dy = y + h;
                break;
            case eNW:
                sx = (w - x2 + ox2) / w;
                sy = (h - y2 + oy2) / h;
                dx = x + w;
                dy = y + h;
                break;
            default:
                break;
        }
        return new Point2D.Double(sx, sy);
    }

    // 도형 크기 조정을 계속하는 메서드
    public void keepResize(Graphics graphics, int x, int y) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setXORMode(graphics2D.getBackground());
        graphics2D.draw(this.shape);

        this.ox2 = this.x2;
        this.oy2 = this.y2;
        this.x2 = x;
        this.y2 = y;

        Point2D resizeFactor = getResizeFactor();

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(dx * (1 - resizeFactor.getX()), dy * (1 - resizeFactor.getY()));
        affineTransform.scale(resizeFactor.getX(), resizeFactor.getY());
        this.shape = affineTransform.createTransformedShape(this.shape);

        graphics2D.draw(this.shape);
    }

    // 도형 크기 조정을 종료하는 메서드
    public void stopResize(Graphics graphics, int x, int y) {
    }

    // 도형을 복사할 때 사용하는 메서드들
    public Rectangle getBounds() {
        return shape.getBounds();
    }
    public void moveTo(int x, int y) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToTranslation(x - this.shape.getBounds().x, y - this.shape.getBounds().y);
        this.shape = affineTransform.createTransformedShape(this.shape);
    }
    public void translate(int dx, int dy) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(dx, dy);
        this.shape = affineTransform.createTransformedShape(this.shape);
    }
    @Override
    public String toString() {
        Rectangle bounds = this.shape.getBounds();
        return this.getClass().getSimpleName() + " [x=" + bounds.x + ", y=" + bounds.y + ", width=" + bounds.width + ", height=" + bounds.height + "]";
    }
    
 // 도형 회전 메서드 추가
    public void rotate(Graphics2D graphics2D, double angle) {
        AffineTransform transform = new AffineTransform();
        Rectangle bounds = shape.getBounds();
        transform.rotate(Math.toRadians(angle), bounds.getCenterX(), bounds.getCenterY());
        shape = transform.createTransformedShape(shape);
    }

}
