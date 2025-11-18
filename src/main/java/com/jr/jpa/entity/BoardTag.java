package com.jr.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="BoardTag") // 생략 시 클래스 이름 (Board)이 테이블명이 됨.
@Entity  //해당 클래스가 데이터베이스 테이블과 매핑되는 객체임을 JPA에 알려주는 표시
@NoArgsConstructor(access=AccessLevel.PROTECTED) //JPA 스펙상 필수 + 외부 생성 방지
@AllArgsConstructor										// JPA 사용 하려면 꼭 써줘야함
@Builder
@Getter
public class BoardTag {
	
	
	//# int는 값이 없으면 기본값 0, Integer는 null 들어감
	@Id
	@Column(name="BOARD_TAG_NO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long boardTagNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BOARD_NO")
	// -> FK를 가진 쪽이 연관관계 주인
	private Board board;
	

	// BoardTag : Tag (1:N)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TAG_NO")
	private Tag tag; 
	
	
	// 연관 편의 메소드
	public void addBoard(Board board) {
	    this.board = board;
	    if(board.getBoardTags().contains(this)) {
	        board.getBoardTags().add(this);
	    }
	}

	
	
	
	
	
}
