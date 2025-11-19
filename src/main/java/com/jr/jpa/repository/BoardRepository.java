package com.jr.jpa.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jr.jpa.entity.Board;
import com.jr.jpa.enums.CommonEnums.Status;

public interface BoardRepository {

	
	// 게시글 작성
	Board save(Board board);

	// 게시글 목록 조회
	Page<Board> findByStatus(Status status, Pageable pageble);
	// -> Board 엔티티 조회해 올거니까 

	// 게시글 상세 조회
	Optional<Board> findById(Long boardNo);

	// 게시글 수정


}
