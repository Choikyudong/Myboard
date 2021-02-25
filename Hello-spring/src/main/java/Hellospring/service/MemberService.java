package Hellospring.service;

import Hellospring.domain.MemberDTO;

public interface MemberService {

	public boolean saveMember(MemberDTO params);
	//회원 정보 저장
	
	public MemberDTO signMember(MemberDTO params);
	//회원 로그인
	
}
