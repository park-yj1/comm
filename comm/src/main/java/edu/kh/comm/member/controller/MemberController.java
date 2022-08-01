package edu.kh.comm.member.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.kh.comm.member.model.service.MemberService;
import edu.kh.comm.member.model.service.MemberServiceImpl;
import edu.kh.comm.member.model.vo.Member;

// POJO 기반 프레임 워크 : 외부 라이브러리 상속X

// class : 객체를 만들기 위한 설계도 
// -> 객체로 생성 되어야지 기능 수행이 가능하다.
//--> IOC(제어의 역전, 객체 생명주기를 스프링이 관리)를 이용하여 객체를 생성 
//	*** 이 때, 스프링이 생성한 객체를 [bean]이라고 한다 ***


//bean 등록 == 스프링이 객체로 만들어서 가지고 있어라
//@Component // @Component: 해당 클래스를 bean으로 등록하라는 프로그램에게 알려주는 주석(Annotation)
@Controller // 생성된 bean이 Controller임을 명시 + bean 등록 -> 객체로 만들어주세요
@RequestMapping("/member") // @RequestMapping("/member") : localhost:8080/comm/member 이하의 요청을 처리하는 컨트롤러 
//localhost:8080/comm/member
//localhost:8080/comm/member/login
//localhost:8080/comm/member/signUp

@SessionAttributes({"loginMember"}) // @SessionAttributes 란? 
									// Model에 추가된 값의 key와 어노테이션에 작성된 값이 같으면 
									// 해당 값을 session scope로 이동시키는 역할
								
public class MemberController {
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	//memberServiceimpl의 bean을 연결 : @Autowired (자동, 줄 줄로 이어준다? 이런의미)
	@Autowired // bean으로 등록된 객체 중 타입이 같거나, 상속관계인 bean을 주입해주는 역할
	private MemberService service; //-> 의존성 주입 (DI , Dependency Injection)
	
	
	
	// Controller : 요청/응답을 제어하는 역할을 하는 클래스 
	
	// @RequestMapping: 클라이언트 요청(url)에 맞는 클래스나 or 매서드를 연결 시켜주는 어노테이션 HandlerMapping( == @RequestMapping)
	
	/*
	 	[위치에 따른 해석]
	 	-클래스 레벨 : 공통 주소 (**프론트 컨드롤러 패턴 지정**)
	 	-메서드 레벨 : 공통 주소 외 나머지 주소 
	 	
	 	단, 클레스 레벨에 @RequestMapping이 존재하지 않는다면 
	 	- 메서드 레벨(의 주소는 바뀐다) : 단독 요청 처리 주소("/member/login")
	 	 
	 	 
	 	[작성법에 따른 해석] 
	 	
	 	1) @RequestMapping("url")
	 		-> 요청 방식(GET/POST) 관계없이 url이 일치하는 요청 처리
	 	
	 	2) @RequestMapping(value="url", method = RequestMethod.GET | POST)
	 		-> 요청 방식에 따라 요청 처리
	 	
	 	
	 	** 메서드 레벨에서 GET/POST 방식을 구분하여 매핑할 경우 **
	 	@GetMapping("url) / @PostMapping("url) 사용하는 것이 일반적
	 	(메소드 레벨만 사용 가능!)
	 	
	 */
	
	
	// Argument Resolver (매개변수 해결사)라는 매개변수를 유연하게 처리해주는 해결사가 스프링에 내장되어있다!
	// https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments == 스프링 내장 사이트
	
	// 요청 시 파라미터를 얻어오는 방법 1
	
	
/*	// 로그인 
	@RequestMapping("/login") // 나머지 주소 
	public String login(HttpServletRequest req) { 
		// 요청 시 파라미터를 얻어오는 방법 1
		// - HttpServletRequest 이용
		
		String inputEmail = req.getParameter("inputEmail");
		String inputPw = req.getParameter("inpuPw");
		
		logger.debug("inpuntEmail :"+ inputEmail);
		logger.debug("inpuntPw :"+ inputPw);
		
		logger.info("로그인 요청됨");
		return "redirect:/";
		
	}*/
	
	
	// 요청 시 파라미터를 얻어오는 방법 2
	// -> @RequestParam 어노테이션 사용
	
