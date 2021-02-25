package Hellospring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*	
 * 알면 좋은 상식?
 * 해당 애너테이션은 3가지 애너테이션으로 구성되있는데
 * 1. @EnableAutoConfiguration
 * 	스프링부트 개발에 필수 설정, 해당 애너테이션으로 다양한 설정이 자동으로 완료
 * 2. @ComponentScan
 * 	기존의 xml은 스프링 빈(bean)의 등록 및 스캔을 위해 수동으로 componentscan을
 * 여러개 선언하는 방식이였으나 부트에서는 해당 애너테이션으로 컴포넌트 클래스를 검색 그리고
 * 애플리케이션의 콘텍스트를 빈으로 등록해준다. 의존성 주입이 간편해졌다고 말한다.
 * 3. @Configuration
 * 	해당 애너테이션이 선언된 클래스는 자바기반 파일로 인식이 된다. xml 설정에
 * 많은 시간을 소모하지 않아도 된다.
 */

public class HelloSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloSpringApplication.class, args);
	}	//SpringApplication.run으로 웹 애플리케이션을 실행함

	/*
	 * 실행 시 localhost란 자신의 pc 127.0.0.1과 같은 의미
	 * 8080 포트는 was(톰캣)의 기본 포트 번호이다.
	 * 스프링 부트에서 was는 기본적으로 내장되어 있는 톰캣을 사용
	 * application.properties에서 server.port 속성을 이용해서
	 * 포트 번호를 지정할 수 있다.
	 */
}
