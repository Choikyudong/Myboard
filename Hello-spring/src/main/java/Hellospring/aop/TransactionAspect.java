package Hellospring.aop;

import java.util.Collections;
import java.util.List;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransactionAspect {
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	//DBConfiguration 클래스에 빈으로 등록한 객체
	
	private static final String EXPRESSION = "execution(* Hellospring..service.*Impl.*(..))";
	//AOP의 포인트컷
	
	@Bean
	public TransactionInterceptor transactionAdvice() {
		
		List<RollbackRuleAttribute> rollbackRules = Collections.singletonList(new RollbackRuleAttribute(Exception.class));
		/*
		 * 트랜잭션에서 롤백을 수행하는 규칙 RollbackRuleAttribute 생성자의 인자로 
		 * Exception 클래스를 지정했는데 자바의 모든
		 * 예외는 Exception 클래스를 상속받디 때문에 
		 * 어떤 예외가 발생을 해도 무조건 롤백이 수행된다.
		 */
		
		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
		transactionAttribute.setRollbackRules(rollbackRules);
		transactionAttribute.setName("*");
		//트랜잭션의 이름을 설정
		
		MatchAlwaysTransactionAttributeSource attributeSource = new MatchAlwaysTransactionAttributeSource();
		attributeSource.setTransactionAttribute(transactionAttribute);
		
		return new TransactionInterceptor(transactionManager, attributeSource);
	}
	
	@Bean
	public Advisor transactionAdvisor() {
		
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(EXPRESSION);
		/*
		 * poincut은 AOP의 포인트컷으로 
		 * EXPRESSION에 지정한 ServiceImpl 클래스의 
		 * 모든 메서드를 대상으로 설정
		 */
		return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
	}
}