	// @RequestParam("name속성값") + 자료형 + 변수명 
	// - 클라이언트 요청 시 같이 전달된 파라미터를 변수에 저장 
	// --> 어떤 파라미터를 변수에 저장할지는 "name속성값"을 이용해 지정 
	
	// 매개변수 지정 시 데이터 타입 파싱을 자유롭게 진행 할 수 있음 ex)String email -> int email로 변환)
	
	// [속성]
	// *value : input 태그의 name 속성값 (default)  ( 속성을 하나도 적지 않은 경우의 기본값)
	// @RequestParam("inputEmail") == (@RequestParam( value =="inputEmail")
	
	// *required : 입력된 name 속성값이 필수적으로 파라미터에 포함되어야 되는지를 지정 
	// 			  required = true /  required = false  (기본값 true)
	
	// 400 – 잘못된 요청(Bad Request) : 파라미터가 존재하지 않아 요청이 잘못됨. (요청 자체가 잘못됨) 
	
	// required = false 일때 파라미터가 없으면 null 
	
	// *defaultValue : required가 false인 상태에서 파라미터가 존재하지 않을 경우의 값을 지정 (기본값 지정) 
	// null 대신해서 [DEBUG] name :홍길동 값이 나온다 //defaultValue= "홍길동"
	
	// ** @RequestParam을 생략하지만 파라미터를 얻어오는 방법 **
	// - **name 속성값과 
	//   파라미터를 저장할 변수 이름을 동일하게 작성할떄 @RequestParam 생략 가능
	// @RequestParam("inputEmail")*/  ==>  int inputEmail, 
	
	
	
//	@RequestMapping("/login")		 
//	public String login(/*@RequestParam("inputEmail")*/ int inputEmail, 
//						@RequestParam("inputPw")String pw,
//						@RequestParam(value="inputName", required = false , defaultValue= "홍길동") String name) {  
//		
//		logger.debug("email : " + inputEmail);
//		logger.debug("pw : " + pw);
//		logger.debug("name :"  + name);
//
//		
//		// email로 숫자만 입력 받는다고 가정 
//		logger.debug(inputEmail+100+"");//debug는 String만 출력 가능하기때문에 ""
//		
//		
//		
//		return "redirect:/";// 리다이렉트 방법
//		
//	}
	
	// 요청 시 파라미터를 얻어오는 방법 3
	// -> @ModelAttribute 어노테이션 사용(url,매개변수 자리에 작성 가능)
	
	// [@ModelAttribute를 매개변수에 작성하는 경우]
	
	// @ModelAttribute VO타입 변수명 
	//-> 파라미터 중 name 속성값(index.jsp의 name)이 VO의 필드와 일치하면 
	//   해당 VO 객체의 필드에 값을 세팅 
	
	// **** @ModelAttribute를 이용해서(사용하려면) 객체에 값을 직접 담는 경우의 대한 주의사항 ****
	// 반드시 필요한 내용 !!
	// -VO 기본 생성자
	// -VO 필드에 대한 Setter
	// @ModelAttribute를 사용하려면 기본 생성자와 setter가 필요하다!
	
