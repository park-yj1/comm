package edu.kh.comm.member.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.member.model.vo.Member;

//저장소
@Repository// 영속성(지속성)을 가지는 DB/파일과 연결되는 객체임을 명시 + bean등록 
public class MemberDAO {

	
	
	// DAO는DB랑 연결하기 위한 Connection이 공통적으로 필요하다!
	// -> 필드에 선언
	// + Mybatis 영속성 프레임워크를 이용하려면 Connection을 이용해 만들어진 객체 
	//   SqlSessionTemplate을 사용
	//Connection을 사용안하고 SqlSessionTemplate을 사용할것이다.
	
	@Autowired // root-context.xml에서 생성된 SqlSessionTemplate bean을 의존성 주입(DI)
	private SqlSessionTemplate sqlSession ;
	
	private Logger logger = LoggerFactory.getLogger(MemberDAO.class);
	
	/**로그인 DAO
	 * @param inputMember
	 * @return
	 */
	public Member login(Member inputMember) {
		// 1행 조회 (파라미터X) 방법
		//int count = sqlSession.selectOne("namespace값.id값");
		//int count = sqlSession.selectOne("memberMapper.test1");
		//logger.debug(count+"");
		
		
		// 1행 조회(파라미터 O) 방법
		
		String memberNickname = sqlSession.selectOne("memberMapper.test2",inputMember.getMemberEmail());
		logger.debug(memberNickname);
//		select == test2(member-mapper.xml)
//		SELECT MEMBER_NICK FROM MEMBER
//		WHERE MEMBER_EMAIL='user01@kh.or.kr'
//		AND SECESSION_FL ='N';
		
		
		// 1행 조회(파라미터가 vo인 경우)
		//String memberTel = sqlSession.selectOne("memberMapper.test3",inputMember);
																	//inputMember 속에 memberEmail,memberPw 멤버 객체 전체를 보내겠다.(parameterType)			
		
		//logger.debug(memberTel);
		
		
		// 1행 조회 (파라미터가 VO, 반환되는 결과도 VO)
		Member loginMember = sqlSession.selectOne("memberMapper.login",inputMember);
																	// VO가 전달되게
		
		return loginMember;
	}

	/**이메일 중복 검사 DAO
	 * @param memberEmail
	 * @return result
	 */
	public int emailDupCheck(String memberEmail) {
		
		
		
		return sqlSession.selectOne("memberMapper.emailDupCheck",memberEmail);
	}

	/**닉네임 중복 검사 DAO
	 * @param memberNickname
	 * @return result
	 */
	public int nicknameDupCheck(String memberNickname) {
	
		return sqlSession.selectOne("memberMapper.nicknameDupCheck",memberNickname);
	}

	/** 회원가입 DAO
	 * @param inputMember
	 * @return result
	 */
	public int signUp(Member inputMember) {
		
		//INSERT UPDATE DELETE를 수행하기 위한 메서드가 존재함
		// ** insert()/update()/delete() 메서드의 반환 값은 int형으로 고정
		// -> mapper에서도 resultType이 항상 _int로 고정 
		// -> resultType 생략 가능 (묵시적으로 _int로 고정)
		
		return sqlSession.insert("memberMapper.signUp",inputMember);
	}

	/**회원 1명 정보 조회 DAO
	 * @param memberEmail
	 * @return mem
	 */
	public Member selectOne(String memberEmail) {
		
		return sqlSession.selectOne("memberMapper.selectOne",memberEmail);
	}

	/**회원 목록 조회 DAP
	 * @return list
	 */
	public List<Member> selectAll() {
		//selectList(): 여러 행을 조회-> 리턴 타입이 List
		// * 조회되는 모든 행을 순차적으로 접근해서 VO에 필드값을 다음은 후 List에 자동 추가
		
		return sqlSession.selectList("memberMapper.selectAll");
	}
	
	
	

}
