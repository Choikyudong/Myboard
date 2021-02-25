package Hellospring.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO extends CommonDTO {
	
	private String id;
	//회원 아이디
	
	private String nickname;
	//회원 닉네임
	
	private String pwd;
	//회원 비밀번호
	
	private String email;
	//이메일
	
	private int phone;
	//휴대폰번호
	
}