	// Getter는 JSP - EL 사용 시 반드시 필요!
	
	
	//@RequestMapping(value="/login",method=RequestMethod.POST) == @PostMapping("/login")
	@PostMapping("/login")
	public String login( /*@ModelAttribute*/ Member inputMember
						, Model model
						, RedirectAttributes ra
						, HttpServletResponse resp
						, HttpServletRequest req
						, @RequestParam(value="saveId", required = false)String saveId) {
													//saveId가있으면 넘어오고(true) 안넘어오면 (false)
		
		 
		
		//@ModelAttribute 생략 가능 
		// -> 커맨드 객체(@ModelAttribute가 생략된 상태에서 파라미터가 필드에 세팅된 객체를 커맨드 객체라고 한다.)
		
		
		logger.info("로그인 기능 수행됨");
		
		//아이디, 비밀번호 일치하는 회원 정보를 조회하는 Service호출후 결관 반환바기
		Member loginMember = service.login(inputMember);
			
		
		/*
		 * Model : 데이터를 맵형식 (K:V) 형태로 담아 전달하는 용도의 객체
		 * -> request, session을 대체하는 객체(두개를 다 다룰수 있음 model)
		 * 
		 * - 기본 scope : request
		 * - session scope로 변환하고 싶은 경우
		 * 	 클래스 레벨로 @SessionAttributes를 작성하면 된다.
		 */
	
		//@SessionAttributes 미작성 -> request scope
		
		if(loginMember != null) {// 로그인 성공 시 
			
			model.addAttribute("loginMember", loginMember); // == req.setAttribute("loginMember",loginMember);
			// ("key",value)로 작성해줘야 한다.
		
			// 로그인 성공시 무조건 쿠키 생성 
			// 딘, 아이디 저장 체크 여부에 따라서 쿠키의 유지 시간을 조정 
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail() );
			
			if(saveId !=null) {//아이디 저장이 체크되었을때 (쿠키)
				
				cookie.setMaxAge(60*60*24*365); // 초 단위로 지정 (1년)
				
			}else { // 체크 되지 않았을때
				
				cookie.setMaxAge(0); //0초 -> 생성 되자마자 사라짐 == 쿠키 삭제
			}
			
			//쿠키가 적용될 범위(경로) 지정
			cookie.setPath(req.getContextPath());
			
			// 쿠키를 응답 시 클라이언트에게 전달
			resp.addCookie(cookie);
			
		
		}else { // 로그인 실패시
			//model.addAttribute("message","아이디 또는 비밀번호가 일치하지 않습니다 ");
			ra.addFlashAttribute("message","아이디 또는 비밀번호가 일치하지 않습니다 ");
			// -> redirect시 잠깐 Session scope로 이동 후
			//    redirect가 완료되면 다시 Request scope로 이동
			
			// redirect시에도 request scope로 세팅된 데이터가 유지될 수 있도록 하는 방법을 
			// spring에서도 제공해줌
			// -> RedirectAttributes 객체 (컨트롤러 매개변수에 작성하면 사용가능)
			
		}
		
