package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import Huffman.HuffmanTree;

@SuppressWarnings("serial")
public class MainGUI extends JFrame implements ActionListener {
	private int color_select; // 테마 색
	private Container c; // 컨테이너 객체
	private JFileChooser chooser = new JFileChooser(); // 파일 선택
	private JLabel la = new JLabel("Huffman Coding");
	private JLabel load = new JLabel("불러온 파일");
	private JTextField tf = new JTextField();
	private JButton encode = new JButton("압축");
	private JButton decode = new JButton("압축해제");
	private String
	filepath = "",
	DESC = "허프만 코딩(Huffman coding)은 자료 압축의 가장 오래되고 기초적인 방법 중의 하나이며 \r\n"
			+ "무손실 압축에 쓰이는 엔트로피 부호화의 일종으로, 데이터 문자의 등장 빈도에 따라서 다른 길이의 부호를 사용하는 알고리즘입니다.\r\n"
			+ "위 프로그램은 텍스트 파일을 압축 및 해제 기능을 지원하며 평균적인 통계정보 및 결과 파일들을 제공합니다.\r\n",
	MENUAL = "1. '파일-파일 불러오기' 메뉴를 통해 압축 또는 압축해제할 텍스트 파일을 불러옵니다.\r\n"
			+ "2. 파일을 불러온 후, '압축' 또는 '압축해제' 버튼을 클릭합니다.\r\n"
			+ "3. 원본 파일 내용과 함께 압축 또는 압축해제된 결과가 화면에 출력되며 해당 내용은 자동저장 됩니다.\r\n"
			+ "    이 때, 다른 파일명으로 저장되기를 원하시면 '파일-다른 이름으로 저장'메뉴를 선택해 주시기 바랍니다.\r\n"
			+ "4. 결과에 관한 자세한 사항을 보고 싶으시다면 '보기-통계정보'메뉴를 선택하여 주시기 바랍니다.\r\n"
			+ "5. 새로운 작업 수행을 원하실 경우, '메인으로 돌아가기' 버튼을 눌러주시기 바랍니다.";
	
