package Hellospring.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggerInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("");
		logger.debug("================BEGIN==============");
		logger.debug("Requst URI ===> " + request.getRequestURI());
		return super.preHandle(request, response, handler);
		/*
		 * perHandle은 컨트롤러의 메서드에 매핑된 
		 * 특정 URI를 호출 시 컨트롤러에 접근하기 전에 실행되는 메서드 
		 * logger 이용하여 콘솔에 출력하여 쉽게 파악하도록 한다.
		 */
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("=================END===============");
		System.out.println("");
		
		/*
		 * postHandle은 컨트롤러를 경유하고 
		 * 화면으로 결과를 전달하기 전에 실행되는 메서드
		 */
	}

}
