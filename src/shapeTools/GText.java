package shapeTools;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import shapeTools.GShape.EDrawingStyle;

public class GText extends GShape {
    private static final long serialVersionUID = 1L;
    private String text;
    private Font font;

    public GText() {
        super(EDrawingStyle.e2PStyle, new Rectangle());
        this.text = "";
        this.font = new Font("Arial", Font.PLAIN, 20);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public GText clone() {
        GText cloned = new GText();
        cloned.text = this.text;
        cloned.font = this.font;
        return cloned;
    }

    @Override
    public void drag(Graphics graphics) {
    	
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(this.font);
        Rectangle2D bounds = graphics2D.getFontMetrics().getStringBounds(this.text, graphics2D);
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        graphics2D.drawString(this.text, x, y + (int) bounds.getHeight());
    }
}
