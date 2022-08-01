package edu.kh.comm.common.aop;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.kh.comm.member.model.vo.Member;

@Component
@Aspect
public class BeforeAspect {

	private Logger logger = LoggerFactory.getLogger(BeforeAspect.class);

	// JoinPoint : advice가 적용될 수 있는 후보

	// JoinPoint 인터페이스 :
	// advice가 적용되는 target Object (ServiceImpl)의
	// 정보, 전달되는 매개변수, 메서드, 반환 값 예외 등을 얻을 수 있는 메서드를 제공한다

	// (주의사항) JoinPoint 인터페이스는 항상 첫번째 매개변수로 작성 되어야 한다!

	@Before("CommonPointcut.implPointcut()")
	public void serviceStart(JoinPoint jp) {
		String str = "--------------------------------------------------------------------------------\n";

		// jp.getTarget() : aop가 적용된 객체(각종 ServiceImpl)[의 클래스 정보(설계도) ]
		String className = jp.getTarget().getClass().getSimpleName(); // 간단한 클래스명{패키지명 제외}

		// jp.getSignature() : 수행되는 메서드 정보[실행되는 메서드 이름만 얻어오겠다.]
		String methodName = jp.getSignature().getName();

		// jp.getArgs() : 메서드 호출 시 전달된 매개변수
		String param = Arrays.toString(jp.getArgs());

		str += "Start : " + className + " - " + methodName + "\n";
		// Start : MemberServiceImpl - login

		str += "매개변수 : " + param + "\n";

		try {

			// HttpServletRequest 얻어오기
			// 단, 스프링 스케줄러 동작 시 예외 발생 (스케줄러는 요청 객체가 존재하지 않음)
			HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

			Member loginMember = (Member) req.getSession().getAttribute("loginMember");

			// ip : xxx.xxx.xxx.xxx(email : test@naver.com)
			str += "ip :" + getRemoteAddr(req);

			if(loginMember != null) { // 로그인 상태인 경우
				str += "(email :" + loginMember.getMemberEmail() + ")";
				
			}
			
		} catch (Exception e) {

			str +="[스케줄러 동작]";

		}

		logger.info(str);

	}

	  public static String getRemoteAddr(HttpServletRequest request) {

	        String ip = null;

	        ip = request.getHeader("X-Forwarded-For");

	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("Proxy-Client-IP"); 
	        } 

	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("WL-Proxy-Client-IP"); 
	        } 

	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("HTTP_CLIENT_IP"); 
	        } 

	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	        }

	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("X-Real-IP"); 
	        }

	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("X-RealIP"); 
	        }

	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getHeader("REMOTE_ADDR");
	        }

	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	            ip = request.getRemoteAddr(); 
	        }

	      return ip;
		}

	}
