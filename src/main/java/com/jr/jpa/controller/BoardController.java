package com.jr.jpa.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jr.jpa.dto.BoardDto;
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
}
