package com.jr.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.jr.jpa.entity.Board;
import com.jr.jpa.enums.CommonEnums.Status;

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



	// 게시글 목록 조회
	@Override
	public Page<Board> findByStatus(Status status, Pageable pageble) {
		String query = "select b from Board b where b.status =:status1";
		// JPQL : 엔티티 중심으로 작성					// :status == 위치홀더랑 같음, 다만 순서 없고 그냥 변수임
		
		// 게시글 목록 조회
		List<Board> boards = em.createQuery(query, Board.class)
								.setParameter("status1", status)
								.setFirstResult((int)pageble.getOffset()) // 어디서 부터 가지고 올 것인지
								.setMaxResults(pageble.getPageSize()) // 몇 개를 가지고 올 것인지 
								.getResultList();
		// 총 게시글 수
		String countQuery = "select count(b) from Board b where b.status = :status";
		
		
		// em.createQuery(실행할 쿼리문, 반환형) # Long 타입임
		Long totalCount = em.createQuery(countQuery, Long.class)
						// .setParameter(:변수명, 값)
						.setParameter("status", status)
						.getSingleResult();
		
		// Page<T> 인터페이스의 기본 구현체를 통해서 페이징한 정보를 한번에 전달할 수 있다.
		// PageImpl<>(content, pageable, total) :
		
		return new PageImpl<Board>(boards, pageble, totalCount);
		
	}


	// 게시글 상세 조회
	@Override
	public Optional<Board> findById(Long boardNo) {
		if (boardNo == null)return Optional.empty(); //값 없음
		return Optional.ofNullable(em.find(Board.class, boardNo));
		// Optional.ofNullable()
		// 찾은 경우 -> Optional<Board>
		// 못 찾은 경우 -> optioanl.empty()
	}


	// 게시글 삭제
	@Override
	public void delete(Board board) {
		em.remove(board);
	}
	


}