		return "redirect:/"; // 재요청 (
		
	}
	
	//로그아웃 
	@GetMapping("/logout")
	public String logout(HttpSession session , SessionStatus status) {
		// 로그아웃 == 세션을 없애는것 
		
		// * @SessionAttributes을 이용해서 session scope에 배치된 데이터는 
		//    SessionStatus라는 별도 객체를 이용해야만 없앨 수 있다.
		logger.info("로그아웃 수행됨");
		
		
		//session.invalidate();  // 기존 세션 무효화 방식으로는 안된다!
		status.setComplete(); // 세션이 할일이 완료됨 -> 정보 없앤다 (로그아웃시킨다!!!!)
		
		
		return "redirect:/"; // 메인페이지로 리다이렉트
	}
	
	//회원가입 화면 전환
	@GetMapping("/signUp") // get방식 -> a태그 /comm/member/signUp 
	public String signUp() {
		
		return "member/signUp";
		
	}
	
	
	// 이메일 중복 검사 
	@ResponseBody// ajex값을 반환할때 반드시 사용!!
	@GetMapping("/emailDupCheck")
	public int emailDupCheck(String memberEmail) {
			
		int result= service.emailDupCheck(memberEmail);
		
		// 컨트롤러에서 반환되는 값은 forward 또는 redirect를 위한 경로인 경우가 일반적
		// -> 반환되는 값은 경로로 인식됨 
		
		//-> 이를 해결하기위한 어노테이션 @ResponseBody가 존재함
		
		//@ResponseBody: 반환되는 값은 응답의 몸통(body)에 추가하여
		//				 이전 요청 주소로 돌아감 
		// -> 컨트롤러에서 반환되는 값이 경로가 아닌 "값 자체"로 인식됨
		
		return result ;
		

	}
	
	@ResponseBody
	@GetMapping("/nicknameDupCheck")
	public int nicknameDupCheck(String memberNickname) {
		int result =service.nicknameDupCheck(memberNickname);
		
		return result;
	}
	
	
	// 회원가입
	@PostMapping("/signUp")
	public String signUp(Member inputMember ,
						 String[] memberAddress ,
						 RedirectAttributes ra) {
		
		// 커멘드 객체를 이용해서 입력된 회원 정보를 잘 받아옴
		// 단, 같은 name을 가진 주소가 하나의 문자열로 합쳐서 세팅되어있음.
		// -> 도로명 주소에 "," 기호가 포함되는 경우가 있어 이를 구분자로 사용할 수 없음.
		
		// String[] memberAddress : 
		//		name이 memberAddress인 파라미터의 값을 모두 배열에 담아서 반환
		
		
		inputMember.setMemberAddress( String.join(",,", memberAddress));
		// String.join("구분자",배열)
		// 배열을 하나의 문자열로 합치는 메서드
		// 중간에 들어갈 구분자를 지정할 수있다.
		// [a,b,c] - join 진행 -> "a,,b,,c"
		
		if(inputMember.getMemberAddress().equals(",,,,")) { //주소가 입력 되지않은 경우
			
			inputMember.setMemberAddress(null);//null로변환
		}
		
		int result = service.signUp(inputMember);
		
		String message = null;
		String path = null;
		
		if(result>0) {// 성공
			
			message =" 회원 가입 성공 !";
			path = "redirect:/" ; //메인페이지
			
		}else {//실패
			
			message =" 회원 가입 실패 !";
			path = "redirect:/member/signUp" ; // 회원 가입 페이지로 리다이렉트
		}
		
		ra.addFlashAttribute("message",message);
		
		return path;
		
	}
	
	//회원 1명 정보 조회 (ajax)
	@ResponseBody //반환되는 값이 forward/redirect 경로가 아닌 값 자체임을 인식(ajax 비동기 통신시 사용)
	@PostMapping("/selectOne")
	public String selectOne(/*@RequestParam("memberEmail")*/ String memberEmail) {
		
		Member mem = service.selectOne(memberEmail);
		
		//JSON : 자바스크립트 객체 표기법으로 작성된 문자열(String)이다
		//"{K:V , K:V}"
		
		//GSON 라이브러리 : JSON을 쉽게 다루기 위한 Google에서 제공하는 라이브러리
		
		//Gson().toJson(Object) : 객체를 JSON 형태로 변환
		return new Gson().toJson(mem); //"{'memberEmail':'test01@naver.com','memberNickname':'테스트1',,,,}"
	}
	
	//회원 목록 조회 (ajax)
	@ResponseBody
	@GetMapping("/selectAll")
	public String selectAll() {
		
		List<Member> list = service.selectAll();
		
		return new Gson().toJson(list);
	}
	/* 스프링 예외 처리 방법 (3가지, 중복 사용 가능)
	 * 
	 * 1 순위 : 메서드 별로 예외 처리(try-catch / throws)
	 *  
	 * 2 순위 : 하나의 컨트롤러에서 발생하는 예외를 모아서 처리 
	 * 		  -> @ExceptionHandler (메서드에 작성)
	 * 
	 * 3 순위 : 전역(웹 애플리케이션)에서 발생하는 예외를 모아서 처리
	 *  	  -> @ControllerAdvice (클래스에 작성)
	 *  
	 */
	
	
	// 회원(member)컨트롤러에서 발생하는 예외를 모아서 처리
	/*@ExceptionHandler(Exception.class)
	public String ExceptionHandler(Exception e, Model model) {
		
		e.printStackTrace();
		model.addAttribute("errorMessage","서비스 이용중 문제가 발생했습니다");
		model.addAttribute("e",e);
		
		return "common/error";
	}*/
	
	
	
	
}
