package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ResultGUI extends JFrame implements ActionListener {
	private int mode = 0, color_select; // mode: 압축인지 압축해제인지 구별
	private Container c;
	private JTextArea ta_src = new JTextArea(); // 파일 내용 TextArea 객체
	private JTextArea ta_res = new JTextArea(); // 결과 내용 TextArea 객체
	private JButton go_main = new JButton("메인으로 돌아가기");
	private JFileChooser chooser = new JFileChooser();
	private String
	src_text = "", stat_text = "", filepath = "",
	DESC = "허프만 코딩(Huffman coding)은 자료 압축의 가장 오래되고 기초적인 방법 중의 하나이며 \r\n"
			+ "무손실 압축에 쓰이는 엔트로피 부호화의 일종으로, 데이터 문자의 등장 빈도에 따라서 다른 길이의 부호를 사용하는 알고리즘입니다.\r\n"
			+ "위 프로그램은 텍스트 파일을 압축 및 해제 기능을 지원하며 평균적인 통계정보 및 결과 파일들을 제공합니다.\r\n",
	MENUAL = "1. '파일-파일 불러오기' 메뉴를 통해 압축 또는 압축해제할 텍스트 파일을 불러옵니다.\r\n"
			+ "2. 파일을 불러온 후, '압축' 또는 '압축해제' 버튼을 클릭합니다.\r\n"
			+ "3. 원본 파일 내용과 함께 압축 또는 압축해제된 결과가 화면에 출력되며 해당 내용은 자동저장 됩니다.\r\n"
			+ "    이 때, 다른 파일명으로 저장되기를 원하시면 '파일-다른 이름으로 저장'메뉴를 선택해 주시기 바랍니다.\r\n"
			+ "4. 결과에 관한 자세한 사항을 보고 싶으시다면 '보기-통계정보'메뉴를 선택하여 주시기 바랍니다.\r\n"
			+ "5. 새로운 작업 수행을 원하실 경우, '메인으로 돌아가기' 버튼을 눌러주시기 바랍니다.";
	
	public ResultGUI(int mode, int theme, String srctext, String restext, String stattext, String filepath) {
		this.mode = mode;
		this.src_text = srctext;
		this.stat_text = stattext;
		this.filepath = filepath;
		
		// 프레임 기본 설정
		go_main.addActionListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		c.setLayout(new BorderLayout(0, 5));
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		
		// 압축/압축해제에 따른 타이틀 설정
		if(mode == 0) // '압축' 버튼 눌렀을 때
			setTitle("Huffman Coding Result");
		if(mode == 1) // '압축해제' 버튼 눌렀을 때
			setTitle("Huffman Decoding Result");
		
		// 테마 모드 설정(MainGUI에서 설정된 값 그대로 가져오기)
		switch(theme) {
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
		
		// 화면 구성 추가
		createMenu();
		c.add(addLabel(), BorderLayout.NORTH);
		c.add(addTextArea(), BorderLayout.CENTER);
		c.add(go_main, BorderLayout.SOUTH);
		
		// TextArea에 내용 출력
		ta_src.setText(srctext);
		ta_res.setText(restext);
		
		// 프레임 위치, 크기, 보이기 값 설정
		setLocation(res.width/5, res.height/5);
		setSize(900, 600);
		setVisible(true);
	}
	// 메뉴 생성 함수
	private void createMenu() {
		JMenuBar mb = new JMenuBar(); // 메뉴바 생성
		
		// 메뉴 객체 생성
		JMenu fileMenu = new JMenu("파일");
		JMenu showMenu = new JMenu("보기");
		JMenu helpMenu = new JMenu("도움말");
		
		// 메뉴 바에 메뉴 추가
		mb.add(fileMenu);
		mb.add(showMenu);
		mb.add(helpMenu);
		
		// 서브 메뉴 생성
		JMenuItem menuItem = null;
		String[] itemTitle = { "다른 이름으로 저장", "종료", "통계정보", "프로그램 설명", "프로그램 사용법" };
		for(int i=0; i < itemTitle.length; i++) {
			menuItem = new JMenuItem(itemTitle[i]);
			menuItem.addActionListener(this);
			if(i<2) {
				if(i == 1)
					fileMenu.addSeparator();
				fileMenu.add(menuItem);
			}
			else if(i == 2)
				showMenu.add(menuItem);
			else
				helpMenu.add(menuItem);
		}
		// 메뉴바를 프레임에 부착
		setJMenuBar(mb);
	}
	// Label 설정 함수
	private JPanel addLabel() {
		JLabel src = new JLabel("< 파일 내용 >", JLabel.CENTER);
		JLabel enc = new JLabel("< 압축 결과 >", JLabel.CENTER);
		JLabel dec = new JLabel("< 압축 해제 >", JLabel.CENTER);
		src.setFont(new Font("굴림", Font.BOLD, 13));
		enc.setFont(new Font("굴림", Font.BOLD, 13));
		dec.setFont(new Font("굴림", Font.BOLD, 13));
		JPanel l_pn = new JPanel(new GridLayout(1, 2));
		l_pn.setBackground(c.getBackground());
		
		if(mode == 0) {
			l_pn.add(src);
			l_pn.add(enc);
		}
		if(mode == 1) {
			l_pn.add(src);
			l_pn.add(dec);
		}
		return l_pn;
	}
	// TextArea 설정 함수
	private JPanel addTextArea() {
		ta_src.setEditable(false);
		ta_res.setEditable(false);
		JScrollPane sp_src = new JScrollPane(ta_src);
		JScrollPane sp_res = new JScrollPane(ta_res);
		JPanel ta_pn = new JPanel(new GridLayout(1, 2, 10, 10));
		
		ta_pn.setBackground(c.getBackground());
		ta_pn.add(sp_src);
		ta_pn.add(sp_res);

		return ta_pn;
	}
	// 프로그램 설명 함수
	private void showDesc() { 
		JOptionPane.showMessageDialog(this, DESC, "Description", JOptionPane.QUESTION_MESSAGE);
	}
	// 프로그램 사용법 함수
	private void showMenual() {
		JOptionPane.showMessageDialog(this, MENUAL, "Menual", JOptionPane.QUESTION_MESSAGE);
	}
	// 통계정보 함수
	private void showStat(String text) {
		JOptionPane.showMessageDialog(this, text, "Statistical Information", JOptionPane.QUESTION_MESSAGE);
	}
	// 다른 이름으로 파일 저장 함수
	private void saveFile(int mode) {
		StringBuffer sb = new StringBuffer();
		File srcfile = null, savefile = null;
		sb.append(filepath);
		
		// 저장할 파일 이름 지정
		if(mode == 0) {	
			sb.delete(sb.length()-4, sb.length());
			sb.append("(zip).txt");
			srcfile = new File(chooser.getSelectedFile().getAbsolutePath()+".txt");
			savefile = new File(chooser.getSelectedFile().getAbsolutePath()+"(zip).txt");
			if (srcfile.exists()) { // 파일이 이미 존재하는 경우
				JOptionPane.showMessageDialog(this, "이미 해당파일이 존재합니다. 다른 이름으로 시도해 주시기 바랍니다!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(srcfile), "UTF-8"));
				bw.write(src_text);
				bw.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "저장 실패!", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		if(mode == 1) {
			sb.delete(sb.length()-9, sb.length());
			sb.append("(unzip).txt");
			savefile = new File(chooser.getSelectedFile().getAbsolutePath()+"(unzip).txt");
		}
		File file = new File(sb.toString());
		if (savefile.exists()) { // 파일이 이미 존재하는 경우
			JOptionPane.showMessageDialog(this, "이미 해당파일이 존재합니다. 다른 이름으로 시도해 주시기 바랍니다!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// 저장할 파일 내용을 불러와 사용자가 입력한 파일 이름으로 저장
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(savefile), "UTF-8"));
			
			int c = 0;
			while((c = br.read()) != -1) 
				bw.write(c);
			br.close();
			bw.close();
			JOptionPane.showMessageDialog(this, "저장완료!", "Success", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "저장 실패!", "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
	// 이벤트 처리 함수
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		switch(s) {
		case "다른 이름으로 저장":
			chooser.setDialogTitle("다른 이름으로 저장");
			
			//파일 다이얼로그 출력
			int choose = chooser.showSaveDialog(this);
			if(choose != JFileChooser.APPROVE_OPTION) { // 확인 버튼을 정상적으로 누르지 않은 경우(사용자가 창을 강제롤 닫았거나 취소버튼을 누른 경우)
				JOptionPane.showMessageDialog(null, "파일 저장위치가 선택되지 않았습니다.");
				return;
			}
			this.saveFile(mode);
			break;
		case "종료":
			System.exit(0);
			break;
		case "통계정보":
			showStat(stat_text);
			break;
		case "프로그램 설명":
			showDesc();
			break;
		case "프로그램 사용법":
			showMenual();
			break;
		case "메인으로 돌아가기":
			new MainGUI(color_select);
			this.dispose();
			break;
		}
	}
}