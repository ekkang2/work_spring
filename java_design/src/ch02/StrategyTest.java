package ch02;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.management.RuntimeErrorException;

public class StrategyTest {

	public static void main(String[] args) {

		// 추상화를 높이는게 좋은 코드가 될 수 있다.
		EncodingStrategy base64 = new Base64Strategy();
		EncodingStrategy normal = new NormalStrategy();
		EncodingStrategy append = new AppendStrategy();
		EncodingStrategy urlencoding = new UrlEncodingStrategy();
		
		String message = "1 2 34 _ ";
		Encoder encoder = new Encoder();
		// Base64 인코딩해주세요
		
		encoder.setEncondingStrategy(base64);
		System.out.println(encoder.getMessage(message));
		
		// 일반 텍스트로 인코딩 해주세요
		encoder.setEncondingStrategy(normal);
		System.out.println(encoder.getMessage(message));
		
		// append 텍스트로 인코딩 해주세요
		encoder.setEncondingStrategy(append);
		System.out.println(encoder.getMessage(message));
		
		// 도전 과제 - UrlEncodingStrategy 클래스를 만들고 전략 패턴을 활용해주세요
		encoder.setEncondingStrategy(urlencoding);
		System.out.println(encoder.getMessage(message));
	}

} // end of class


// 인코딩 전략
interface EncodingStrategy {
	String encode(String text);
}

// 바이트 0101010 --> new File();
// 서버측 데이터를 API --- json --> 문자열 
// Base64 인코딩
class Base64Strategy implements EncodingStrategy {

	@Override
	public String encode(String text) {
		return Base64.getEncoder().encodeToString(text.getBytes());
	}
	
}

// 일반 텍스트 전략 
class NormalStrategy implements EncodingStrategy {

	@Override
	public String encode(String text) {
		return text;
	}
}

// 문자열 ABC를 붙여서 보내라 
class AppendStrategy implements EncodingStrategy {

	@Override
	public String encode(String text) {
		return "ABC" + text;
	}
}

class UrlEncodingStrategy implements EncodingStrategy {

	@Override
	public String encode(String text) {
		
		try {
			// URLEncoder 사용해서 UTF-8 형식으로 인코딩 
			// 공백, 특수문자 등을 % 형식을 변환해서 전송할 수 있도록 한다.
			return URLEncoder.encode(text, StandardCharsets.UTF_8.toString());	
		} catch (Exception e) {
			throw new RuntimeException("인코딩 실패",  e);
		}
		
	}
	
}

// 클라이언트 클래스
class Encoder {
	
	// DI - 생성자 주입 
	// DI - 메서드 주입 setter
	
	// 행동을 할 멤버
	EncodingStrategy encodingStrategy;
	
	// 전략에 따라서 실행할 메서드가
	public String getMessage(String message) {
		return this.encodingStrategy.encode(message);
	}
	
	// 전략에 따라서 멤버 변경할 수 있는 메서드 필요
	public void setEncondingStrategy(EncodingStrategy encodingStrategy) {
		this.encodingStrategy = encodingStrategy;
	}
	
	
	
}