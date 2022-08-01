package edu.kh.comm.member.model.dao;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
@Repository
public class MyPageDAO {

	@Autowired
	private SqlSessionTemplate sqlSession;


	// 파라미터가 Map인경우 -> Mapper에서 사용시 #{key} 를 작성하면
	// key에 대응되는 value가 출력됨
	// ex)  #{updateTel}  ->  '01099999999'

	/** 회원 정보 수정 DAO
	 * @param paramMap
	 * @return result
	 */
	public int updateInfo(Map<String, Object> paramMap) {
		return sqlSession.update("myPageMapper.updateInfo", paramMap);
	}


	/** 현재 로그인한 회원의 암호화된 비밀번호 조회 DAO
	 * @param memberNo
	 * @return encPw
	 */
	public String selectEncPw(int memberNo) {
		return sqlSession.selectOne("myPageMapper.selectEncPw", memberNo);
	}


	/** 비밀번호 변경 DAO
	 * @param paramMap
	 * @return result
	 */
	public int changePw(Map<String, Object> paramMap) {
		return sqlSession.update("myPageMapper.changePw", paramMap);
	}


	/** 회원 탈퇴 DAO
	 * @param memberNo
	 * @return result
	 */
	public int secession(int memberNo) {
		return sqlSession.update("myPageMapper.secession", memberNo);
	}


	/** 프로필 이미지 수정
	 * @param map
	 * @return result
	 */
	public int updateProfile(Map<String, Object> map) {
		return sqlSession.update("myPageMapper.updateProfile", map);
	}

}
