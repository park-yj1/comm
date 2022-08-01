package edu.kh.comm.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(5) // @Order : advice 실행 순서 , 클수록 먼저 시작
@Component
@Aspect
public class AfterReturningAspect {

	private Logger logger = LoggerFactory.getLogger(AfterAspect.class);

	// @AfterReturning : 기존 @After + 반환 값 얻어오기 기능

	// returning="returnObj" : 반환 값을 저장할 매개변수를 지정
	
	@AfterReturning(pointcut="CommonPointcut.implPointcut()",returning="returnObj")
	public void serviceReturnValue(JoinPoint jp , Object returnObj) {
		
		logger.info("Return Value :" + returnObj);
		
		
	}

}
