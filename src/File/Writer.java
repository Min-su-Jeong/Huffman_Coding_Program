package File;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;

public class Writer {
	private int buf = 0, pos = 0; // 비트 단위 쓰기를 위한 변수
	private StringBuffer sb = new StringBuffer(); // 파일이름을 다루기 위한 객체
	
	// 허프만 Encoding 시 파일 쓰기 함수
	public void fileWriteEnc(String filepath, String enc, int bitsize, HashMap<Character, String> table) {
		BufferedWriter bw = null;
		try {
			// '파일 이름(zip).txt' 형식으로 지정 및 파일 쓰기
			sb.append(filepath);
			sb.delete(sb.length()-4, sb.length());
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sb + "(zip).txt"), "UTF-8"));
			
			// 허프만 테이블 쓰기
			Character key = null;
			Iterator<Character> iter = table.keySet().iterator();
			bw.write(Integer.toString(bitsize)+'\n'); // 총 비트 수 정보 첫번쨰에 저장
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
					bw.write(key); // 문자(Character)
				bw.write(":"); // 문자와 코드값 구분자
				bw.write(table.get(key)); // 코드값(String)
				bw.newLine();
			}
			bw.write("√"); // 허프만 코드표와 파일 내용 구분자
			bw.newLine();
			
			// 허프만 압축 내용을 bit 단위로 쓰기
			for (int i=0; i < enc.length(); i++) {
				char c = enc.charAt(i);
				if (c == '1')
					buf |= (64 >>> pos); // pos 크기만큼 shift 연산 후 비트 OR 연산
				pos++;
				if (pos == 7) { // 8비트가 채워 졌을 때 파일 쓰기 후 초기화
					bw.write(buf);
					bw.flush();
					buf = 0;
					pos = 0;
				}
				if(i  == enc.length()-1) // 마지막인 경우 전부 삽입
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
	// 허프만 Decoding 시 파일 쓰기 함수
	public void fileWriteDec(String filepath, String dec) {
		BufferedWriter bw = null;
		try {
			// '파일 이름(zip).txt' 형식으로 지정 및 파일 쓰기
			sb.append(filepath);
			sb.delete(sb.length()-9, sb.length());
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sb + "(unzip).txt"), "UTF-8"));
			bw.write(dec); // 디코딩된 파일 내용 쓰기
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