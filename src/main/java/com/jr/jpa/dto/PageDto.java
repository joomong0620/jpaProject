package com.jr.jpa.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

// 페이징 처리용 DTO
// <T>: 어떤 타입이든 가능
// 직렬화(객체를 내보낼 때) 시 getter 필수
@Getter
public class PageDto<T> {
	private List<T> content; // 데이터 목록
	private int currentPage; // 현재 페이지 번호
	private int totalPage; // 전체 페이지 수
	private long totalCount; // 전체 데이터 수
	private boolean hasNext; // 다음 페이지 여부
	private boolean hasPrevious; // 이전 페이지 여부

	
	// Page<T> : 페이지 결과를 담고 있는 인터페이스
	public PageDto(Page<T> page) {
		this.content = page.getContent(); // 데이터 목록
		this.currentPage = page.getNumber(); // 현재 페이지
		this.totalPage = page.getTotalPages(); // 총 페이지 수
		this.totalCount = page.getTotalElements(); // 총 데이터 수
		this.hasNext = page.hasNext(); // 다음 페이지 여부
		this.hasPrevious = page.hasPrevious(); // 이전 페이지 여부
		
	}
	
}
