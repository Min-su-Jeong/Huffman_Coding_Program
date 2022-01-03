package File;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;

public class Writer {
	private int buf = 0, pos = 0; // ��Ʈ ���� ���⸦ ���� ����
	private StringBuffer sb = new StringBuffer(); // �����̸��� �ٷ�� ���� ��ü
	
	// ������ Encoding �� ���� ���� �Լ�
	public void fileWriteEnc(String filepath, String enc, int bitsize, HashMap<Character, String> table) {
		BufferedWriter bw = null;
		try {
			// '���� �̸�(zip).txt' �������� ���� �� ���� ����
			sb.append(filepath);
			sb.delete(sb.length()-4, sb.length());
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sb + "(zip).txt"), "UTF-8"));
			
			// ������ ���̺� ����
			Character key = null;
			Iterator<Character> iter = table.keySet().iterator();
			bw.write(Integer.toString(bitsize)+'\n'); // �� ��Ʈ �� ���� ù������ ����
			while(iter.hasNext()) {
				key = iter.next();
				if((int)key == 9)
					bw.write("Tap");
				else if((int)key == 10)
					bw.write("Newline");
				else if((int)key == 13)
					bw.write("Enter");
				else if((int)key == 32)
					bw.write("Space");
				else
					bw.write(key); // ����(Character)
				bw.write(":"); // ���ڿ� �ڵ尪 ������
				bw.write(table.get(key)); // �ڵ尪(String)
				bw.newLine();
			}
			bw.write("��"); // ������ �ڵ�ǥ�� ���� ���� ������
			bw.newLine();
			
			// ������ ���� ������ bit ������ ����
			for (int i=0; i < enc.length(); i++) {
				char c = enc.charAt(i);
				if (c == '1')
					buf |= (64 >>> pos); // pos ũ�⸸ŭ shift ���� �� ��Ʈ OR ����
				pos++;
				if (pos == 7) { // 8��Ʈ�� ä�� ���� �� ���� ���� �� �ʱ�ȭ
					bw.write(buf);
					bw.flush();
					buf = 0;
					pos = 0;
				}
				if(i  == enc.length()-1) // �������� ��� ���� ����
				{
					if(pos == 0)
						return;
					bw.write(buf);
					bw.flush();
					buf = 0;
					pos = 0;
					return;
				}
			}
		} catch (IOException e) { 
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) { 
				e.printStackTrace(); 
			}
		}
	}
	// ������ Decoding �� ���� ���� �Լ�
	public void fileWriteDec(String filepath, String dec) {
		BufferedWriter bw = null;
		try {
			// '���� �̸�(zip).txt' �������� ���� �� ���� ����
			sb.append(filepath);
			sb.delete(sb.length()-9, sb.length());
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sb + "(unzip).txt"), "UTF-8"));
			bw.write(dec); // ���ڵ��� ���� ���� ����
		} catch (IOException e) { 
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) { 
				e.printStackTrace(); 
			}
		}
	}
}