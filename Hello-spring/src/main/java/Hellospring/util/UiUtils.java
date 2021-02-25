package Hellospring.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import Hellospring.constant.Method;
import Hellospring.paging.Criteria;

@Controller
public class UiUtils {
	
	public String showMessageWithRedirect(
			@RequestParam(value = "message", required = false) String mesaage, 
			//message는 사용자에게 전달할 메시지를 의미
			@RequestParam(value = "redirectUri", required = false) String redirectUri, 
			//redirect할때 uri를 의미
			@RequestParam(value = "method", required = false) Method method,
			//Enum에서 선언한 HTTP 요청 메서드를 이용할때
			@RequestParam(value = "params", required = false) Map<String, Object> params, Model model) {
			//화면(view)로 전달할 파라미터
			//파라미터 개수는 페이지에 따라 달라질 수 있으므로
			//여러가지 데이터를 key, value로 담을 수 있는 map으로
			//예를들어 1번 페이지에서 5번을 글을 수정하고 나면 다시 1번 페이지로
			//돌아가야 하므로 해당 방식이 편하다고 생각한다.
		
		model.addAttribute("message", mesaage);
		model.addAttribute("redirectUri", redirectUri);
		model.addAttribute("method", method);
		model.addAttribute("params", params);
		//model들은 화면으로 파라미터를 전달하는 용도
		
		return "utils/message-redirect";
	}
	
	public Map<String, Object> getPagingParams(Criteria criteria) {
		//해당 메서드는 Criteria 클래스이 모든 멤버 변수(이전 페이지 정보)를
		//Map(key, value) 형태로 담아 리턴하는 메서드로
		//이전 페이지 정보는 페이징이 적용된 곳에서 모두 사용되기에 여기에다가 작성
		
		Map<String, Object> params = new LinkedHashMap<>();
		//LinkedHashMap과 HashMap의 차이점은 키쌍을 매핑했을때 순서이다.
		//HashMap은 순서대로 저장이 되지않고
		//LinkedHashMap은 순서대로 저장이 된다, 그래서 이전 페이지 정보는 순서를
		//정확히 넣어야 되기 때문에 LinkedHahshMap으로 사용한다.
		params.put("currentPageNo", criteria.getCurrentPageNo());
		params.put("recordsPerPage", criteria.getRecordsPerPage());
		params.put("pageSize", criteria.getPageSize());
		params.put("searchType", criteria.getSearchType());
		params.put("searchKeyword", criteria.getSearchKeyword());
		
		return params;
	}
}
