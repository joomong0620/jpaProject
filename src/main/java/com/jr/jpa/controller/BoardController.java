package com.jr.jpa.controller;

import java.io.IOException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jr.jpa.dto.BoardDto;
import com.jr.jpa.dto.PageDto;
import com.jr.jpa.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor 
public class BoardController {
	
	
	private final BoardService boardService; // 한 번만 세팅 가능
	
	// (참고) 
	// json
	
	// 게시글 작성
	@PostMapping
	public ResponseEntity<Long> createBoard(@ModelAttribute BoardDto.Create boardCreate) 
			throws IllegalStateException, IOException{
		
		
		
		// @RequestBody : "단일 바디"를 통째로 DTO/객체로 변환
		// -> 대표 헤더 : application/sson, application/xml, text/plain 등
		
		// @ModelAttribute : "폼 바인딩" 개념. 키-값 쌍으로 자바 DTO에 바인딩
		// -> 대표 헤더 : application/x-www-form-urlencoded, multipart/form-data
		// -> 값이 ModelAttribute에 세팅되려면 dto에 getter/setter 필수로 작성
		
		
		// Content-Type(전송 방식)에 따라서 Spring이 내부적ㅇ르ㅗ 어떤 어노테이션으로 처리할 지 결정
		
		
	
		System.out.println(boardCreate);
		
		return ResponseEntity.ok(boardService.createBoard(boardCreate));
	}
	
	
	// 게시글 목록 조회
	// size : 몇개씩 가지고 올 것인지
	// page : 보고자 하는 페이지 번호 (0부터 시작)
	// sort  직렬 기준 : 속성, 방향
	@GetMapping
	public ResponseEntity <PageDto<BoardDto.Response>> getBoardList(
			@PageableDefault(size=10, page=0) Pageable pageble){
		// Pageable: 페이징 정보를 담은 객체
		return ResponseEntity.ok(new PageDto<>(boardService.getBoardList(pageble)));
	}
		
	// 게시글 상세 조회
	// 요청 주소 : /api/board/게시글번호
	// 해당하는 게시글이 존재하는 경우 : Board DTO로 반환
	// 없는 경우 : 해당 게시글이 존재하지 않습니다.
	
	@GetMapping("/{boardNo}")
	public ResponseEntity <BoardDto.Response> getBoardDetail(@PathVariable("boardNo") Long boardNo){
		return ResponseEntity.ok(boardService.getBoardDetail(boardNo));

	}
	// 게시글 수정
	// PUT  : 전체 수정
	// PATCH: 부분 수정
   @PatchMapping("/{boardNo}")
   public ResponseEntity<BoardDto.Response> updateBoard(
         @PathVariable("boardNo") Long boardNo,
         @ModelAttribute BoardDto.Update board)throws IllegalStateException, IOException{
//      System.out.println("hi"+board);
      return ResponseEntity.ok(boardService.updateBoard(boardNo, board));
   }	

   
   // 게시글 삭제 (상태 업데이트)
   // /api/board/8
   // -> 존재하면 해당 게시글 삭제 반환값 X
   // -> 존재하지 않는 게시글 번호일 경우 : 해당 게시글이 존재하지 않습니다. 에러 메세지
   @PutMapping("/{boardNo}")
   public ResponseEntity<Void> updateStatus(
           @PathVariable Long boardNo) {
	   //update board set status = N where boardNo = 8
       boardService.updateStatus(boardNo);

       return ResponseEntity.ok().build();
   }
		   
   
   
   
   
   

}
