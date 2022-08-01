package edu.kh.comm.common.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
@Aspect
public class AfterAspect {
	
	private Logger logger = LoggerFactory.getLogger(AfterAspect.class);
	
	@After("CommonPointcut.implPointcut()")
	public void serviceEnd(JoinPoint jp) {
	

		// jp.getTarget() : aop가 적용된 객체(각종 ServiceImpl)[의 클래스 정보(설계도) ]
		String className = jp.getTarget().getClass().getSimpleName(); // 간단한 클래스명{패키지명 제외}

		// jp.getSignature() : 수행되는 메서드 정보[실행되는 메서드 이름만 얻어오겠다.]
		String methodName = jp.getSignature().getName();

		
		String str = "end :" + className + " - " + methodName + "\n";
		// Start : MemberServiceImpl - login
		
		str += "--------------------------------------------------------------------------------\n";
		
		logger.info(str);

		
	}

}
