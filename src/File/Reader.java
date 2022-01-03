package File;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;

public class Reader {
	private BufferedReader br = null;
	private int bitsize = 0, bit = 0, buf = 0, pos = 7; // 8bit ���� �б⸦ ���� ����
	private String text = "", dec_text = ""; // ���� ������ �����ϱ� ���� ����
	private HashMap<Character, Integer> check = new HashMap<Character, Integer>(); // �� ���ڿ� ���� �󵵼� üũ ����
	private HashMap<String, String> table = new HashMap<String, String>(); // �ؽø��� ���� ������ ���̺� ���� ����
	
	// ������ Encoding �� ���� �б� �Լ�
	public HashMap<Character, Integer> fileReadEnc(String filepath) {
		check.clear();
		text = "";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
			
			int read = 0;
			while ((read = br.read()) != -1) { // �� ���ھ� �б�(�о�� ���� ������ -1 ��ȯ)
				char tmp = (char)read; // ���ڸ� ���ڷ� ��ȯ
				// �󵵼� ���
				if(check.containsKey(tmp)) // ���� ���ڰ� �ؽøʿ� ����ִ� ��� ũ�� 1 ����
					check.put(tmp,  check.get(tmp) + 1);
				else // ���ڰ� ó�� ���ԵǴ� ��� ũ�� 1�� ����
					check.put(tmp, 1);
				text += tmp;
			}
		} catch (IOException e) { 
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) { 
				e.printStackTrace(); 
			}
		}
		return check;
	}
	// ������ Decoding �� ���� �б� �Լ�
	public String fileReadDec(String filepath) {
		check.clear();
		text = "";
		String[] token;	
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
			String s = br.readLine(); // �� ���� �б� ���� ����
			bitsize = Integer.valueOf(s); // ù�ٿ� �����ִ� ��Ʈũ�� ����(������ ���ڵ� ���̸� �˱� ����)
			text += (s+'\n');
			
			// ������ �ڵ�ǥ �о �ؽøʿ� ����
			while(!(s = br.readLine()).contains("��")) {
				text+=(s+"\n");
				token = s.split(":");
				if(token[0].equals("Tap"))
					table.put("\t", token[1]);
				else if(token[0].equals("Newline"))
					table.put("\n", token[1]);
				else if(token[0].equals("Enter"))
					table.put("\r", token[1]);
				else if(token[0].equals("Space"))
					table.put(" ", token[1]);
				else if(token.length == 3)
					table.put(":", token[2]);
				else
					table.put(token[0], token[1]);
			}
			text += (s+'\n');
			
			// binary �����͸� ��Ʈ ������ �о� �����ϱ�
			int textcnt = 0;
			String bitcode = "";
			
			while((bit = this.readBit()) != -1) {
				if(textcnt == bitsize) // ���� �ؽ�Ʈ ���̿� bitsize�� �������� ���
					break;
				bitcode += bit;
				textcnt++;
				for (Entry<String, String> entry : table.entrySet()) {
					if(entry.getValue().equals(bitcode)) {
						dec_text += entry.getKey().toString();
						bitcode = "";
						break;
					}
				}
			}
		} catch (IOException e) { 
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) { 
				e.printStackTrace(); 
			}
		}
		return dec_text;
	}
	// ��Ʈ ���� �б� �Լ�
	public int readBit() {
		if(pos == 7) {
			try {
				buf = br.read();
				if(buf == -1)
					return -1;
			} catch (IOException e) {
				e.printStackTrace();
			}
			text += (char)buf;
			pos = 0;
		}
		if ((buf & (64 >>> pos)) == 0) {
			pos++;
			return 0;
		}
		else {
			pos++;
			return 1;
		}
	}
	// ���� ���� getter
	public String getText() {
		return text;
	}
}

