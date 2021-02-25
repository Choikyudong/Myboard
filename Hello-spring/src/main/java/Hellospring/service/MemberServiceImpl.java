package Hellospring.service;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Hellospring.domain.MemberDTO;
import Hellospring.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
/*롬복에서 제공하는 애너테이션으로, @Autowired나 @Inject를 대체하는 기능
  *스프링에서 권장하는 생성자 주입 방식으로 처리 해준다고 한다.
  *이렇게 풀어준다고 한다.. final 키워드를 무조건 붙여야 한다고 한다..
  *또한 @SpringBootTest에서는 사용불가능!
  *
  public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
   this.memberMapper = memberMapper;
   this.passwordEncoder= passwordEncoder;
 } */

public class MemberServiceImpl implements MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	private final SqlSessionTemplate sql;
	
	@Override
	public boolean saveMember(MemberDTO params) {
		
		if(StringUtils.isNotBlank(params.getPwd())) {
			String SpringPwd = passwordEncoder.encode(params.getPwd());
			params.setPwd(SpringPwd);
		}
		
		return memberMapper.saveMember(params) > 0;
	}
	
	@Override
	public MemberDTO signMember(MemberDTO params) {
		
		MemberDTO parmas = memberMapper.signMember(params);
		String namespace = "Hellospring.mapper.MemberMapper";
		MemberDTO result = sql.selectOne(namespace + ".signMember", parmas);
		return result;
	}
	
}
