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
	private int mode = 0, color_select; // mode: �������� ������������ ����
	private Container c;
	private JTextArea ta_src = new JTextArea(); // ���� ���� TextArea ��ü
	private JTextArea ta_res = new JTextArea(); // ��� ���� TextArea ��ü
	private JButton go_main = new JButton("�������� ���ư���");
	private JFileChooser chooser = new JFileChooser();
	private String
	src_text = "", stat_text = "", filepath = "",
	DESC = "������ �ڵ�(Huffman coding)�� �ڷ� ������ ���� �����ǰ� �������� ��� ���� �ϳ��̸� \r\n"
			+ "���ս� ���࿡ ���̴� ��Ʈ���� ��ȣȭ�� ��������, ������ ������ ���� �󵵿� ���� �ٸ� ������ ��ȣ�� ����ϴ� �˰����Դϴ�.\r\n"
			+ "�� ���α׷��� �ؽ�Ʈ ������ ���� �� ���� ����� �����ϸ� ������� ������� �� ��� ���ϵ��� �����մϴ�.\r\n",
	MENUAL = "1. '����-���� �ҷ�����' �޴��� ���� ���� �Ǵ� ���������� �ؽ�Ʈ ������ �ҷ��ɴϴ�.\r\n"
			+ "2. ������ �ҷ��� ��, '����' �Ǵ� '��������' ��ư�� Ŭ���մϴ�.\r\n"
			+ "3. ���� ���� ����� �Բ� ���� �Ǵ� ���������� ����� ȭ�鿡 ��µǸ� �ش� ������ �ڵ����� �˴ϴ�.\r\n"
			+ "    �� ��, �ٸ� ���ϸ����� ����Ǳ⸦ ���Ͻø� '����-�ٸ� �̸����� ����'�޴��� ������ �ֽñ� �ٶ��ϴ�.\r\n"
			+ "4. ����� ���� �ڼ��� ������ ���� �����ôٸ� '����-�������'�޴��� �����Ͽ� �ֽñ� �ٶ��ϴ�.\r\n"
			+ "5. ���ο� �۾� ������ ���Ͻ� ���, '�������� ���ư���' ��ư�� �����ֽñ� �ٶ��ϴ�.";
	
	public ResultGUI(int mode, int theme, String srctext, String restext, String stattext, String filepath) {
		this.mode = mode;
		this.src_text = srctext;
		this.stat_text = stattext;
		this.filepath = filepath;
		
		// ������ �⺻ ����
		go_main.addActionListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		c.setLayout(new BorderLayout(0, 5));
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		
		// ����/���������� ���� Ÿ��Ʋ ����
		if(mode == 0) // '����' ��ư ������ ��
			setTitle("Huffman Coding Result");
		if(mode == 1) // '��������' ��ư ������ ��
			setTitle("Huffman Decoding Result");
		
		// �׸� ��� ����(MainGUI���� ������ �� �״�� ��������)
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
		
		// ȭ�� ���� �߰�
		createMenu();
		c.add(addLabel(), BorderLayout.NORTH);
		c.add(addTextArea(), BorderLayout.CENTER);
		c.add(go_main, BorderLayout.SOUTH);
		
		// TextArea�� ���� ���
		ta_src.setText(srctext);
		ta_res.setText(restext);
		
		// ������ ��ġ, ũ��, ���̱� �� ����
		setLocation(res.width/5, res.height/5);
		setSize(900, 600);
		setVisible(true);
	}
	// �޴� ���� �Լ�
	private void createMenu() {
		JMenuBar mb = new JMenuBar(); // �޴��� ����
		
		// �޴� ��ü ����
		JMenu fileMenu = new JMenu("����");
		JMenu showMenu = new JMenu("����");
		JMenu helpMenu = new JMenu("����");
		
		// �޴� �ٿ� �޴� �߰�
		mb.add(fileMenu);
		mb.add(showMenu);
		mb.add(helpMenu);
		
		// ���� �޴� ����
		JMenuItem menuItem = null;
		String[] itemTitle = { "�ٸ� �̸����� ����", "����", "�������", "���α׷� ����", "���α׷� ����" };
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
		// �޴��ٸ� �����ӿ� ����
		setJMenuBar(mb);
	}
	// Label ���� �Լ�
	private JPanel addLabel() {
		JLabel src = new JLabel("< ���� ���� >", JLabel.CENTER);
		JLabel enc = new JLabel("< ���� ��� >", JLabel.CENTER);
		JLabel dec = new JLabel("< ���� ���� >", JLabel.CENTER);
		src.setFont(new Font("����", Font.BOLD, 13));
		enc.setFont(new Font("����", Font.BOLD, 13));
		dec.setFont(new Font("����", Font.BOLD, 13));
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
	// TextArea ���� �Լ�
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
	// ���α׷� ���� �Լ�
	private void showDesc() { 
		JOptionPane.showMessageDialog(this, DESC, "Description", JOptionPane.QUESTION_MESSAGE);
	}
	// ���α׷� ���� �Լ�
	private void showMenual() {
		JOptionPane.showMessageDialog(this, MENUAL, "Menual", JOptionPane.QUESTION_MESSAGE);
	}
	// ������� �Լ�
	private void showStat(String text) {
		JOptionPane.showMessageDialog(this, text, "Statistical Information", JOptionPane.QUESTION_MESSAGE);
	}
	// �ٸ� �̸����� ���� ���� �Լ�
	private void saveFile(int mode) {
		StringBuffer sb = new StringBuffer();
		File srcfile = null, savefile = null;
		sb.append(filepath);
		
		// ������ ���� �̸� ����
		if(mode == 0) {	
			sb.delete(sb.length()-4, sb.length());
			sb.append("(zip).txt");
			srcfile = new File(chooser.getSelectedFile().getAbsolutePath()+".txt");
			savefile = new File(chooser.getSelectedFile().getAbsolutePath()+"(zip).txt");
			if (srcfile.exists()) { // ������ �̹� �����ϴ� ���
				JOptionPane.showMessageDialog(this, "�̹� �ش������� �����մϴ�. �ٸ� �̸����� �õ��� �ֽñ� �ٶ��ϴ�!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(srcfile), "UTF-8"));
				bw.write(src_text);
				bw.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "���� ����!", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		if(mode == 1) {
			sb.delete(sb.length()-9, sb.length());
			sb.append("(unzip).txt");
			savefile = new File(chooser.getSelectedFile().getAbsolutePath()+"(unzip).txt");
		}
		File file = new File(sb.toString());
		if (savefile.exists()) { // ������ �̹� �����ϴ� ���
			JOptionPane.showMessageDialog(this, "�̹� �ش������� �����մϴ�. �ٸ� �̸����� �õ��� �ֽñ� �ٶ��ϴ�!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// ������ ���� ������ �ҷ��� ����ڰ� �Է��� ���� �̸����� ����
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(savefile), "UTF-8"));
			
			int c = 0;
			while((c = br.read()) != -1) 
				bw.write(c);
			br.close();
			bw.close();
			JOptionPane.showMessageDialog(this, "����Ϸ�!", "Success", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "���� ����!", "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
	// �̺�Ʈ ó�� �Լ�
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		switch(s) {
		case "�ٸ� �̸����� ����":
			chooser.setDialogTitle("�ٸ� �̸����� ����");
			
			//���� ���̾�α� ���
			int choose = chooser.showSaveDialog(this);
			if(choose != JFileChooser.APPROVE_OPTION) { // Ȯ�� ��ư�� ���������� ������ ���� ���(����ڰ� â�� ������ �ݾҰų� ��ҹ�ư�� ���� ���)
				JOptionPane.showMessageDialog(null, "���� ������ġ�� ���õ��� �ʾҽ��ϴ�.");
				return;
			}
			this.saveFile(mode);
			break;
		case "����":
			System.exit(0);
			break;
		case "�������":
			showStat(stat_text);
			break;
		case "���α׷� ����":
			showDesc();
			break;
		case "���α׷� ����":
			showMenual();
			break;
		case "�������� ���ư���":
			new MainGUI(color_select);
			this.dispose();
			break;
		}
	}
}