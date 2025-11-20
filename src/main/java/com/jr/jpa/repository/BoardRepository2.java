package com.jr.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jr.jpa.entity.Board;
import com.jr.jpa.enums.CommonEnums.Status;
															// pk 자료형
public interface BoardRepository2 extends JpaRepository <Board, Long> {

	
//	// 게시글 작성
//	Board save(Board board);

	// 게시글 목록 조회 => 기본적으로 제공 안해줌 지우면 안됨
	Page<Board> findByStatus(Status status, Pageable pageble);
	// -> Board 엔티티 조회해 올거니까 

//	// 게시글 상세 조회
//	Optional<Board> findById(Long boardNo);

//	// 게시글 삭제
//	void delete(Board board);
//	


}
