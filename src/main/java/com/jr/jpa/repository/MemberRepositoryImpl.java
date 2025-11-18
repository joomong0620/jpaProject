package com.jr.jpa.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.jr.jpa.entity.Member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Repository
public class MemberRepositoryImpl implements MemberRepository{

	@PersistenceContext //EntityManager를 주입해줘
	private EntityManager em;
	// EntityManager
	// -JPA에서 엔티티를 관리하고 데이터베이스와 상호작용을 담당하는 핵심 객체
	// -영속성 컨텍스트를 생성하고 엔티티의 저장, 조회, 수정, 삭제 작업을 수행

	@Override
	public void save(Member member) {
		em.persist(member); // 엔티티를 영속 상태로 전환 -> JPA가 관리 시작
		// persist(): 엔티티 객체를 영속 상태로 만들어줌 (INSERT 예정)
		
	}

	// 아이디가 일치하는 회원 조회
	@Override
	public Optional<Member> findById(String memberId) {
		// 반환형 Optional<Member> : 결과(Member)가 null일 수도 있음을 명식적으로 표현
		
		return Optional.ofNullable(em.find(Member.class, memberId));
		// em.find(Member.class, memberId)
		// -> EntityManager를 이용해서, Member 엔티티에서 
		// memberId를 기준으로 1개의 엔티티만 조회
		// * find()는 PK로만 조회 가능
		
		//Optional.ofNullable()
		// - 값이 있으면 Optional<Member>, 
		// - 값이 없으면 Optional.empty() 반환 ==# 빈칸이 아니라 값이 비어있다는 표시이다.
		
		// em.find() 호출 시점에
		// 캐시에 있으면 -> DB에 안가고 캐시에서 반환
		// 캐시에 없으면 -> DB에서 SELECT 후 , 캐시에 저장 + 반환
		
		
	}

	// 회원 목록 조회
	@Override
	public List<Member> findAll() {
		return em.createQuery("select m from Member m", Member.class)
		// createQuery() : JPQL을 사용해서 쿼리를 만들겠다 라는 의미
		// JPQL(JPA Query Language) : 엔티티를 대상으로 SQL처럼 쿼리를 작성할 수 있게 해주는 언어
		
		.getResultList();
		// 쿼리를 실행하고 결과를 List<Member>로 반환
		
		
	}

	// 영속성 관리 : JPA가 엔티티 객체(Member 등)의 상태를 자동으로 추적해서 DB에 반영해주는 것
	
	// 영속성 컨텍스트
	// - 엔티티의 	변경정보를 메모리에 저장하는 환경
	
	// -> 여기에 저장된 엔티티는 JPA가 자동으로 상태를 추적하고 필요한 시점에 DB와 동기화
	
	
	// 회원 삭제
	@Override
	public void delete(Member member) {
		em.remove(member); // remove : 엔티티 삭제 
		
		
	}

	// 이름이 일치하는 회원 목록 조회
	@Override
	public List<Member> findByMemberName(String name) {
		//SELECT * FROM MEMBER WHERE MEMBER_NAME LIKE '&?%';
		
		String query = "select m from Member m where m.memberName LIKE :memberName"; 
		
		return em.createQuery(query, Member.class)
				//createQeury() : JPQ 을 사용해서 쿼리를 만들겠다라는 의미
				//Member.class : 결과를 Member 엔티티 타입으로 매핑하겠다.
				.setParameter("memberName", "%" + name + "%")
				// : memberName 이라는 파라미터에 값 세팅
				//ex) %길동%
				.getResultList();
				//쿼리를 실행하고 결과를 List<Member>로 반환
		
	}
	
	
	
	
	
}
