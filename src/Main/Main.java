package Main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import GUI.MainGUI;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener{
	private JLabel la = new JLabel("Huffman Coding");
	private JLabel maker = new JLabel("- Made by. MSJ -");
	private JButton start = new JButton("시작");
	private JButton exit = new JButton("종료");
	
	public Main() {
		setTitle("Huffman Coding"); // 프레임 타이틀 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 윈도우가 닫힐 때 프로그램 종료
		Container c = getContentPane(); // 프레임의 컨텐트팬 알아내기
		c.setBackground(new Color(100, 180, 255)); // 배경색 설정
		c.setLayout(null); // 배치 위치 직접 설정
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize(); // 현재 화면의 해상도 가져오기
		
		// 객체 위치 및 크기 설정
		la.setFont(new Font("Serif", Font.BOLD, 50));
		la.setBounds(60, 0, 400, 100);
		maker.setFont(new Font("Serif", Font.BOLD, 15));
		maker.setBounds(350, 220, 150, 50);
		start.setBounds(190, 120, 100, 40);
		exit.setBounds(190, 180, 100, 40);
		
		// 버튼에 대한 이벤트 리스너 달기
		start.addActionListener(this);
		exit.addActionListener(this);
		
		// 컨테이너에 추가
		c.add(la);
		c.add(maker);
		c.add(start);
		c.add(exit);
		
		// 프레임 위치, 크기, 보이기 값 설정
		setLocation(res.width/3, res.height/3); 
		setSize(500, 300); 
		setVisible(true); 
	}
	// 버튼에 대한 이벤트 처리 함수
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		if(b.getText().equals("시작")) { // '시작'버튼에 대한 이벤트 처리
			new MainGUI(0);
			this.dispose(); // 현재 프레임만 종료
		}
		else if(b.getText().equals("종료")) // '종료'버튼에 대한 이벤트 처리
			System.exit(0);
	}
	// 메인 함수 실행
	public static void main(String args[]) {
		new Main();
	}
}