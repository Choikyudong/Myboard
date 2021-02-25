/* 간단 AOP 용어 요약
관점(Aspect) : 공통적으로 적용될 기능으로 부가적인 기능을 정의한 코드
			  어드바이스와 어드바이스를 적용할 곳을 정하는 포인트컷의 조합으로 만들어짐
어드바이스(Advice) : 실제로 부가적인 기능을 규현한 객체
조인 포인트(Join point) : 어드바이스를 적용할 위치를 의미
포인트컷(Point cut) : 어드바이스를 적용할 조인 포인트를 선별 또는 그 기능을 정의한 모듈
				  	정규 표현식이나 AspectJ문법을 이용
타겟(Target) : 실제로 비즈니스 로직을 수행하는 객체. 어드바이스를 적용할 대상
인트로덕션(Introduction) : 타겟에는 없는 새로운 메서드, 인스턴스 변수를 추가하는 기능
위빙(Weaving) : 포인트컷에 의해서 결정된 타겟의 조인 포인트에 어드바이스를 적용한 것 */

package Hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
/*
 * 스프링 컨테이너에 빈으로 등록하는 애너테이션
 * @Bean은 개발자가 제어할 수 없는 외부 라이브러리를 빈으로 등록할 때 사용
 * @Component는 개발자가 직접 정의한 클래스를 빈으로 등록할 때 사용
 */
@Aspect
//AOP 기능을 하는 클래스의 레벨에 지정하는 애너테이션
public class LoggerAspect {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Around("execution(* Hellospring..controller.*Controller.*(..))"
			//Hellospring의 모든 하위패키지 중 controller로 시작하는 패키지에서
			//xxxController와 같은 패턴의 이름을 가진 클래스에서 
			//파라미터가 0개 이상인 메서드를 의미
			+ " or execution(* Hellospring..service.*Impl.*(..))"
			+ " or execution(* Hellospring..mapper.*Mapper.*(..))")
	
	/* 어드바이스의 종류(위에서 사용된 @Around같은거얌)
	@Before : Target 메서드 호출 이전에 적용할 어드바이스 정의
	@AfterReturning : Target 메서드가 성공적으로 실행되고, 결과값을 반환한 뒤 적용
	@AfterThrowing : Target 메서드에서 예외 발생 이후에 적용(try/catch의 catch와 비슷)
	@After : Target 메서드에서 예외 발생에 관계없이 적용(try/catch의 finally와 비슷)
	@Around : Target 메서드 호출 이전과 이후 모두 적용 */
	
	/* execution 간단 설명
	위의 어드바이스문 안에서 execution으로 시작하는 구문은 포인트컷을 지정하는 문법으로
	execution을 제외한 명시자로는 within과 bean이 있다.
	그 중에서 execution은 접근 제어자, 리턴 타입, 타입 패턴, 메서드 등등을 조합해서
	정교한 포인트컷을 만들 수 있다. */
	
	public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
		/* ProceedingJoinPoint 인터페이스에는 다음의 메서드를 가짐
		Object[] getArgs() : 전달되는 모든 파라미터들을 Object 타입의 배열로 가져옴
		String getKind() : 해당 어드바이스의 타입을 가지고 온다
		Signature getSignature() : 실행되는 대상 객체의 메서드 정보를 가지고 옴
		Object getTarget() : 타겟 객체를 가지고 온다.
		Object getThis() : 어드바이스를 행하는 객체를 가지고 온다. */
		
		String type = "";
		String name = joinPoint.getSignature().getDeclaringTypeName();
		/* 메서드 정보는 Signature 객체에 담겨 있고
		파일명을 포함한 패키지 경로를 name에 담아
		어떤 클래스의 어떤 메서드를 호출하는지를 로그에 출력 */
		
		if(name.contains("Controller")==true) {
			
			type = "Controller ===> ";
		
		} else if(name.contains("Service")==true) {
			
			type = "ServiceImpl ===> "; 
		
		} else if(name.contains("Mapper")==true) {
			
			type = "Mapper ===> ";
		
		}
		
		logger.debug(type+name+"."+joinPoint.getSignature().getName());
		
		return joinPoint.proceed();
	}
	
}
