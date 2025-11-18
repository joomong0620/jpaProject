package com.jr.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jr.jpa.entity.Member;

public interface MemberRepository2 extends JpaRepository<Member, String> {
										  // JpaRepository<엔티티, PK 타입>
											// 구현체가 없어도 알아서 구현해준다.



	
	/** 이름이 완전히 일치하는 회원 목록 조회
	 * @return name
	 * return memberList
	 */
	
	// 메소드명으로 쿼리 생성하는 방법
	// **(주의)매개변수로 넘겨주는 변수명은 엔티티의 필드명과 같아야 한다.
	List<Member> findByMemberName(String memberName);
	
	
	// 이름을 포함하는 회원 목록 조회 + 나이 내림차순 정렬
	List<Member> findByMemberNameContainingOrderByAgeDesc(String memberName);
	
	

}
	