	public MainGUI(int color) {
		// 프레임 설정
		setTitle("Huffman Coding");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		c.setLayout(null);
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
	
		// 테마 색 설정
		switch(color) {
		case 0: // Blue
			c.setBackground(new Color(100, 180, 255));
			color_select = 0;
			break;
		case 1: // Pink
			c.setBackground(new Color(255, 200, 255));
			color_select = 1;
			break;
		case 2: // Purple
			c.setBackground(new Color(204, 153, 255));
			color_select = 2;
			break;
		case 3: // Green
			c.setBackground(new Color(200, 255, 200));
			color_select = 3;
			break;
		case 4: // Gray
			c.setBackground(new Color(200, 200, 200));
			color_select = 4;
			break;
		case 5: // Orange
			c.setBackground(new Color(255, 173, 0));
			color_select = 5;
			break;
		}
		
		// 메뉴바 달기
		createMenu();
				
		// 객체 위치, 크기, 폰트 설정
		la.setFont(new Font("Serif", Font.BOLD, 35));
		la.setBounds(120, 0, 400, 100);
		load.setBounds(140, 60, 100, 100);
		tf.setEditable(false);
		tf.setColumns(10);
		tf.setBounds(140, 125, 205, 20);
		encode.setBounds(140, 150, 100, 100);
		decode.setBounds(245, 150, 100, 100);
		
		// 버튼 비활성화 및 이벤트 리스너 달기
		encode.setEnabled(false);
		decode.setEnabled(false);
		encode.addActionListener(this);
		decode.addActionListener(this);
		
		// 컨테이너에 추가
		c.add(la);
		c.add(load);
		c.add(tf);
		c.add(encode);
		c.add(decode);
		
		// 프레임 위치, 크기, 보이기 값 설정
		setLocation(res.width/3, res.height/3);
		setSize(500, 350);
		setVisible(true);
	}
	// 메뉴 생성 함수
	private void createMenu() {
		// 메뉴바 생성
		JMenuBar mb = new JMenuBar(); 
		
		// 메뉴 객체 생성
		JMenu fileMenu = new JMenu("파일");
		JMenu themeMenu = new JMenu("테마");
		JMenu helpMenu = new JMenu("도움말");
		
		// 메뉴 바에 메뉴 추가
		mb.add(fileMenu);
		mb.add(themeMenu);
		mb.add(helpMenu);
		
		// 서브 메뉴 생성
		JMenuItem menuItem = null;
		String[] itemTitle = { "파일 불러오기", "종료", "Blue(Default)", "Pink", "Purple", "Green", "Gray", "Orange", "프로그램 설명", "프로그램 사용법" };
		for(int i=0; i < itemTitle.length; i++) {
			menuItem = new JMenuItem(itemTitle[i]);
			menuItem.addActionListener(this);
			if(i<2) {
				if(i == 1)
					fileMenu.addSeparator();
				fileMenu.add(menuItem);
			}
			else if(i>=2 && i<8)
				themeMenu.add(menuItem);
			else
				helpMenu.add(menuItem);
		}
		// 메뉴바를 프레임에 부착
		setJMenuBar(mb);
	}
	// 프로그램 설명 함수
	private void showDesc() { 
		JOptionPane.showMessageDialog(this, DESC, "Description", JOptionPane.QUESTION_MESSAGE);
	}
	// 프로그램 사용법 함수
	private void showMenual() {
		JOptionPane.showMessageDialog(this, MENUAL, "Menual", JOptionPane.QUESTION_MESSAGE);
	}
	// 파일 불러오기 함수
	private void fileLoad() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"텍스트 파일(.txt)", // 파일 유형
				"txt","txt(zip)","txt(unzip)"); //파일 다이얼로그에 파일 필터 설정
		chooser.setFileFilter(filter); //파일 다이얼로그에 파일 필터 설정
		
		//파일 다이얼로그 출력
		int choose = chooser.showOpenDialog(null);
		if(choose != JFileChooser.APPROVE_OPTION) { // 확인 버튼을 정상적으로 누르지 않은 경우(사용자가 창을 강제롤 닫았거나 취소버튼을 누른 경우)
			JOptionPane.showMessageDialog(null, "파일이 선택되지 않았습니다.");
			chooser = new JFileChooser();
			return;
		}
		
		//사용자가 파일을 선택하고 "열기"버튼을 누른 경우
		String filePath = chooser.getSelectedFile().getPath(); // 파일 경로를 가져오기
		String fileName = chooser.getSelectedFile().getName(); // 파일 이름을 가져오기
		
		// 파일 유형을 확인하여 압축/압축해제 버튼 활성화
		if(fileName.substring(fileName.length()-5).contains(")")) {
			if(fileName.substring(fileName.length()-9).contains("(zip)")) {
				encode.setEnabled(false);
				decode.setEnabled(true);
			}
			else {
				encode.setEnabled(true);
				decode.setEnabled(false);
			}
		}
		else {
			encode.setEnabled(true);
			decode.setEnabled(false);
		}
		this.filepath = filePath;
		tf.setText(fileName);
	}
	// 이벤트 처리 함수
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		switch(s) {
		case "파일 불러오기":
			this.fileLoad();
			break;
		case "종료":
			System.exit(0);
			break;
		case "Blue(Default)":
			c.setBackground(new Color(100, 180, 255));
			color_select = 0;
			break;
		case "Pink":
			c.setBackground(new Color(255, 200, 255));
			color_select = 1;
			break;
		case "Purple":
			c.setBackground(new Color(204, 153, 255));
			color_select = 2;
			break;
		case "Green":
			c.setBackground(new Color(200, 255, 200));
			color_select = 3;
			break;
		case "Gray":
			c.setBackground(new Color(200, 200, 200));
			color_select = 4;
			break;
		case "Orange":
			c.setBackground(new Color(255, 173, 0));
			color_select = 5;
			break;
		case "프로그램 설명":
			showDesc();
			break;
		case "프로그램 사용법":
			showMenual();
			break;
		case "압축":
			HuffmanTree treeE = new HuffmanTree(filepath, 0);
			if(treeE.getError()) // 압축 실패 시(결과 창 띄우지 않기)
				new MainGUI(color_select);
			else
				new ResultGUI(0, color_select, treeE.getSrcText(), treeE.getEncText(), treeE.getStatText(), filepath);
			this.dispose();
			break;
		case "압축해제":
			HuffmanTree treeD = new HuffmanTree(filepath, 1);
			if(treeD.getError()) // 압축해제 실패 시(결과 창 띄우지 않기)
				new MainGUI(color_select);
			else 
				new ResultGUI(1, color_select, treeD.getSrcText(), treeD.getDecText(), treeD.getStatText(), filepath);
			this.dispose();
			break;
		}
	}
}