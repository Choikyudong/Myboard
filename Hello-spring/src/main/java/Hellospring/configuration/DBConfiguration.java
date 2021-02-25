package Hellospring.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@PropertySource("classpath:/application.properties")
//properties의 파일 위치를 지정한다.
@EnableTransactionManagement
//스프링에서 제공하는 트랜잭션을 활성화하는 애너테이션
@Configuration
//스프링은 해당 애너테이션이 지정된 클래스를 자바 기반 설정파일로 인식한다.
public class DBConfiguration {

	@Autowired
	//bean으로 등록된 인스턴스(쉽게말하면 객체)를 클래스에 주입하는 용도 @resource, @inject등이 존재
	private ApplicationContext applicationContext;
	/*
	 * ApplicationContext는 스프링 컨테이너 중 하나로 bean의 생성과 사용, 관계,
	 * 생명주기 등을 관리한다.
	 */

	@Bean
	//해당 애너테이션이 지정된 객체는 컨테이너가 관리를 한다.
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	/*
	 * 인자에 prefix 속성을 지정할 수 있다(prefix란 접두사, 머리를 의미한다)
	 * 지정된 파일에서 spring.datasource.hikari로 시작되는
	 * 설정을 읽어서 메서드에 매핑(바인딩)한다.
	 * 현재 설정에서는 application.properties에 해당되는 설정을 모두 가져옴
	 */
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	} //hikariconfig란 커넥션 풀 라이브러리 중 하나

	@Bean
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig());
		/*
		 * 데이터 소스 객체를 생성한다.
		 * 순수 JDBC는 sql을 실행할 때마다 커넥션을 맺고 끊는 I/O 작업을 하는데
		 * 이러한 작업이 리소스를 많이 먹는다고 한다. 그리고 해결을 위해 커넥션 풀이 등장
		 * 커넥션 풀이란 커넥션 객체를 생성하고, 데이터베이스에 접근하는 사용자에게 미리 생성한
		 * 커넥션을 제공하고 다시 돌려받는 방법이다
		 * 그리고 데이터 소스는 커넥션 풀을 지원하기 위한 인터페이스이다.
		 */
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**/*Mapper.xml"));
		//해당 라인은 XML Mapper를 인식하는 역할을 한다.
		factoryBean.setTypeAliasesPackage("Hellospring.*");
		/*
		 * XML에서 parameterType과 resultType은 풀 패키지 경로가 되야하는데
		 * 해당 메서드로 패키지 경로를 생략가능하다! * 이것도 지정가능
		 */
		factoryBean.setConfiguration(mybatisConfg());
		//96번 라인에 입력된 마이바티스 설정 bean을 설정 파일로 지정한다.
		return factoryBean.getObject();
	}
	/*
	 * SqlSessionFactory란 데이터베이스의 커넥션과 "sql 실행에 대한 모든 것"을 갖는
	 * 아주아주 중요한 역활이다. SqlSessionFactory은 마이바티스와 스프링의 연동 모듈로 사용
	 * 마이바티스 XML Mapper, 설정 파일 위치를 지정, SqlSessionFactoryBean 자체가 아닌
	 * getObject 메서드가 리턴하는 SqlSessionFactory를 생성한다.
	 */

	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}
	/*
	 * sqlSession 객체를 생성한다. 그러면 sqlSession이란 뭔가?
	 * 마이바티스 공식 문서에 다음과 같이 정의되어 있다고 한다.
	 * 1. SqlSessionTemplate은 마이바티스 스프링 연동 모듈의 핵심이다.
	 * 2. SqlSessionTemplate은 SqlSession을 구현하고, 코드에서 SqlSession을 대체하는 역할을 한다.
	 * 3. SqlSessionTemplate은 쓰레드에 안전하고, 여러 개의 DAO나 Mapper에서 공유할 수 있다.
	 * 4. 필요한 시점에 세션을 닫고, 커밋 또는 롤백하는 것을 포함한 세션의 생명주기를 관리한다.
	 * 말이 굉장히 어려운거 같은데 SqlSessionTemplate은 위의 SqlSessionFactory를 통해서 생성되고
	 * 커밋, 롤백 등 sql 실행에 필요한 모든 메서드를 갖는 객체로 생각할 수 있다.
	 */
	
	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration")
	public org.apache.ibatis.session.Configuration mybatisConfg() {
		return new org.apache.ibatis.session.Configuration();
	}
	/*
	 * application.properties에서 mybatis.configuration으로 시작하는
	 * 모든 설정을 가져와 bean으로 등록합니다.
	 */
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	} //트랜잭션 매니저를 빈으로 등록
	
}
