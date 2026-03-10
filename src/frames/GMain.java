package frames;

public class GMain {
	
	public GMain() {
	}

	// 프로그램 시작
	public static void main(String[] args) {
		// GMainFrame 객체 생성
		GMainFrame mainFrame = new GMainFrame();
		// 메인 프레임을 보이도록 설정
		mainFrame.setVisible(true);
		// 메인 프레임 초기화
		mainFrame.initialize();

	}
}
