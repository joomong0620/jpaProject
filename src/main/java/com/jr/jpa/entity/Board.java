package com.jr.jpa.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.jr.jpa.enums.CommonEnums;
import com.jr.jpa.enums.CommonEnums.Status;

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
import lombok.Setter;




@Table(name="Board") // 생략 시 클래스 이름 (Board)이 테이블명이 됨.
@Entity  //해당 클래스가 데이터베이스 테이블과 매핑되는 객체임을 JPA에 알려주는 표시
@NoArgsConstructor(access=AccessLevel.PROTECTED) //JPA 스펙상 필수 + 외부 생성 방지
@AllArgsConstructor										// JPA 사용 하려면 꼭 써줘야함
@Builder
@Getter
@Setter
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
	// 비주인
	// orphanRemoval=true : (N:1) 또는 (1:N) 연관관계에서 자식의 생명주기를 부모가 통제 (주인 비주인 관계 X, 부모 자식 관계 O)
	// -> 부모 엔티티에서 자식과의 관계가 제거되면 자식도 자동으로 삭제 
	@OneToMany(mappedBy="board", cascade = CascadeType.ALL, orphanRemoval=true) // // mappedBy = "연관관계 주인의 필드명", mappedBy 있으면 비주인
	@Builder.Default // 기본값을 유지
	private List<Reply> replies = new ArrayList<>();	
	
	// 항상 쿼리문을 이용해서 사용했는데 우리는 객체 지향적으로 코드를 사용하고 싶으니까
	// orphanRemoval은 null 값을 세팅한다거나 해당 객체를 빼버리게 되면 더이상 관계가 끊긴 자식들을 다 삭제하라는 소리이다
		
	//Board : BoardTag(1:N)
	@OneToMany(mappedBy="board", cascade = CascadeType.ALL, orphanRemoval=true)
	@Builder.Default // replies랑 boardTags 둘다 넣어줘야함
	private List<BoardTag> boardTags = new ArrayList<>();
	
	// 연관관계 편의 메소드
	// Board와 Member 동기화
	public void addMember(Member member) {
		this.member = member;
		
		if(!member.getBoards().contains(this)) { // 반대쪽(Member)에 Board 추가
			member.getBoards().add(this);
	}
//주인 : BOARD, Reply
//비주인: MEMBER, TAG
	}
	
	
	public void changeFile(String originName, String changeName) {
		this.originName = originName;
		this.changeName = changeName;
		
	}
	
	
	// 제목 변경 메소드
	public void changeTitle(String boardTitle) {
		if(boardTitle != null && !boardTitle.isEmpty())
		this.boardTitle = boardTitle;
	}
	
	// 내용 변경 메소드
	public void changeContent(String boardContent) {
		if(boardContent != null && !boardContent.isEmpty())
		this.boardContent = boardContent;
		
		
		
	}

	// 게시글 삭제(상태 변경) 메소드
	public void setStatus(Status n) {
		this.status = n;
	}
	
}