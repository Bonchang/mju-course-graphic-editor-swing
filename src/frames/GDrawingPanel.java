package frames;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import shapeTools.GShape;
import shapeTools.GShape.EAnchors;
import shapeTools.GShape.EDrawingStyle;
import shapeTools.GText;

public class GDrawingPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    // 그리기 상태를 나타내는 열거형
    private enum EDrawingState {
        eIdle,          // 초기 상태
        e2PState,       // 두 점으로 그리기
        eNPState,       // 여러 점으로 그리기
        eTransformation // 변형 상태
    }
    // 현재 그리기 상태
    private EDrawingState eDrawingState;

    // 도형들을 저장하는 벡터
    private Vector<GShape> shapes;
    // 현재 선택된 도형 도구
    private GShape shapeTool;
    // 현재 그리고 있는 도형
    private GShape currentShape;
    // 현재 색상 필드 추가
    private Color currentColor;
    // 현재 채우기 색상
    private Color currentFillColor;
 
    // 더블 버퍼링을 위한 BufferedImage와 Graphics2D 객체
    private BufferedImage bufferedImage;
    private Graphics2D graphics;
    
    // 클립보드에 저장된 도형
    private GShape clipboardShape;
    // 확대/축소 배율
    private double scale = 1.0;
    

    // 생성자
    public GDrawingPanel() {
        // 패널의 배경색을 흰색으로 설정
        this.setBackground(Color.WHITE);
        this.eDrawingState = EDrawingState.eIdle;
        // 초기 색상 설정
        this.currentColor = Color.BLACK;
        this.currentFillColor = Color.WHITE;

        // 마우스 이벤트 핸들러 설정
        MouseEventHandler mouseEventHandler = new MouseEventHandler();
        this.addMouseListener(mouseEventHandler);
        this.addMouseMotionListener(mouseEventHandler);

        // 도형 목록 초기화
        this.shapes = new Vector<GShape>();

        // 컴포넌트 리스너 추가
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // 컴포넌트가 표시될 때 위치를 가져오는 코드
                Point location = getLocationOnScreen();
                // 위치를 사용한 다른 작업
            }
        });
    }

    // 초기화: BufferedImage와 Graphics2D 객체를 생성하고 초기화
    public void intitialize() {
        // 패널 크기와 동일한 크기의 BufferedImage 생성
        bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        // BufferedImage에 그리기 위한 Graphics2D 객체 생성
        graphics = (Graphics2D) bufferedImage.getGraphics();
        // 배경색 설정
        graphics.setColor(this.getForeground());
        graphics.setBackground(Color.WHITE);
        // 초기화 시 이미지를 투명하게 설정
        clear();
    }
    // 색상을 설정
    public void setCurrentColor(Color color) {
        this.currentColor = color;
        if (graphics != null) {
            graphics.setColor(currentColor);
        }
    }
    // 채우기 색상을 설정하는 메서드
    public void setCurrentFillColor(Color fillColor) {
        this.currentFillColor = fillColor;
    }

    // 도형 도구를 설정
    public void setShapeTool(GShape shapeTool) {
        this.shapeTool = shapeTool;
    }

    // 도형 목록을 반환
    public Vector<GShape> getShapes() {
        return this.shapes;
    }

    // 도형 목록을 설정
    public void setShapes(Object object) {
        this.shapes = (Vector<GShape>) object;
    }

    // 패널을 다시 그릴 때 호출
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        // BufferedImage가 초기화되지 않은 경우 초기화
        if (bufferedImage == null) {
            intitialize();
        }
        // Graphics2D로 형변환
        Graphics2D g2d = (Graphics2D) graphics;
        // 확대/축소 배율 적용
        g2d.scale(scale, scale);
        // BufferedImage를 화면에 그림
        graphics.drawImage(bufferedImage, 0, 0, null);
    }

    // BufferedImage를 초기화하고 배경색으로 채움
    private void clear() {
        // BufferedImage Clear 하도록 설정
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        // 이후 그리는 작업은 배경색을 덮어쓰도록 설정
        graphics.setComposite(AlphaComposite.SrcOver);
        // 배경색으로 이미지 채움
        graphics.setColor(this.getBackground());
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        // 색 설정
        graphics.setColor(this.getForeground());
    }

    // 모든 도형을 다시 그리고 패널을 업데이트
    private void redraw() {
        // 이미지 초기화
        clear();
        // 모든 도형을 다시 그림
        for (GShape shape : shapes) {
            shape.draw(graphics);
        }
        // 로그 출력으로 현재 도형 목록 확인
        // System.out.println("Shapes: " + shapes); // test
        // 패널을 다시 그림
        repaint();
    }
    // 새로운 도형 그리기를 시작
    private void startDrawing(int x, int y) {
        if (shapeTool != null) { // shapeTool이 null이 아닌지 확인
            currentShape = shapeTool.clone();
            if (currentShape != null) { // currentShape이 null이 아닌지 확인
                currentShape.setOrigin(x, y);
                currentShape.setColor(currentColor); // 현재 색상 적용
                currentShape.setFillColor(currentFillColor); // 현재 채우기 색상 적용
                currentShape.setSelected(true);
                if (currentShape instanceof GText) {
                    shapes.add(currentShape); // 텍스트는 바로 추가
                    currentShape = null;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            redraw();
                            repaint();
                        }
                    });
                }
            }
        }
    }
    // 도형을 그리고 있는 중에 호출
    private void keepDrawing(int x, int y) {
        if (currentShape != null) {
            currentShape.setColor(currentColor); // 현재 색상 적용
            currentShape.setFillColor(currentFillColor); // 현재 채우기 색상 적용
            // 도형의 끝점을 이동
            currentShape.movePoint(x, y);
            // 모든 도형을 다시 그림
            redraw();
            // 현재 도형을 그림
            currentShape.setColor(currentColor); // 현재 색상 적용
            currentShape.drag(graphics);
            currentShape.draw(graphics);
            // 패널을 다시 그림
            repaint();
        }
    }
    // 여러 점으로 이루어진 도형을 계속 그림
    private void continueDrawing(int x, int y) {
        if (currentShape != null) {
            // 도형에 점을 추가
            currentShape.addPoint(x, y);
            // 모든 도형을 다시 그림
            redraw();
            // 현재 도형을 그림
            currentShape.setColor(currentColor); // 현재 색상 적용
            currentShape.setFillColor(currentFillColor); // 현재 채우기 색상 적용
            currentShape.drag(graphics);
            currentShape.draw(graphics);
            // 패널을 다시 그림
            repaint();
        }
    }

    // 도형 그리기를 종료
    private void stopDrawing(int x, int y) {
        if (currentShape != null) { // currentShape이 null이 아닌지 확인
            currentShape.setSelected(false);
            shapes.add(currentShape);
            currentShape = null;
            redraw();
        }
    }


    // 특정 좌표에 도형이 있는지 확인하는 메서드
    private GShape onShape(int x, int y) {
        // 모든 도형을 순회하며 좌표가 도형 내에 있는지 확인
        for (GShape shape : this.shapes) {
            if (shape.onShape(x, y)) {
                return shape;
            }
        }
        return null;
    }

    // 커서를 변경하는 메서드
    private void changeCursor(int x, int y) {
        // 특정 좌표에 도형이 있는지 확인
        GShape shape = this.onShape(x, y);
        if (shape == null) {
            // 도형이 없으면 기본 커서로 설정
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            // 도형이 있으면 해당 도형의 커서로 설정
            this.setCursor(shape.getCursor());
        }
    }
    
    // EditMenu에서 호출하는 메서드들
    public void cut() {
        if (currentShape != null && currentShape.isSelected()) {
            clipboardShape = currentShape.clone();
            clipboardShape.setSelected(false);
            shapes.remove(currentShape);
            currentShape = null;
            redraw();
        } else {
            System.out.println("No shape selected to cut."); 
        }
    }
    public void copy() {
        if (currentShape != null && currentShape.isSelected()) {
            clipboardShape = currentShape.clone();
            System.out.println(clipboardShape);
        } else {
            System.out.println("No shape selected to copy."); 
        }
    }
    public void paste() {
        if (clipboardShape != null) {
            if (isShowing()) {
                // 클립보드의 도형을 복제하여 새 도형 생성
                GShape newShape = clipboardShape.clone();
                // 새로운 도형을 기존 위치에서 약간 이동시킴
                newShape.translate(10, 10);
                // 새 도형을 도형 목록에 추가
                shapes.add(newShape);
                // 로그 출력으로 도형이 제대로 추가되었는지 확인
                System.out.println("Pasted shape: " + newShape); // test
                // 현재 도형을 null로 설정
                currentShape = null;
                // 모든 도형을 다시 그림
                redraw();
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        paste();
                    }
                });
            }
        } else {
            System.out.println("No shape in clipboard to paste."); 
        }
    }

    public void delete() {
        if (currentShape != null && currentShape.isSelected()) {
            shapes.remove(currentShape);
            System.out.println(currentShape); // test
            currentShape = null;
            redraw();
        } else {
            System.out.println("No shape in clipboard to delete."); 
        }
    }
    // 확대/축소 메서드
    public void zoomIn() {
        scale *= 1.1;
        repaint();
    }

    public void zoomOut() {
        scale /= 1.1;
        repaint();
    }
    public void resetZoom() {
        scale = 1.0;
        repaint();
    }
    
 // 도형을 앞으로 가져오는 메서드
    public void bringToFront() {
        if (currentShape != null && shapes.contains(currentShape)) {
            shapes.remove(currentShape);
            shapes.add(currentShape);
            redraw();
        }
    }

    // 도형을 뒤로 보내는 메서드
    public void sendToBack() {
        if (currentShape != null && shapes.contains(currentShape)) {
            shapes.remove(currentShape);
            shapes.add(0, currentShape);
            redraw();
        }
    }
 // 배경색을 설정하는 메서드
    public void setBackgroundColor(Color color) {
        this.setBackground(color);
        if (graphics != null) {
            graphics.setBackground(color);
            clear();  // 배경색 변경 시 화면을 지우고 다시 그리기
            redraw();
        }
    }
 // 도형 회전 메서드 추가
    public void rotateCurrentShape(double angle) {
        if (currentShape != null && currentShape.isSelected()) {
            Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
            currentShape.rotate(graphics2D, angle);
            redraw();
        } else {
            System.out.println("No shape selected to rotate.");
        }
    }
 // 이미지를 파일로 저장하는 메서드
    public void saveImage(File file) {
        try {
            // 패널의 내용을 BufferedImage로 복사
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            paint(image.getGraphics());
            // 파일로 저장
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // 마우스 이벤트 핸들러 클래스
    private class MouseEventHandler implements MouseListener, MouseMotionListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                mouse1Clicked(e);
            } else if (e.getClickCount() == 2) {
                mouse2Clicked(e);
            }
        }

        // 마우스 단일 클릭 처리
        private void mouse1Clicked(MouseEvent e) {
            if (eDrawingState == EDrawingState.eIdle) {
                // 현재 상태가 Idle인 경우
                if (shapeTool != null && shapeTool.getEDrawingStyle() == EDrawingStyle.eNPStyle) {
                    // 도형 도구가 다점 도형인 경우
                    startDrawing(e.getX(), e.getY());
                    eDrawingState = EDrawingState.eNPState;
                } else {
                    currentShape = onShape(e.getX(), e.getY());
                    if (currentShape != null) {
                        for (GShape shape : shapes) {
                            shape.setSelected(false);
                        }
                        currentShape.setSelected(true);
                        redraw();
                    }
                }
            } else if (eDrawingState == EDrawingState.eNPState) {
                // 다점 도형을 그리는 중인 경우
                continueDrawing(e.getX(), e.getY());
            }
        }

        // 마우스 더블 클릭 처리
        private void mouse2Clicked(MouseEvent e) {
            if (eDrawingState == EDrawingState.eNPState) {
                // 다점 도형을 그리는 중인 경우 도형 그리기 종료
                stopDrawing(e.getX(), e.getY());
                eDrawingState = EDrawingState.eIdle;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (eDrawingState == EDrawingState.eIdle) {
                // 현재 상태가 Idle인 경우 커서 변경
                changeCursor(e.getX(), e.getY());
            } else if (eDrawingState == EDrawingState.eNPState) {
                // 다점 도형을 그리는 중인 경우 도형 그리기 계속
                keepDrawing(e.getX(), e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (eDrawingState == EDrawingState.eIdle) {
                // 현재 상태가 Idle인 경우
                currentShape = onShape(e.getX(), e.getY());
                if (currentShape == null && shapeTool != null) { // shapeTool이 null이 아닌지 확인
                    // 클릭한 좌표에 도형이 없는 경우
                    if (shapeTool.getEDrawingStyle() == EDrawingStyle.e2PStyle) {
                        // 도형 도구가 2점 도형인 경우 도형 그리기 시작
                        startDrawing(e.getX(), e.getY());
                        eDrawingState = EDrawingState.e2PState;
                    }
                } else if (currentShape != null) {
                    // 클릭한 좌표에 도형이 있는 경우
                    for (GShape shape : shapes) {
                        shape.setSelected(false);
                    }
                    currentShape.setSelected(true);
                    if (currentShape.getSelectedAnchor() == EAnchors.eMM) {
                        // 도형 이동 시작
                        currentShape.startMove(graphics, e.getX(), e.getY());
                    } else {
                        // 도형 크기 조정 시작
                        currentShape.startResize(graphics, e.getX(), e.getY());
                    }
                    eDrawingState = EDrawingState.eTransformation;
                    redraw();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (eDrawingState == EDrawingState.e2PState) {
                // 2점 도형을 그리는 중인 경우
                keepDrawing(e.getX(), e.getY());
            } else if (eDrawingState == EDrawingState.eTransformation) {
                // 도형 변형 중인 경우
                if (currentShape.getSelectedAnchor() == EAnchors.eMM) {
                    // 도형 이동 계속
                    currentShape.keepMove(graphics, e.getX(), e.getY());
                } else {
                    // 도형 크기 조정 계속
                    currentShape.keepResize(graphics, e.getX(), e.getY());
                }
                redraw();
            }
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            if (eDrawingState == EDrawingState.e2PState) {
                // 2점 도형 그리기 종료
                stopDrawing(e.getX(), e.getY());
                eDrawingState = EDrawingState.eIdle;
            } else if (eDrawingState == EDrawingState.eTransformation) {
                if (currentShape != null) {
                    if (currentShape.getSelectedAnchor() == EAnchors.eMM) {
                        // 도형 이동 종료
                        currentShape.keepMove(graphics, e.getX(), e.getY());
                    } else {
                        // 도형 크기 조정 종료
                        currentShape.stopResize(graphics, e.getX(), e.getY());
                    }
                }
                eDrawingState = EDrawingState.eIdle;
                redraw();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}