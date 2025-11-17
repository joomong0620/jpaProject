package com.jr.jpa.entity;

import java.time.LocalDateTime;

import com.jr.jpa.enums.CommonEnums;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 댓글 정보를 저장하는 엔티티 클래스
// Board 엔티티와 N:1 관계
// Member 엔티티와 N:1 관계

@Table(name="Reply") 
@Entity
@Builder
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reply {
	@Id // PK : NOT NULL, UNIQUE => 이미 포함되어있으니까 
	@Column(name="REPLY_NO")
	@GeneratedValue(strategy=GenerationType.IDENTITY) // 알아서 1씩 증가해라
	private Long replyNo;
	
	@Column(name="REPLY_CONTENT", length=400, nullable=false)
	private String replyContent;
	
	@Column(name="CREATE_DATE")
	private LocalDateTime createDate;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS", columnDefinition = "CHAR(1)", nullable = false)
	//columnDefinition : 직접적으로 타입을 지정하기 위한 옵션
	private CommonEnums.Status status;
	
	//Reply : Board(N:1)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REF_BNO", nullable=false)
	private Board board; // 주소값도 가지고 와야함
	// -> DB(REF_DNO)에는 board의 @ID 필드의 자료형이 저장된다. 
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPLY_WRITER")
	private Member member; 
	
	
	@PrePersist
	public void onCreate() {
		this.createDate = LocalDateTime.now();
		if(this.status == null) {
			this.status = CommonEnums.Status.Y;
		}
	}
	
	
	
	
	
	
	
}
