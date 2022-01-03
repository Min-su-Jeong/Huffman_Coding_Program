package File;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;

public class Reader {
	private BufferedReader br = null;
	private int bitsize = 0, bit = 0, buf = 0, pos = 7; // 8bit 단위 읽기를 위한 변수
	private String text = "", dec_text = ""; // 파일 내용을 저장하기 위한 변수
	private HashMap<Character, Integer> check = new HashMap<Character, Integer>(); // 각 문자에 대한 빈도수 체크 변수
	private HashMap<String, String> table = new HashMap<String, String>(); // 해시맵을 통한 허프만 테이블 저장 변수
	
	// 허프만 Encoding 시 파일 읽기 함수
	public HashMap<Character, Integer> fileReadEnc(String filepath) {
		check.clear();
		text = "";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
			
			int read = 0;
			while ((read = br.read()) != -1) { // 한 글자씩 읽기(읽어올 값이 없으면 -1 반환)
				char tmp = (char)read; // 숫자를 문자로 변환
				// 빈도수 계산
				if(check.containsKey(tmp)) // 현재 문자가 해시맵에 들어있는 경우 크기 1 증가
					check.put(tmp,  check.get(tmp) + 1);
				else // 문자가 처음 삽입되는 경우 크기 1로 설정
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
	// 허프만 Decoding 시 파일 읽기 함수
	public String fileReadDec(String filepath) {
		check.clear();
		text = "";
		String[] token;	
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
			String s = br.readLine(); // 한 라인 읽기 위한 변수
			bitsize = Integer.valueOf(s); // 첫줄에 적혀있는 비트크기 저장(허프만 인코딩 길이를 알기 위함)
			text += (s+'\n');
			
			// 허프만 코드표 읽어서 해시맵에 저장
			while(!(s = br.readLine()).contains("√")) {
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
			
			// binary 데이터를 비트 단위로 읽어 복원하기
			int textcnt = 0;
			String bitcode = "";
			
			while((bit = this.readBit()) != -1) {
				if(textcnt == bitsize) // 읽은 텍스트 길이와 bitsize와 같아지는 경우
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
	// 비트 단위 읽기 함수
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
	// 원본 내용 getter
	public String getText() {
		return text;
	}
}

