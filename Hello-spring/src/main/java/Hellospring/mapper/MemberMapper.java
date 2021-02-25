package Hellospring.mapper;

import org.apache.ibatis.annotations.Mapper;

import Hellospring.domain.MemberDTO;

@Mapper
public interface MemberMapper {

	public int saveMember(MemberDTO params);
	//회원가입용 쿼리

	public MemberDTO signMember(MemberDTO params);
	//로그인
	
}
