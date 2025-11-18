package com.jr.jpa.repository;

import org.springframework.stereotype.Repository;

import com.jr.jpa.entity.Board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class BoardRepositoryImpl implements BoardRepository {

	
	
	@PersistenceContext // EntityManager를 주입해줘
	private EntityManager em;
	// EntityManager
		// -JPA에서 엔티티를 관리하고 데이터베이스와 상호작용을 담당하는 핵심 객체
		// -영속성 컨텍스트를 생성하고 엔티티의 저장, 조회, 수정, 삭제 작업을 수행
	
	
	
	
	// 게시글 작성
	@Override
	public Board save(Board board) {
		em.persist(board);
		return board; // return 했으니까 get이용해서 Board 꺼낼 수 있다.
	}

}
