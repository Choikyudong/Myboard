package Hellospring;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import Hellospring.domain.MemberDTO;
import Hellospring.service.MemberService;

@SpringBootTest
public class MemberTests {

	@Autowired
	private MemberService memberService;

	@Autowired
	private BCryptPasswordEncoder pwdencoder;
	
	@Test
	public void saveMember() {
		
		MemberDTO params = new MemberDTO();
		params.setId("테스트");
		params.setNickname("닉네임");
		params.setPwd("123!@#@!#");
		params.setEmail("테스트@테스트");
		params.setPhone(123);
		
		memberService.saveMember(params);
	}
	
	@Test
	public void passwordEncoding() {
		String password = "123";
		String test = "$2a$10$aYcwWjtiB1xOuvu6PfCpyO89imOnUM1AUpir7YCItwSO3Ne5mUWtm";
		
		assertTrue(pwdencoder.matches(password, test));
	}

}
