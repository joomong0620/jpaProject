package com.jr.jpa.repository;

import java.util.List;
import java.util.Optional;

import com.jr.jpa.entity.Member;

public interface MemberRepository {

	
	/** 회원 등록
	 * @param member
	 */
	void save(Member member);

	/** 아이디가 일치하는 회원 조회
	 * @param memberId
	 * @return
	 */
	Optional<Member> findById(String memberId);
	//Optional<Member> : 값이 있을 수도 있고 없을 수도 있는 객체(Member)를 감싸는 래퍼 클래스
	// 					 결과(Member)가 null일 수도 있음을 명시적으로 표현

	
	/**회원 목록 조회
	 * @return memberList
	 */
	List<Member> findAll();

	
	/** 회원 삭제
	 * @param member
	 */
	void delete(Member member);

	
	/** 이름이 일치하는 회원 목록 조회
	 * @return name
	 * return memberList
	 */
	List<Member> findByMemberName(String name);
	
	
	

}
	


