package com.jr.jpa.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jr.jpa.dto.BoardDto.Create;
import com.jr.jpa.dto.BoardDto.Response;
import com.jr.jpa.dto.BoardDto.Update;

public interface BoardService {

   // 게시글 작성
   Long createBoard(Create boardCreate) throws IllegalStateException, IOException;
   
   // 게시글 목록 조회
   Page<Response> getBoardList(Pageable pageble);

   
   // 게시글 상세 조회
   Response getBoardDetail(Long boardNo);
   
   // 게시글 수정
   Response updateBoard(Long boardNo, Update updateBoard) throws IllegalStateException, IOException;


   // 게시글 삭제(상태 업데이트)
   void updateStatus(Long boardNo);
   


}
