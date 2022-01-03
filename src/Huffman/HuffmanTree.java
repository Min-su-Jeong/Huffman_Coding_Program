package Huffman;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import javax.swing.JOptionPane;
import File.Reader;
import File.Writer;

// 우선순위 큐에 사용되는 노드 클래스
class Node {
	public char ch; // 하나의 문자
	public int freq; // 빈도수
	public Node left, right; // 왼쪽, 오른쪽 노드
}

// 빈도수 측정 클래스(우선순위 큐 정렬)
class FreqComparator implements Comparator<Node> {
	public int compare(Node n1, Node n2) {
		int freq1 = n1.freq;
		int freq2 = n2.freq;

		return freq1 - freq2;
	}
}

public class HuffmanTree {
	private boolean error = false; // 파일 압축 결과 변수
	private int num = 0, bitsize = 0;
	private Reader read = new Reader();
	private Writer write = new Writer();
	private String enc_text = "", dec_text = "", stat_text = "";
	private StringBuffer sb = new StringBuffer();
	private HashMap<Character, String> table = new HashMap<Character, String>(); // 허프만 테이블 저장을 위한 해시맵 객체
	private PriorityQueue<Node> queue = new PriorityQueue<Node>(500, new FreqComparator()); // 우선순위 큐 객체

	public HuffmanTree(String filepath, int mode)  {
		if (mode == 0) { // 압축을 선택한 경우
			double freq_total = 0.0, avg_bit = 0.0;
			long src_filesize = new File(filepath).length();
			long startTimeE = System.currentTimeMillis(); // 압축시간 측정 시작
			HashMap<Character, Integer> checkE = read.fileReadEnc(filepath);

			// 모든 노드들을 우선순위 큐에 삽입
			for(Character c : checkE.keySet()) {
				Node tmp = new Node();
				tmp.ch = c;
				tmp.freq = checkE.get(c);
				queue.add(tmp);
				num++;
				freq_total += tmp.freq;
			}
			// 허프만 트리 생성
			Node root = HuffTree(num);

			// 순회를 통해 각 문자에 대한 허프만 코드 생성
			Traversal(root, new String());

			// 파일 내용 Encoding
			String text = read.getText();
			for(int i=0; i < text.length(); i++)
				enc_text += table.get(text.charAt(i));
			write.fileWriteEnc(filepath, enc_text, bitsize, table);
			double endTimeE = System.currentTimeMillis(); // 압축시간 측정 종료

			// 압축된 파일 사이즈 읽기
			sb.append(filepath);
			sb.delete(sb.length()-4, sb.length());
			sb.append("(zip).txt");
			long enc_filesize = new File(sb.toString()).length();

			// 문자당 평균 비트 수 구하기
			for(Character c : checkE.keySet())
				avg_bit += (table.get(c).length() * (checkE.get(c) / freq_total));

			// 통계정보 변수에 저장
			stat_text += ("압축 시간: " + ((endTimeE-startTimeE)/1000.0) + "s\n") +
				("원본파일 크기: " + src_filesize + "byte\n") + 
				("압축파일 크기: " + enc_filesize + "byte\n") +
				("압축률: " + (String.format("%.3f", (1 - (double)enc_filesize / src_filesize) * 100)) + "%\n") + 
				("문자 당 평균 비트 수: " + String.format("%.3f",avg_bit));

			// 원본 파일보다 압축 파일 크기가 같거나 더 큰 경우
			if (src_filesize <= enc_filesize) {
				JOptionPane.showMessageDialog(null, "압축에 실패하였습니다!\n원본 파일보다 "+(enc_filesize-src_filesize)+"byte만큼 크기가 증가하였습니다.", "Error", JOptionPane.ERROR_MESSAGE);
				error = true;
			}
		}
		if (mode == 1) { // 압축해제를 선택한 경우
			long startTimeD = System.currentTimeMillis(); // 압축해제시간 측정 시작
			dec_text = read.fileReadDec(filepath);
			write.fileWriteDec(filepath, dec_text);
			double endTimeD = System.currentTimeMillis(); // 압축해제시간 측정 종료

			// 압축된 파일 사이즈 읽기
			sb.append(filepath);
			sb.delete(sb.length()-9, sb.length());
			sb.append(".txt");
			long src_filesize = new File(sb.toString()).length();
			sb.delete(sb.length()-4, sb.length());
			sb.append("(unzip).txt");
			long dec_filesize = new File(sb.toString()).length();

			// 통계정보 변수에 저장
			stat_text += ("복원 시간: " + ((endTimeD-startTimeD)/1000.0) + "s\n") +
				("원본파일 크기: " + src_filesize + "byte\n") + 
				("복원파일 크기: " + dec_filesize + "byte\n") +
				("복원율: " + (String.format("%.3f", ((double)dec_filesize / src_filesize) * 100)) + "%\n");

			// 원본 파일과 복원 파일과의 복원률이 99% 미만인 경우
			if (((double)dec_filesize / src_filesize) * 100 < 99) {
				JOptionPane.showMessageDialog(null, "압축해제에 실패하였습니다!\n원본 파일과 압축해제된 파일의 용량이 다릅니다.", "Error", JOptionPane.ERROR_MESSAGE);
				error = true;
			}
		}
	}
	// 문자 빈도 수에 따른 트리 생성 메소드
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
	// 트리 순회 메소드
	public void Traversal(Node n, String s) {
		if (n == null)
			return;
		Traversal(n.left, s + "0");
		Traversal(n.right, s + "1");
		if(n.ch != '\0') {
			table.put(n.ch, s);
			bitsize += (n.freq * s.length()); // 총 비트 수 계산
		}
	}
	// 원본 파일 내용 getter
	public String getSrcText() {
		return read.getText();
	}
	// Encoding된 파일 내용 getter
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
	// Decoding된 파일 내용 getter
	public String getDecText() {
		return dec_text;
	}
	// 통계정보 내용 getter
	public String getStatText() {
		return stat_text;
	}
	// 압축/압축해제 결과 getter
	public boolean getError() {
		return error;
	}
}