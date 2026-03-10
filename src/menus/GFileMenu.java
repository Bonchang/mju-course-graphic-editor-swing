package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import frames.GDrawingPanel;
import frames.GMainFrame;

public class GFileMenu extends JMenu {
    private static final long serialVersionUID = 1L;

    // 그리기 패널과의 연결
    private GDrawingPanel drawingPanel;
    
    // 그리기 패널과 연결
    public void associate(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }
    
    // 생성자
    public GFileMenu(String s) {
        super(s);
        ActionHandler actionHandler = new ActionHandler();
        
        // "new" 메뉴 아이템 생성 및 설정
        JMenuItem menuItemNew = new JMenuItem("new");
        menuItemNew.setActionCommand("new");
        menuItemNew.addActionListener(actionHandler);
        this.add(menuItemNew);
        
        // "open" 메뉴 아이템 생성 및 설정
        JMenuItem menuItemOpen = new JMenuItem("open");
        menuItemOpen.setActionCommand("open");
        menuItemOpen.addActionListener(actionHandler);
        this.add(menuItemOpen);
        
        // "save" 메뉴 아이템 생성 및 설정
        JMenuItem menuItemSave = new JMenuItem("save");
        menuItemSave.setActionCommand("save");
        menuItemSave.addActionListener(actionHandler);
        this.add(menuItemSave);

        // "save as image" 메뉴 아이템 생성 및 설정
        JMenuItem menuItemSaveImage = new JMenuItem("save as image");
        menuItemSaveImage.setActionCommand("saveImage");
        menuItemSaveImage.addActionListener(actionHandler);
        this.add(menuItemSaveImage);
    }
    
    // "new" 메서드
    private void newDrawing() {
        // GMainFrame 객체 생성
        GMainFrame mainFrame = new GMainFrame();
        // 메인 프레임을 보이도록 설정
        mainFrame.setVisible(true);
        // 메인 프레임 초기화
        mainFrame.initialize();
    }

    // 파일 열기 메서드
    private void open() {
        try {
            File file = new File("output");
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    new BufferedInputStream(new FileInputStream(file)));
            Object object = objectInputStream.readObject();
            this.drawingPanel.setShapes(object);
            objectInputStream.close();
            this.drawingPanel.repaint();
        } catch (IOException | ClassNotFoundException e) {
            // 예외 처리
            e.printStackTrace();
        }
    }
    
    // 파일 저장 메서드
    private void save() {
        try {
            File file = new File("output");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(file)));
            objectOutputStream.writeObject(this.drawingPanel.getShapes());
            objectOutputStream.close();
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace();
        }        
    }

    // 이미지를 파일로 저장하는 메서드
    private void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            drawingPanel.saveImage(file);
        }
    }

    // 액션 핸들러 클래스
    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("open")) {
                open();
            } else if (e.getActionCommand().equals("save")) {
                save();
            } else if (e.getActionCommand().equals("new")) {
                newDrawing();
            } else if (e.getActionCommand().equals("saveImage")) {
                saveImage();
            }
        }
    }
}
