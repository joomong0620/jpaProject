package com.jr.jpa.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.jr.jpa.enums.CommonEnums;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;




@Table(name="Board") // 생략 시 클래스 이름 (Board)이 테이블명이 됨.
@Entity  //해당 클래스가 데이터베이스 테이블과 매핑되는 객체임을 JPA에 알려주는 표시
@NoArgsConstructor(access=AccessLevel.PROTECTED) //JPA 스펙상 필수 + 외부 생성 방지
@AllArgsConstructor										// JPA 사용 하려면 꼭 써줘야함
@Builder
@Getter

public class Board {
	// @Id : 기본키(PK) 지정
	// @Column: 컬럼 이름, 길이, null 허용 여부 설정

	@Id
	@Column(name="BOARD_NO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	// -> Oracle 12c 이상
	private Long boardNo;
	
	@Column(name="BOARD_TITLE", length = 30, nullable=false)
	private String boardTitle;
	
	@Lob // (Large OBject) -> 자동으로 CLOB(대용량 데이터)로 매핑
		 //	게시글 작성 구현할 때 에디터 같은 걸 쓰면 그 게시글 안에 있는 html 을 통채로
		 // 저장하게 되는데 그걸 varchar2로는 감당이 안돼서 clob으로 사용한다.
	@Column(name="BOARD_CONTENT", nullable=false)
	private String boardContent;

	
	//원본명
	@Column(name="ORIGIN_NAME", length = 100)
	private String originName;
	
	
	//변경명
	@Column(name="CHANGE_NAME", length = 100)
	private String changeName;
	
	// 생성일
	@Column(name="CREATE_DATE"/*, columnDefinition = "DATE"*/)
	private LocalDateTime createDate; //기본값 TIMESTAMP, length는 기본값 varchar2
	
	// 상태
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS", columnDefinition = "CHAR(1)")
	//columnDefinition : 직접적으로 타입을 지정하기 위한 옵션
	private CommonEnums.Status status;
	
	
	// 조회수
	@Column(name="COUNT", nullable = false)
	private Integer count;
	
	
	
	// Board : Member (N: 1)
	// Board에서 FK관리 -> 연관관계의 주인
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BOARD_WRITER", nullable=false)
	private Member member; 
	// 주인 -> 비주인 (정방향 탐색), 반대는 역방향
	
	
	@PrePersist // 기본값 설정
	public void onCreate() {
		this.createDate = LocalDateTime.now();
		if(this.status == null) {
			this.status = CommonEnums.Status.Y;
		}
		this.count = 0;
	}
	
	
	// Board : Reply(1:N)
	@OneToMany(mappedBy="board", cascade = CascadeType.ALL) // mappedBy 있으면 비주인
	// mappedBy = "연관관계 주인의 필드명"
		private List<Reply> replies = new ArrayList<>();
		private List<BoardTag> boardtags = new ArrayList<>();
	
		 
	
		
	
	
//주인 : BOARD, Reply
//비주인: MEMBER, TAG
	
	
	
	
	
	
	
	
}
