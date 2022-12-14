package edu.kh.comm.common.aop;

import org.aspectj.lang.annotation.Pointcut;

// 각종 Pointcut을 모아둘 클래스
public class CommonPointcut {
	
	// 회원 서비스용 Pointcut
	@Pointcut("execution( * edu.kh.comm.member..*Impl.*(..))")
	public void memberPointcut() {} // 내용 작성 X

	// 모든 ServiceImpl 클래스용 Pointcut
	@Pointcut("execution( * edu.kh.comm..*Impl.*(..))")
	public void implPointcut() {} // 내용 작성 X
	
	

}
