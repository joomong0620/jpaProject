package com.jr.jpa.service;

import java.io.IOException;

import com.jr.jpa.dto.BoardDto.Create;

public interface BoardService {

   // 게시글 작성
   Long createBoard(Create boardCreate) throws IllegalStateException, IOException;

}
