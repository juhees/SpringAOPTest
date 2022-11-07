package kr.kwangan2.aop.advice;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Aspect //해당클래스의 객체가 aspect를 구현한 것임으로 나타내기 위해서 사용
@Log4j
@Component //스프링에서 bean으로 인식하기 위해 사용
public class LogAdvice {
	
	/*
	 * 		 AspectJ Expression Language (AspectJ EL)
	 * 		=> Advice가 적용될 JoinPoint에 대한 설정 문법
	 * 		
	 * 		* : all 
	 * 		.. : 0개이상(패키지에 사용할때는 하위패키지 모두)
	 * 		[] : 생략가능
	 * 		|| : or
	 * 		&& : and
	 * 		! : not
	 * 		() : 인자 없음
	 * 		(*) : 인자 1개
	 * 		(*,*) : 인자 2개
	 * 		(..) : 인자 0개 이상
	 * 
	 */
	
	@Before("execution(* kr.kwangan2.aop.service.SampleService*.*(..))") //AspectJ의 표현식
										//advice와 관련된 어노테이션들은 내부적으로 pointcut을 지정한다.
	public void logBefore() {
		log.info("======================");
	}
	
	@Before("execution(* kr.kwangan2.aop.service.SampleService*.doAdd(String, String))&& args(str1, str2)") //pointcut 설정
	public void logParam(String str1, String str2) {
		log.info("str1: " + str1);
		log.info("str2: " + str2);
	}
	
	@AfterThrowing(
			pointcut="execution(* kr.kwangan2.aop.service.SampleService*.*(..))"
			,throwing="exception"
		)
	public void logException(Exception exception) {
		log.info("발생한 예외 ===>" + exception);
	}
	
	@Around("execution(* kr.kwangan2.aop.service.SampleService*.*(..))")
	public Object logTime(ProceedingJoinPoint pjp) {
		
		long start = System.currentTimeMillis();
		
		log.info("타겟===>" + pjp.getTarget());
		log.info("param: "+Arrays.toString(pjp.getArgs()));
		
		//invoke method
		Object result = null;
		
		try {
			result = pjp.proceed();
		}catch (Throwable e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		
		log.info("Time: " + (end - start));
		
		return result;
	}
	

}
