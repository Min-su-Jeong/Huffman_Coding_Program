package Huffman;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import javax.swing.JOptionPane;
import File.Reader;
import File.Writer;

// �켱���� ť�� ���Ǵ� ��� Ŭ����
class Node {
	public char ch; // �ϳ��� ����
	public int freq; // �󵵼�
	public Node left, right; // ����, ������ ���
}

// �󵵼� ���� Ŭ����(�켱���� ť ����)
class FreqComparator implements Comparator<Node> {
	public int compare(Node n1, Node n2) {
		int freq1 = n1.freq;
		int freq2 = n2.freq;

		return freq1 - freq2;
	}
}

public class HuffmanTree {
	private boolean error = false; // ���� ���� ��� ����
	private int num = 0, bitsize = 0;
	private Reader read = new Reader();
	private Writer write = new Writer();
	private String enc_text = "", dec_text = "", stat_text = "";
	private StringBuffer sb = new StringBuffer();
	private HashMap<Character, String> table = new HashMap<Character, String>(); // ������ ���̺� ������ ���� �ؽø� ��ü
	private PriorityQueue<Node> queue = new PriorityQueue<Node>(500, new FreqComparator()); // �켱���� ť ��ü

	public HuffmanTree(String filepath, int mode)  {
		if (mode == 0) { // ������ ������ ���
			double freq_total = 0.0, avg_bit = 0.0;
			long src_filesize = new File(filepath).length();
			long startTimeE = System.currentTimeMillis(); // ����ð� ���� ����
			HashMap<Character, Integer> checkE = read.fileReadEnc(filepath);

			// ��� ������ �켱���� ť�� ����
			for(Character c : checkE.keySet()) {
				Node tmp = new Node();
				tmp.ch = c;
				tmp.freq = checkE.get(c);
				queue.add(tmp);
				num++;
				freq_total += tmp.freq;
			}
			// ������ Ʈ�� ����
			Node root = HuffTree(num);

			// ��ȸ�� ���� �� ���ڿ� ���� ������ �ڵ� ����
			Traversal(root, new String());

			// ���� ���� Encoding
			String text = read.getText();
			for(int i=0; i < text.length(); i++)
				enc_text += table.get(text.charAt(i));
			write.fileWriteEnc(filepath, enc_text, bitsize, table);
			double endTimeE = System.currentTimeMillis(); // ����ð� ���� ����

			// ����� ���� ������ �б�
			sb.append(filepath);
			sb.delete(sb.length()-4, sb.length());
			sb.append("(zip).txt");
			long enc_filesize = new File(sb.toString()).length();

			// ���ڴ� ��� ��Ʈ �� ���ϱ�
			for(Character c : checkE.keySet())
				avg_bit += (table.get(c).length() * (checkE.get(c) / freq_total));

			// ������� ������ ����
			stat_text += ("���� �ð�: " + ((endTimeE-startTimeE)/1000.0) + "s\n") +
				("�������� ũ��: " + src_filesize + "byte\n") + 
				("�������� ũ��: " + enc_filesize + "byte\n") +
				("�����: " + (String.format("%.3f", (1 - (double)enc_filesize / src_filesize) * 100)) + "%\n") + 
				("���� �� ��� ��Ʈ ��: " + String.format("%.3f",avg_bit));

			// ���� ���Ϻ��� ���� ���� ũ�Ⱑ ���ų� �� ū ���
			if (src_filesize <= enc_filesize) {
				JOptionPane.showMessageDialog(null, "���࿡ �����Ͽ����ϴ�!\n���� ���Ϻ��� "+(enc_filesize-src_filesize)+"byte��ŭ ũ�Ⱑ �����Ͽ����ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
				error = true;
			}
		}
		if (mode == 1) { // ���������� ������ ���
			long startTimeD = System.currentTimeMillis(); // ���������ð� ���� ����
			dec_text = read.fileReadDec(filepath);
			write.fileWriteDec(filepath, dec_text);
			double endTimeD = System.currentTimeMillis(); // ���������ð� ���� ����

			// ����� ���� ������ �б�
			sb.append(filepath);
			sb.delete(sb.length()-9, sb.length());
			sb.append(".txt");
			long src_filesize = new File(sb.toString()).length();
			sb.delete(sb.length()-4, sb.length());
			sb.append("(unzip).txt");
			long dec_filesize = new File(sb.toString()).length();

			// ������� ������ ����
			stat_text += ("���� �ð�: " + ((endTimeD-startTimeD)/1000.0) + "s\n") +
				("�������� ũ��: " + src_filesize + "byte\n") + 
				("�������� ũ��: " + dec_filesize + "byte\n") +
				("������: " + (String.format("%.3f", ((double)dec_filesize / src_filesize) * 100)) + "%\n");

			// ���� ���ϰ� ���� ���ϰ��� �������� 99% �̸��� ���
			if (((double)dec_filesize / src_filesize) * 100 < 99) {
				JOptionPane.showMessageDialog(null, "���������� �����Ͽ����ϴ�!\n���� ���ϰ� ���������� ������ �뷮�� �ٸ��ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
				error = true;
			}
		}
	}
	// ���� �� ���� ���� Ʈ�� ���� �޼ҵ�
	public Node HuffTree(int n) {
		for(int i=0; i < n-1; i++) {
			Node node = new Node();
			node.left = queue.poll();
			node.right = queue.poll();
			node.freq = node.left.freq + node.right.freq;
			queue.add(node);
		}
		return queue.poll();
	}
	// Ʈ�� ��ȸ �޼ҵ�
	public void Traversal(Node n, String s) {
		if (n == null)
			return;
		Traversal(n.left, s + "0");
		Traversal(n.right, s + "1");
		if(n.ch != '\0') {
			table.put(n.ch, s);
			bitsize += (n.freq * s.length()); // �� ��Ʈ �� ���
		}
	}
	// ���� ���� ���� getter
	public String getSrcText() {
		return read.getText();
	}
	// Encoding�� ���� ���� getter
	public String getEncText() {
		Character key = null;
		String value = "", text = "";
		Iterator<Character> iter = table.keySet().iterator();
		while(iter.hasNext()) {
			key = iter.next();
			value = table.get(key);
			text += key.toString() + ":" + value + '\n';
		}
		text += enc_text;
		return text;
	}
	// Decoding�� ���� ���� getter
	public String getDecText() {
		return dec_text;
	}
	// ������� ���� getter
	public String getStatText() {
		return stat_text;
	}
	// ����/�������� ��� getter
	public boolean getError() {
		return error;
	}
}