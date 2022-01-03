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
	private int color_select; // �׸� ��
	private Container c; // �����̳� ��ü
	private JFileChooser chooser = new JFileChooser(); // ���� ����
	private JLabel la = new JLabel("Huffman Coding");
	private JLabel load = new JLabel("�ҷ��� ����");
	private JTextField tf = new JTextField();
	private JButton encode = new JButton("����");
	private JButton decode = new JButton("��������");
	private String
	filepath = "",
	DESC = "������ �ڵ�(Huffman coding)�� �ڷ� ������ ���� �����ǰ� �������� ��� ���� �ϳ��̸� \r\n"
			+ "���ս� ���࿡ ���̴� ��Ʈ���� ��ȣȭ�� ��������, ������ ������ ���� �󵵿� ���� �ٸ� ������ ��ȣ�� ����ϴ� �˰����Դϴ�.\r\n"
			+ "�� ���α׷��� �ؽ�Ʈ ������ ���� �� ���� ����� �����ϸ� ������� ������� �� ��� ���ϵ��� �����մϴ�.\r\n",
	MENUAL = "1. '����-���� �ҷ�����' �޴��� ���� ���� �Ǵ� ���������� �ؽ�Ʈ ������ �ҷ��ɴϴ�.\r\n"
			+ "2. ������ �ҷ��� ��, '����' �Ǵ� '��������' ��ư�� Ŭ���մϴ�.\r\n"
			+ "3. ���� ���� ����� �Բ� ���� �Ǵ� ���������� ����� ȭ�鿡 ��µǸ� �ش� ������ �ڵ����� �˴ϴ�.\r\n"
			+ "    �� ��, �ٸ� ���ϸ����� ����Ǳ⸦ ���Ͻø� '����-�ٸ� �̸����� ����'�޴��� ������ �ֽñ� �ٶ��ϴ�.\r\n"
			+ "4. ����� ���� �ڼ��� ������ ���� �����ôٸ� '����-�������'�޴��� �����Ͽ� �ֽñ� �ٶ��ϴ�.\r\n"
			+ "5. ���ο� �۾� ������ ���Ͻ� ���, '�������� ���ư���' ��ư�� �����ֽñ� �ٶ��ϴ�.";
	
	public MainGUI(int color) {
		// ������ ����
		setTitle("Huffman Coding");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		c.setLayout(null);
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
	
		// �׸� �� ����
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
		
		// �޴��� �ޱ�
		createMenu();
				
		// ��ü ��ġ, ũ��, ��Ʈ ����
		la.setFont(new Font("Serif", Font.BOLD, 35));
		la.setBounds(120, 0, 400, 100);
		load.setBounds(140, 60, 100, 100);
		tf.setEditable(false);
		tf.setColumns(10);
		tf.setBounds(140, 125, 205, 20);
		encode.setBounds(140, 150, 100, 100);
		decode.setBounds(245, 150, 100, 100);
		
		// ��ư ��Ȱ��ȭ �� �̺�Ʈ ������ �ޱ�
		encode.setEnabled(false);
		decode.setEnabled(false);
		encode.addActionListener(this);
		decode.addActionListener(this);
		
		// �����̳ʿ� �߰�
		c.add(la);
		c.add(load);
		c.add(tf);
		c.add(encode);
		c.add(decode);
		
		// ������ ��ġ, ũ��, ���̱� �� ����
		setLocation(res.width/3, res.height/3);
		setSize(500, 350);
		setVisible(true);
	}
	// �޴� ���� �Լ�
	private void createMenu() {
		// �޴��� ����
		JMenuBar mb = new JMenuBar(); 
		
		// �޴� ��ü ����
		JMenu fileMenu = new JMenu("����");
		JMenu themeMenu = new JMenu("�׸�");
		JMenu helpMenu = new JMenu("����");
		
		// �޴� �ٿ� �޴� �߰�
		mb.add(fileMenu);
		mb.add(themeMenu);
		mb.add(helpMenu);
		
		// ���� �޴� ����
		JMenuItem menuItem = null;
		String[] itemTitle = { "���� �ҷ�����", "����", "Blue(Default)", "Pink", "Purple", "Green", "Gray", "Orange", "���α׷� ����", "���α׷� ����" };
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
		// �޴��ٸ� �����ӿ� ����
		setJMenuBar(mb);
	}
	// ���α׷� ���� �Լ�
	private void showDesc() { 
		JOptionPane.showMessageDialog(this, DESC, "Description", JOptionPane.QUESTION_MESSAGE);
	}
	// ���α׷� ���� �Լ�
	private void showMenual() {
		JOptionPane.showMessageDialog(this, MENUAL, "Menual", JOptionPane.QUESTION_MESSAGE);
	}
	// ���� �ҷ����� �Լ�
	private void fileLoad() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"�ؽ�Ʈ ����(.txt)", // ���� ����
				"txt","txt(zip)","txt(unzip)"); //���� ���̾�α׿� ���� ���� ����
		chooser.setFileFilter(filter); //���� ���̾�α׿� ���� ���� ����
		
		//���� ���̾�α� ���
		int choose = chooser.showOpenDialog(null);
		if(choose != JFileChooser.APPROVE_OPTION) { // Ȯ�� ��ư�� ���������� ������ ���� ���(����ڰ� â�� ������ �ݾҰų� ��ҹ�ư�� ���� ���)
			JOptionPane.showMessageDialog(null, "������ ���õ��� �ʾҽ��ϴ�.");
			chooser = new JFileChooser();
			return;
		}
		
		//����ڰ� ������ �����ϰ� "����"��ư�� ���� ���
		String filePath = chooser.getSelectedFile().getPath(); // ���� ��θ� ��������
		String fileName = chooser.getSelectedFile().getName(); // ���� �̸��� ��������
		
		// ���� ������ Ȯ���Ͽ� ����/�������� ��ư Ȱ��ȭ
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
	// �̺�Ʈ ó�� �Լ�
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		switch(s) {
		case "���� �ҷ�����":
			this.fileLoad();
			break;
		case "����":
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
		case "���α׷� ����":
			showDesc();
			break;
		case "���α׷� ����":
			showMenual();
			break;
		case "����":
			HuffmanTree treeE = new HuffmanTree(filepath, 0);
			if(treeE.getError()) // ���� ���� ��(��� â ����� �ʱ�)
				new MainGUI(color_select);
			else
				new ResultGUI(0, color_select, treeE.getSrcText(), treeE.getEncText(), treeE.getStatText(), filepath);
			this.dispose();
			break;
		case "��������":
			HuffmanTree treeD = new HuffmanTree(filepath, 1);
			if(treeD.getError()) // �������� ���� ��(��� â ����� �ʱ�)
				new MainGUI(color_select);
			else 
				new ResultGUI(1, color_select, treeD.getSrcText(), treeD.getDecText(), treeD.getStatText(), filepath);
			this.dispose();
			break;
		}
	}
}