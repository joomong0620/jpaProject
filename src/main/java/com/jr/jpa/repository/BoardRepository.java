package com.jr.jpa.repository;

import com.jr.jpa.entity.Board;

public interface BoardRepository {

	
	// 게시글 작성
	Board save(Board board);

}
