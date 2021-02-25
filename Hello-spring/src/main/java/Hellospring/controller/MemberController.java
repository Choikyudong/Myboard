package Hellospring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Hellospring.constant.Method;
import Hellospring.domain.MemberDTO;
import Hellospring.service.MemberService;
import Hellospring.util.UiUtils;

@Controller
public class MemberController extends UiUtils {

	@Autowired
	private MemberService memberService;

	@Autowired
	private BCryptPasswordEncoder pwdencoder;	//스프링에서 권장하는 암호화 기법
	
	@Autowired
	private HttpSession session;		//세션을 제어하는 메서드는 해당 컨트롤러에서 많이 쓰임

	@GetMapping(value = "/member/signup.do")
	public String joinMember() {

		return "/member/signup";	//단순한 페이지 이동을 위한 컨트롤러
	}

	@PostMapping(value = "/member/signupCheck.do")
	public String SignupMember(@ModelAttribute("params") MemberDTO params, Model model) {

		try {
			boolean saveMember = memberService.saveMember(params);

			if (saveMember == false) {
				return showMessageWithRedirect("회원가입 실패", "/", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 문제 발생!", "/", Method.GET, null, model);
		} catch (Exception e) {
			return showMessageWithRedirect("시스템 문제 발생!", "/", Method.GET, null, model);
		}

		return showMessageWithRedirect("회원가입 성공", "/member/login.do", Method.GET, null, model);
	}

	@GetMapping(value = "/member/login.do")
	public String loginMember() {

		return "/member/login";
	}

	 @PostMapping(value = "/member/loginCheck.do")
	 public String loginMember(@ModelAttribute("params") MemberDTO params, HttpServletRequest
	  request, Model model, RedirectAttributes redirect) {
		 
		 MemberDTO memberLogin = memberService.signMember(params); 
		 
		 try {
			 boolean passMatch = pwdencoder.matches(params.getPwd(), memberLogin.getPwd());

			 if(memberLogin != null && passMatch) { 
				 session = request.getSession();		//로그인 정보가 일치하다면 세션생성
				 session.setMaxInactiveInterval(10*60);		//세션 시간은 10분으로
				 session.setAttribute("memberVO", memberLogin);		//memberVO라는 이름으로 memberLogin의 정보 넣음
				 System.out.println("memberLogin====>" + memberLogin);
			 } else { 
			 redirect.addFlashAttribute("msg", false);
			 return showMessageWithRedirect("Please Check your Id or Password!", 
			 			"/board/list.do", Method.GET, null, model);
			 }
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 문제 발생!", "/", Method.GET, null, model);
		} catch (Exception e) {
			return showMessageWithRedirect("시스템 문제 발생!", "/", Method.GET, null, model);
		}
		
		 session.getAttribute("memberVO");		//memberVO를 세션에 추가
		 return showMessageWithRedirect("Welcome!", "/board/list.do", Method.GET, null, model);
	  
	 }
	 
	 @GetMapping(value = "/member/logout.do")
	 public String logoutMember(Model model) {
		 session.invalidate();		//세션을 모두 제거
		
		 return showMessageWithRedirect("See you!", "/board/list.do", Method.PATCH, null, model);
	 }
}