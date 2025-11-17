package com.jr.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="Tag") // 생략 시 클래스 이름 (Board)이 테이블명이 됨.
@Entity  //해당 클래스가 데이터베이스 테이블과 매핑되는 객체임을 JPA에 알려주는 표시
@NoArgsConstructor(access=AccessLevel.PROTECTED) //JPA 스펙상 필수 + 외부 생성 방지
@AllArgsConstructor										// JPA 사용 하려면 꼭 써줘야함
@Builder
@Getter



public class Tag {
	@Id
	@Column(name="TAG_NO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long tagNo;
	
	
	@Column(name="TAG_NAME", length=30, nullable=false, unique=true)
	// 태그명은 유일해야 하므로 unique 제약조건 추가
	private String tagName;

//	@OneToMany(mappedBy="tag")
//	// mappedBy = "연관관계 주인의 필드명"
//		private List<Tag> tag = new ArrayList<>();

	// -> 양방향으로 보고싶으면 TAG에서 설정해주면 되는데 필요 없으면 안 쓰면 된다.


}
